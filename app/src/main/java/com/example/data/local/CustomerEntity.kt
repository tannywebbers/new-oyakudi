package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.CustomerCase

@Entity(tableName = "customers")
data class CustomerEntity(
    @PrimaryKey val id: String,
    val customerName: String,
    val phoneNumber: String,
    val referenceNumber: String,
    val loanPlatform: String,
    val platformLogoUrl: String?,
    val avatarUrl: String?,
    val requestType: String,
    val status: String,
    val loanAppStatus: String,
    val currentCreditLimit: Long,
    val requestedCreditLimit: Long,
    val outstandingLoanBalance: Long,
    val loanDuration: String,
    val loanTenorMonths: Int,
    val interestRate: Double,
    val monthlyRepayment: Long,
    val nextLoanEligibleAmount: Long,
    val loanAppStatusReason: String?,
    val retryAvailableAt: String?,
    val loanDecisionAt: String?,
    val expiresAt: String?,
    val approvedAt: String?,
    val declinedAt: String?,
    val createdAt: String,
    val updatedAt: String,
    val shareToken: String?
) {
    fun toDomain(): CustomerCase {
        return CustomerCase(
            id = id,
            customerName = customerName,
            phoneNumber = phoneNumber,
            referenceNumber = referenceNumber,
            loanPlatform = loanPlatform,
            platformLogoUrl = platformLogoUrl,
            avatarUrl = avatarUrl,
            requestType = requestType,
            status = status,
            loanAppStatus = loanAppStatus,
            currentCreditLimit = currentCreditLimit,
            requestedCreditLimit = requestedCreditLimit,
            outstandingLoanBalance = outstandingLoanBalance,
            loanDuration = loanDuration,
            loanTenorMonths = loanTenorMonths,
            interestRate = interestRate,
            monthlyRepayment = monthlyRepayment,
            nextLoanEligibleAmount = nextLoanEligibleAmount,
            loanAppStatusReason = loanAppStatusReason,
            retryAvailableAt = retryAvailableAt,
            loanDecisionAt = loanDecisionAt,
            expiresAt = expiresAt,
            approvedAt = approvedAt,
            declinedAt = declinedAt,
            createdAt = createdAt,
            updatedAt = updatedAt,
            shareToken = shareToken
        )
    }

    companion object {
        fun fromDomain(domain: CustomerCase): CustomerEntity {
            return CustomerEntity(
                id = domain.id,
                customerName = domain.customerName,
                phoneNumber = domain.phoneNumber,
                referenceNumber = domain.referenceNumber,
                loanPlatform = domain.loanPlatform,
                platformLogoUrl = domain.platformLogoUrl,
                avatarUrl = domain.avatarUrl,
                requestType = domain.requestType,
                status = domain.status,
                loanAppStatus = domain.loanAppStatus,
                currentCreditLimit = domain.currentCreditLimit,
                requestedCreditLimit = domain.requestedCreditLimit,
                outstandingLoanBalance = domain.outstandingLoanBalance,
                loanDuration = domain.loanDuration,
                loanTenorMonths = domain.loanTenorMonths,
                interestRate = domain.interestRate,
                monthlyRepayment = domain.monthlyRepayment,
                nextLoanEligibleAmount = domain.nextLoanEligibleAmount,
                loanAppStatusReason = domain.loanAppStatusReason,
                retryAvailableAt = domain.retryAvailableAt,
                loanDecisionAt = domain.loanDecisionAt,
                expiresAt = domain.expiresAt,
                approvedAt = domain.approvedAt,
                declinedAt = domain.declinedAt,
                createdAt = domain.createdAt,
                updatedAt = domain.updatedAt,
                shareToken = domain.shareToken
            )
        }
    }
}

@Entity(tableName = "application_cooldowns")
data class CooldownEntity(
    @PrimaryKey val phoneNumber: String,
    val initiatedAt: Long,
    val retryAvailableTimestamp: Long,
    val formattedRetryDate: String,
    val reason: String
)
