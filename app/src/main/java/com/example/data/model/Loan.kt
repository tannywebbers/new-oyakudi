package com.example.data.model

data class LoanDetail(
    val id: String = "loan_001",
    val amount: Long = 10000,
    val currencySymbol: String = "₦",
    val interestRate: Double = 0.08,
    val repaymentPeriodDays: Int = 180,
    val repaymentAmount: Long = 11440,
    val repaymentDate: String = "2027-02-10",
    val status: String = "PENDING"
) {
    val formattedAmount: String
        get() = "$currencySymbol%,d".format(amount)
        
    val formattedInterestRate: String
        get() = "%.2f%%".format(interestRate)
        
    val formattedRepaymentPeriod: String
        get() = "$repaymentPeriodDays days"
        
    val formattedRepaymentAmount: String
        get() = "$currencySymbol%,d".format(repaymentAmount)
}

data class LoanApplicationRequest(
    val amount: Long = 10000,
    val periodDays: Int = 180,
    val bankAccount: String? = null,
    val bankName: String? = null
)

data class LoanApplicationResult(
    val success: Boolean,
    val message: String,
    val referenceNumber: String? = null,
    val loanDetail: LoanDetail? = null
)
