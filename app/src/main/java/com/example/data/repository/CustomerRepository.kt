package com.example.data.repository

import com.example.data.local.CooldownEntity
import com.example.data.local.CustomerDao
import com.example.data.local.CustomerEntity
import com.example.data.local.TransactionDao
import com.example.data.local.TransactionEntity
import com.example.data.model.CreditTransaction
import com.example.data.model.CustomerCase
import com.example.data.model.LoanAppSubmissionResult
import com.example.data.model.getCurrentDateFormatted
import com.example.data.supabase.SupabaseClient
import com.example.data.supabase.SupabaseCustomerDto
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface CustomerRepository {
    val currentCustomer: StateFlow<CustomerCase>
    suspend fun loginWithPhone(phoneNumber: String): Result<CustomerCase>
    suspend fun refreshCustomerFromDb(): CustomerCase
    suspend fun checkWithdrawCooldown(): CooldownEntity?
    suspend fun submitLoanApplication(amount: Long, durationDays: Int = 180): Result<LoanAppSubmissionResult>
    suspend fun updateCustomerStatusForTesting(status: String, loanAppStatus: String)
    suspend fun setCreditLimitForTesting(newLimit: Long)
    suspend fun clearCooldown()
    suspend fun clearAllCustomerData()
}

class CustomerRepositoryImpl(
    private val customerDao: CustomerDao,
    private val transactionDao: TransactionDao,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) : CustomerRepository {

    private val _currentCustomer = MutableStateFlow(CustomerCase())
    override val currentCustomer: StateFlow<CustomerCase> = _currentCustomer.asStateFlow()

    init {
        scope.launch {
            val cached = customerDao.observeCurrentCustomer()
            cached.collect { entity ->
                if (entity != null) {
                    _currentCustomer.value = entity.toDomain()
                } else {
                    _currentCustomer.value = CustomerCase()
                }
            }
        }
    }

    private fun normalizePhoneDigits(phone: String): String {
        return phone.filter { it.isDigit() }
    }

    override suspend fun loginWithPhone(phoneNumber: String): Result<CustomerCase> = withContext(Dispatchers.IO) {
        val digits = normalizePhoneDigits(phoneNumber)
        if (digits.length < 7) {
            return@withContext Result.failure(Exception("Please enter a valid phone number (at least 7 digits)."))
        }

        // Query the 'customers' table in Supabase filtering by phone_number
        val result = SupabaseClient.getCustomerByPhoneNumber(phoneNumber)
        if (result.isFailure) {
            val error = result.exceptionOrNull() ?: Exception("Customer not found or not migrated, contact support")
            return@withContext Result.failure(error)
        }

        val matchedCustomer = result.getOrThrow().toDomain()

        // Only allow accounts whose platform is PixKudi to proceed to the next screen.
        val platform = matchedCustomer.loanPlatform.trim()
        if (!platform.equals("pixkudi", ignoreCase = true)) {
            return@withContext Result.failure(
                Exception("This phone number is not registered on PixKudi.")
            )
        }

        // Clear previous customer data and save the authentic matched customer to local Room DB
        customerDao.clearAll()
        customerDao.insertCustomer(CustomerEntity.fromDomain(matchedCustomer))
        _currentCustomer.value = matchedCustomer

        Result.success(matchedCustomer)
    }

    override suspend fun clearAllCustomerData() = withContext(Dispatchers.IO) {
        customerDao.clearAll()
        _currentCustomer.value = CustomerCase()
    }

    override suspend fun refreshCustomerFromDb(): CustomerCase = withContext(Dispatchers.IO) {
        val current = _currentCustomer.value
        if (current.phoneNumber.isEmpty()) return@withContext current

        val result = SupabaseClient.getCustomerByPhoneNumber(current.phoneNumber)
        if (result.isSuccess) {
            val domain = result.getOrThrow().toDomain()
            customerDao.insertCustomer(CustomerEntity.fromDomain(domain))
            _currentCustomer.value = domain
            return@withContext domain
        }

        val cached = customerDao.getCustomerById(current.id)
        if (cached != null) {
            val domain = cached.toDomain()
            _currentCustomer.value = domain
            return@withContext domain
        }
        _currentCustomer.value
    }

    override suspend fun checkWithdrawCooldown(): CooldownEntity? = withContext(Dispatchers.IO) {
        val phone = _currentCustomer.value.phoneNumber
        if (phone.isEmpty()) return@withContext null
        val cooldown = customerDao.getCooldown(phone) ?: return@withContext null
        val now = System.currentTimeMillis()
        if (now < cooldown.retryAvailableTimestamp) {
            cooldown
        } else {
            customerDao.clearCooldown(phone)
            null
        }
    }

    override suspend fun submitLoanApplication(amount: Long, durationDays: Int): Result<LoanAppSubmissionResult> = withContext(Dispatchers.IO) {
        val customer = _currentCustomer.value
        val phone = customer.phoneNumber

        // 1. Enforce the once-a-day withdrawal limit. This applies to every attempt,
        //    regardless of the outcome (approved, pending, declined or failed).
        val cooldown = checkWithdrawCooldown()
        if (cooldown != null) {
            val diffMs = cooldown.retryAvailableTimestamp - System.currentTimeMillis()
            val hoursRemaining = ((diffMs / (1000 * 60 * 60)) + 1).toInt().coerceAtLeast(1)
            return@withContext Result.success(
                LoanAppSubmissionResult.CooldownBlocked(
                    retryDate = cooldown.formattedRetryDate,
                    hoursRemaining = hoursRemaining
                )
            )
        }

        // 2. Open the 24-hour withdrawal window up-front so a member can only withdraw
        //    once per day, no matter how this attempt resolves.
        customerDao.insertCooldown(
            CooldownEntity(
                phoneNumber = phone,
                initiatedAt = System.currentTimeMillis(),
                retryAvailableTimestamp = System.currentTimeMillis() + (24L * 60 * 60 * 1000),
                formattedRetryDate = getFutureFormattedDate(1),
                reason = "Daily withdrawal limit"
            )
        )

        // 3. Check loan application status and case status
        if (customer.loanAppStatus.equals("failed", ignoreCase = true) || customer.loanAppStatus.equals("disbursement_suspended", ignoreCase = true)) {
            val retryFormatted = customer.retryAvailableAt ?: getFutureFormattedDate(1)
            val reason = customer.loanAppStatusReason ?: "Disbursement suspended. Please contact support."

            return@withContext Result.success(
                LoanAppSubmissionResult.FailedOrSuspended(
                    reason = reason,
                    retryDate = retryFormatted,
                    daysRemaining = 1
                )
            )
        }

        if (customer.loanAppStatus.equals("declined", ignoreCase = true) || 
            customer.loanAppStatus.equals("rejected", ignoreCase = true) || 
            customer.status.equals("declined", ignoreCase = true)) {
            val retryDate = customer.retryAvailableAt ?: getFutureFormattedDate(1)
            val reason = customer.loanAppStatusReason ?: "Application could not be approved at this time."
            return@withContext Result.success(
                LoanAppSubmissionResult.Declined(
                    reason = reason,
                    retryDate = retryDate
                )
            )
        }

        if (customer.loanAppStatus.equals("pending", ignoreCase = true) || 
            customer.status.equals("pending", ignoreCase = true) || 
            customer.status.equals("processing", ignoreCase = true)) {
            val message = customer.loanAppStatusReason ?: "Your application is currently under review by our underwriting team."
            return@withContext Result.success(
                LoanAppSubmissionResult.Pending(
                    message = message,
                    decisionDate = customer.retryAvailableAt ?: getFutureFormattedDate(2)
                )
            )
        }

        // 4. Approved Loan Processing
        val totalInterest = (amount * (customer.interestRate / 100.0) * durationDays).toLong()
        val totalRepayment = amount + totalInterest
        val dueDate = getFutureFormattedDate(durationDays)

        // Update balances: increase outstanding loan balance, update available credit
        val newOutstanding = customer.outstandingLoanBalance + totalRepayment
        val newAvailableLimit = (customer.currentCreditLimit - amount).coerceAtLeast(0)

        val updatedCustomer = customer.copy(
            outstandingLoanBalance = newOutstanding,
            currentCreditLimit = newAvailableLimit
        )

        // Update local database customer
        customerDao.updateCustomerBalances(
            id = customer.id,
            outstandingBalance = newOutstanding,
            creditLimit = newAvailableLimit
        )

        // Insert Transaction record into local Room Database
        val transactionId = "LN${System.currentTimeMillis() % 1000000}"
        transactionDao.insertTransaction(
            TransactionEntity(
                id = transactionId,
                customerId = customer.id,
                title = "Credit Withdrawal",
                amount = amount,
                date = getCurrentDateFormatted(),
                status = "Active",
                repaymentAmount = "₦%,d".format(totalRepayment),
                dueDate = dueDate
            )
        )

        _currentCustomer.value = updatedCustomer

        // Sync back to Supabase database
        SupabaseClient.updateBalances(customer.id, newOutstanding, newAvailableLimit)

        Result.success(
            LoanAppSubmissionResult.Approved(
                amount = amount,
                totalRepayment = totalRepayment,
                dueDate = dueDate,
                message = "Your loan has been approved! The funds will arrive in your bank account shortly."
            )
        )
    }

    override suspend fun updateCustomerStatusForTesting(status: String, loanAppStatus: String) = withContext(Dispatchers.IO) {
        val current = _currentCustomer.value
        val updated = current.copy(status = status, loanAppStatus = loanAppStatus)
        customerDao.updateCustomerStatus(current.id, status, loanAppStatus)
        _currentCustomer.value = updated
    }

    override suspend fun setCreditLimitForTesting(newLimit: Long) = withContext(Dispatchers.IO) {
        val current = _currentCustomer.value
        val updated = current.copy(currentCreditLimit = newLimit)
        customerDao.updateCustomerBalances(current.id, current.outstandingLoanBalance, newLimit)
        _currentCustomer.value = updated
    }

    override suspend fun clearCooldown() = withContext(Dispatchers.IO) {
        customerDao.clearCooldown(_currentCustomer.value.phoneNumber)
    }

    private fun getFutureFormattedDate(daysInFuture: Int): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, daysInFuture)
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
    }
}
