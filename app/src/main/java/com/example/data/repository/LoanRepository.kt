package com.example.data.repository

import com.example.data.local.TransactionDao
import com.example.data.model.LoanApplicationRequest
import com.example.data.model.LoanApplicationResult
import com.example.data.model.LoanDetail
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers

fun getFutureDateFormatted(daysInFuture: Int = 180): String {
    val cal = Calendar.getInstance()
    cal.add(Calendar.DAY_OF_YEAR, daysInFuture)
    return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
}

interface LoanRepository {
    val currentLoan: Flow<LoanDetail?>
    suspend fun getLoanDetails(): LoanDetail
    suspend fun submitLoanApplication(request: LoanApplicationRequest): Result<LoanApplicationResult>
}

class LoanRepositoryImpl(
    private val customerRepository: CustomerRepository,
    private val transactionDao: TransactionDao
) : LoanRepository {

    override val currentLoan: Flow<LoanDetail?> = customerRepository.currentCustomer.map { customer ->
        if (customer.outstandingLoanBalance > 0) {
            LoanDetail(
                id = "loan_${customer.id}",
                amount = customer.outstandingLoanBalance,
                currencySymbol = "₦",
                interestRate = customer.interestRate,
                repaymentPeriodDays = 180,
                repaymentAmount = customer.outstandingLoanBalance,
                repaymentDate = getFutureDateFormatted(180),
                status = "ACTIVE"
            )
        } else {
            null
        }
    }

    override suspend fun getLoanDetails(): LoanDetail = withContext(Dispatchers.IO) {
        val customer = customerRepository.currentCustomer.value
        LoanDetail(
            id = "loan_${customer.id}",
            amount = customer.outstandingLoanBalance,
            currencySymbol = "₦",
            interestRate = customer.interestRate,
            repaymentPeriodDays = 180,
            repaymentAmount = customer.outstandingLoanBalance,
            repaymentDate = getFutureDateFormatted(180),
            status = if (customer.outstandingLoanBalance > 0) "ACTIVE" else "NONE"
        )
    }

    override suspend fun submitLoanApplication(request: LoanApplicationRequest): Result<LoanApplicationResult> {
        val submission = customerRepository.submitLoanApplication(request.amount, request.periodDays)
        return submission.map { result ->
            LoanApplicationResult(
                success = true,
                message = "Application processed",
                referenceNumber = "SAPA-${System.currentTimeMillis() % 1000000}",
                loanDetail = LoanDetail(
                    id = "LN${System.currentTimeMillis() % 1000000}",
                    amount = request.amount,
                    currencySymbol = "₦",
                    interestRate = 0.08,
                    repaymentPeriodDays = request.periodDays,
                    repaymentAmount = request.amount,
                    repaymentDate = getFutureDateFormatted(request.periodDays),
                    status = "PENDING"
                )
            )
        }
    }
}
