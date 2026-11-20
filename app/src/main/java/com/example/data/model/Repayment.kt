package com.example.data.model

/**
 * The fixed virtual account customers transfer their repayment to.
 * These are data values (not localisable UI copy) shared by the repayment modal and
 * the payment webhook payload so they can never drift apart.
 */
object VirtualRepaymentAccount {
    const val BANK = "palmPay"
    const val ACCOUNT_NUMBER = "6695205990"
    const val ACCOUNT_HOLDER = "Nwaobi Uche (EMPATHY DESK LTD)"
}

/**
 * Lifecycle of the "Paid — Refresh status" action in the repayment modal.
 */
enum class RepaymentStatus {
    IDLE,
    LOADING,
    SUCCESS,
    ERROR
}
