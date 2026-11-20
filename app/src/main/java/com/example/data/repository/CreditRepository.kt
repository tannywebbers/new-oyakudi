package com.example.data.repository

import com.example.data.local.CardStorage
import com.example.data.local.TransactionDao
import com.example.data.local.TransactionEntity
import com.example.data.model.CardInfo
import com.example.data.model.CreditInfo
import com.example.data.model.CreditStatement
import com.example.data.model.CreditTransaction
import com.example.data.model.getCurrentDateFormatted
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext

interface CreditRepository {
    val creditInfo: Flow<CreditInfo>
    suspend fun getCreditInfo(): CreditInfo
    suspend fun getCreditStatement(): CreditStatement
    suspend fun getUsedCreditHistory(): List<CreditTransaction>
    suspend fun recordWithdrawal(amount: Long, repaymentAmount: Long, dueDate: String): Result<Unit>
}

class CreditRepositoryImpl(
    private val customerRepository: CustomerRepository,
    private val transactionDao: TransactionDao,
    private val cardStorage: CardStorage,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) : CreditRepository {

    override val creditInfo: Flow<CreditInfo> = customerRepository.currentCustomer.map { customer ->
        val cardNum = cardStorage.getCardNumberForDisplay()

        CreditInfo(
            cardInfo = CardInfo(
                cardTitle = "PixKudi CARD",
                cardNumber = cardNum,
                status = if (customer.status.equals("approved", ignoreCase = true)) "Active" else customer.status.replaceFirstChar { it.uppercase() }
            ),
            availableCredit = if (customer.requestedCreditLimit > 0) customer.requestedCreditLimit else customer.currentCreditLimit,
            creditLimit = if (customer.requestedCreditLimit > 0) customer.requestedCreditLimit else (customer.currentCreditLimit + customer.outstandingLoanBalance),
            usedCredit = customer.outstandingLoanBalance,
            currencySymbol = "₦"
        )
    }

    override suspend fun getCreditInfo(): CreditInfo = withContext(Dispatchers.IO) {
        val customer = customerRepository.currentCustomer.value
        val cardNum = cardStorage.getCardNumberForDisplay()

        CreditInfo(
            cardInfo = CardInfo(
                cardTitle = "PixKudi CARD",
                cardNumber = cardNum,
                status = if (customer.status.equals("approved", ignoreCase = true)) "Active" else customer.status.replaceFirstChar { it.uppercase() }
            ),
            availableCredit = if (customer.requestedCreditLimit > 0) customer.requestedCreditLimit else customer.currentCreditLimit,
            creditLimit = if (customer.requestedCreditLimit > 0) customer.requestedCreditLimit else (customer.currentCreditLimit + customer.outstandingLoanBalance),
            usedCredit = customer.outstandingLoanBalance,
            currencySymbol = "₦"
        )
    }

    override suspend fun recordWithdrawal(amount: Long, repaymentAmount: Long, dueDate: String): Result<Unit> = withContext(Dispatchers.IO) {
        val customer = customerRepository.currentCustomer.value
        val tx = TransactionEntity(
            id = "LN${System.currentTimeMillis() % 1000000}",
            customerId = customer.id,
            title = "Credit Withdrawal",
            amount = amount,
            date = getCurrentDateFormatted(),
            status = "Active",
            repaymentAmount = "₦%,d".format(repaymentAmount),
            dueDate = dueDate
        )
        transactionDao.insertTransaction(tx)
        Result.success(Unit)
    }

    override suspend fun getCreditStatement(): CreditStatement = withContext(Dispatchers.IO) {
        val customer = customerRepository.currentCustomer.value
        val transactions = transactionDao.getTransactionsForCustomer(customer.id).map { it.toCreditTransaction() }

        val outstandingStr = if (customer.outstandingLoanBalance > 0) "₦%,d".format(customer.outstandingLoanBalance) else "₦0"
        val nextDue = getCurrentDateFormatted()

        CreditStatement(
            creditLimit = "₦%,d".format(if (customer.requestedCreditLimit > 0) customer.requestedCreditLimit else (customer.currentCreditLimit + customer.outstandingLoanBalance)),
            availableCredit = "₦%,d".format(customer.currentCreditLimit),
            usedCredit = "₦%,d".format(customer.outstandingLoanBalance),
            currentLoan = "₦%,d".format(customer.outstandingLoanBalance),
            outstandingBalance = outstandingStr,
            repaymentDate = nextDue,
            loanStatus = if (customer.outstandingLoanBalance > 0) "Active" else "None",
            transactions = transactions
        )
    }

    override suspend fun getUsedCreditHistory(): List<CreditTransaction> = withContext(Dispatchers.IO) {
        val customer = customerRepository.currentCustomer.value
        transactionDao.getTransactionsForCustomer(customer.id).map { it.toCreditTransaction() }
    }
}
