package com.example.data.model

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Customer case status enum matching PostgreSQL schema.
 */
enum class CaseStatus(val value: String) {
    PENDING("pending"),
    PROCESSING("processing"),
    APPROVED("approved"),
    DECLINED("declined");

    companion object {
        fun fromString(value: String?): CaseStatus {
            return entries.find { it.value.equals(value, ignoreCase = true) } ?: PENDING
        }
    }
}

/**
 * Loan application status enum matching PostgreSQL schema.
 */
enum class LoanAppStatus(val value: String) {
    APPROVED("approved"),
    PENDING("pending"),
    FAILED("failed"),
    DISBURSEMENT_SUSPENDED("disbursement_suspended");

    companion object {
        fun fromString(value: String?): LoanAppStatus {
            return entries.find { it.value.equals(value, ignoreCase = true) } ?: PENDING
        }
    }
}

/**
 * Full Customer model matching the Empathy Desk / PixKudi database schema.
 */
data class CustomerCase(
    val id: String = "",
    val customerName: String = "PixKudi",
    val phoneNumber: String = "",
    val referenceNumber: String = "",
    val loanPlatform: String = "PixKudi",
    val platformLogoUrl: String? = null,
    val avatarUrl: String? = null,
    val requestType: String = "credit",
    val status: String = "pending", // 'pending' | 'processing' | 'approved' | 'declined'
    val loanAppStatus: String = "pending", // 'approved' | 'pending' | 'failed' | 'disbursement_suspended'
    val currentCreditLimit: Long = 0,
    val requestedCreditLimit: Long = 0,
    val outstandingLoanBalance: Long = 0,
    val loanDuration: String = "180",
    val loanTenorMonths: Int = 6,
    val interestRate: Double = 0.08,
    val monthlyRepayment: Long = 0,
    val nextLoanEligibleAmount: Long = 0,
    val loanAppStatusReason: String? = null,
    val retryAvailableAt: String? = null,
    val loanDecisionAt: String? = null,
    val expiresAt: String? = null,
    val approvedAt: String? = null,
    val declinedAt: String? = null,
    val createdAt: String = getCurrentDateFormatted(),
    val updatedAt: String = getCurrentDateFormatted(),
    val shareToken: String? = null
) {
    val isStatusApproved: Boolean
        get() = status.equals("approved", ignoreCase = true) || loanAppStatus.equals("approved", ignoreCase = true)

    val isStatusPendingOrProcessing: Boolean
        get() = (status.equals("pending", ignoreCase = true) || status.equals("processing", ignoreCase = true)) &&
                !loanAppStatus.equals("approved", ignoreCase = true)

    val isStatusDeclined: Boolean
        get() = status.equals("declined", ignoreCase = true) || 
                loanAppStatus.equals("failed", ignoreCase = true) || 
                loanAppStatus.equals("disbursement_suspended", ignoreCase = true) ||
                loanAppStatus.equals("declined", ignoreCase = true) ||
                loanAppStatus.equals("rejected", ignoreCase = true)

    val isLoanAppApproved: Boolean
        get() = loanAppStatus.equals("approved", ignoreCase = true)

    val isLoanAppFailedOrSuspended: Boolean
        get() = loanAppStatus.equals("failed", ignoreCase = true) || 
                loanAppStatus.equals("disbursement_suspended", ignoreCase = true) ||
                loanAppStatus.equals("declined", ignoreCase = true) ||
                loanAppStatus.equals("rejected", ignoreCase = true)

    /**
     * Effective limit available for credit is the customer's requested_credit_limit directly from database.
     */
    val effectiveAvailableCredit: Long
        get() = if (requestedCreditLimit > 0) requestedCreditLimit else currentCreditLimit

    val formattedAvailableCredit: String
        get() = "₦%,d".format(effectiveAvailableCredit)

    val formattedRequestedCreditLimit: String
        get() = "₦%,d".format(effectiveAvailableCredit)

    val formattedOutstandingBalance: String
        get() = "₦%,d".format(outstandingLoanBalance)

    val formattedCurrentLimit: String
        get() = "₦%,d".format(currentCreditLimit)

    val durationDays: Int
        get() {
            val digits = loanDuration.filter { it.isDigit() }
            return digits.toIntOrNull() ?: 180
        }
}

/**
 * Result of submitting or checking loan application.
 */
sealed class LoanAppSubmissionResult {
    data class Approved(
        val amount: Long,
        val totalRepayment: Long,
        val dueDate: String,
        val message: String = "Your loan has been approved! The funds will arrive in your bank account shortly."
    ) : LoanAppSubmissionResult()

    data class Pending(
        val message: String = "Your application is currently under review by our credit team.",
        val decisionDate: String? = null
    ) : LoanAppSubmissionResult()

    data class Declined(
        val reason: String,
        val retryDate: String
    ) : LoanAppSubmissionResult()

    data class FailedOrSuspended(
        val reason: String,
        val retryDate: String,
        val daysRemaining: Int = 14
    ) : LoanAppSubmissionResult()

    data class CooldownBlocked(
        val retryDate: String,
        val hoursRemaining: Int
    ) : LoanAppSubmissionResult()
}
