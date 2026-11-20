package com.example.data.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getCurrentDateFormatted(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(Date())
}

data class CardInfo(
    val cardTitle: String = "PixKudi CARD",
    val cardNumber: String = "6623 0000 0000 0000",
    val status: String = "Active",
    val expireOn: String = getCurrentDateFormatted()
)

data class CreditInfo(
    val cardInfo: CardInfo = CardInfo(),
    val availableCredit: Long = 0,
    val creditLimit: Long = 0,
    val usedCredit: Long = 0,
    val currencySymbol: String = "₦"
) {
    val formattedAvailableCredit: String
        get() = "$currencySymbol%,d".format(availableCredit)
        
    val formattedCreditLimit: String
        get() = "$currencySymbol%,d".format(creditLimit)
        
    val formattedUsedCredit: String
        get() = "$currencySymbol%,d".format(usedCredit)
}

data class CreditTransaction(
    val id: String,
    val title: String = "Credit Withdrawal",
    val amount: Long,
    val date: String,
    val status: String = "Active",
    val repaymentAmount: String = "₦0",
    val dueDate: String = "",
    val currencySymbol: String = "₦"
) {
    val formattedAmount: String
        get() = "$currencySymbol%,d".format(amount)
}

data class CreditStatement(
    val creditLimit: String = "₦0",
    val availableCredit: String = "₦0",
    val usedCredit: String = "₦0",
    val currentLoan: String = "₦0",
    val outstandingBalance: String = "₦0",
    val repaymentDate: String = "",
    val loanStatus: String = "None",
    val transactions: List<CreditTransaction> = emptyList()
)
