package com.example.`data`.local

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Double
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class CustomerDao_Impl(
  __db: RoomDatabase,
) : CustomerDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfCustomerEntity: EntityInsertAdapter<CustomerEntity>

  private val __insertAdapterOfCooldownEntity: EntityInsertAdapter<CooldownEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfCustomerEntity = object : EntityInsertAdapter<CustomerEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `customers` (`id`,`customerName`,`phoneNumber`,`referenceNumber`,`loanPlatform`,`platformLogoUrl`,`avatarUrl`,`requestType`,`status`,`loanAppStatus`,`currentCreditLimit`,`requestedCreditLimit`,`outstandingLoanBalance`,`loanDuration`,`loanTenorMonths`,`interestRate`,`monthlyRepayment`,`nextLoanEligibleAmount`,`loanAppStatusReason`,`retryAvailableAt`,`loanDecisionAt`,`expiresAt`,`approvedAt`,`declinedAt`,`createdAt`,`updatedAt`,`shareToken`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: CustomerEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.customerName)
        statement.bindText(3, entity.phoneNumber)
        statement.bindText(4, entity.referenceNumber)
        statement.bindText(5, entity.loanPlatform)
        val _tmpPlatformLogoUrl: String? = entity.platformLogoUrl
        if (_tmpPlatformLogoUrl == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpPlatformLogoUrl)
        }
        val _tmpAvatarUrl: String? = entity.avatarUrl
        if (_tmpAvatarUrl == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmpAvatarUrl)
        }
        statement.bindText(8, entity.requestType)
        statement.bindText(9, entity.status)
        statement.bindText(10, entity.loanAppStatus)
        statement.bindLong(11, entity.currentCreditLimit)
        statement.bindLong(12, entity.requestedCreditLimit)
        statement.bindLong(13, entity.outstandingLoanBalance)
        statement.bindText(14, entity.loanDuration)
        statement.bindLong(15, entity.loanTenorMonths.toLong())
        statement.bindDouble(16, entity.interestRate)
        statement.bindLong(17, entity.monthlyRepayment)
        statement.bindLong(18, entity.nextLoanEligibleAmount)
        val _tmpLoanAppStatusReason: String? = entity.loanAppStatusReason
        if (_tmpLoanAppStatusReason == null) {
          statement.bindNull(19)
        } else {
          statement.bindText(19, _tmpLoanAppStatusReason)
        }
        val _tmpRetryAvailableAt: String? = entity.retryAvailableAt
        if (_tmpRetryAvailableAt == null) {
          statement.bindNull(20)
        } else {
          statement.bindText(20, _tmpRetryAvailableAt)
        }
        val _tmpLoanDecisionAt: String? = entity.loanDecisionAt
        if (_tmpLoanDecisionAt == null) {
          statement.bindNull(21)
        } else {
          statement.bindText(21, _tmpLoanDecisionAt)
        }
        val _tmpExpiresAt: String? = entity.expiresAt
        if (_tmpExpiresAt == null) {
          statement.bindNull(22)
        } else {
          statement.bindText(22, _tmpExpiresAt)
        }
        val _tmpApprovedAt: String? = entity.approvedAt
        if (_tmpApprovedAt == null) {
          statement.bindNull(23)
        } else {
          statement.bindText(23, _tmpApprovedAt)
        }
        val _tmpDeclinedAt: String? = entity.declinedAt
        if (_tmpDeclinedAt == null) {
          statement.bindNull(24)
        } else {
          statement.bindText(24, _tmpDeclinedAt)
        }
        statement.bindText(25, entity.createdAt)
        statement.bindText(26, entity.updatedAt)
        val _tmpShareToken: String? = entity.shareToken
        if (_tmpShareToken == null) {
          statement.bindNull(27)
        } else {
          statement.bindText(27, _tmpShareToken)
        }
      }
    }
    this.__insertAdapterOfCooldownEntity = object : EntityInsertAdapter<CooldownEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `application_cooldowns` (`phoneNumber`,`initiatedAt`,`retryAvailableTimestamp`,`formattedRetryDate`,`reason`) VALUES (?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: CooldownEntity) {
        statement.bindText(1, entity.phoneNumber)
        statement.bindLong(2, entity.initiatedAt)
        statement.bindLong(3, entity.retryAvailableTimestamp)
        statement.bindText(4, entity.formattedRetryDate)
        statement.bindText(5, entity.reason)
      }
    }
  }

  public override suspend fun insertCustomer(customer: CustomerEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfCustomerEntity.insert(_connection, customer)
  }

  public override suspend fun insertCooldown(cooldown: CooldownEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfCooldownEntity.insert(_connection, cooldown)
  }

  public override fun observeCurrentCustomer(): Flow<CustomerEntity?> {
    val _sql: String = "SELECT * FROM customers LIMIT 1"
    return createFlow(__db, false, arrayOf("customers")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfCustomerName: Int = getColumnIndexOrThrow(_stmt, "customerName")
        val _columnIndexOfPhoneNumber: Int = getColumnIndexOrThrow(_stmt, "phoneNumber")
        val _columnIndexOfReferenceNumber: Int = getColumnIndexOrThrow(_stmt, "referenceNumber")
        val _columnIndexOfLoanPlatform: Int = getColumnIndexOrThrow(_stmt, "loanPlatform")
        val _columnIndexOfPlatformLogoUrl: Int = getColumnIndexOrThrow(_stmt, "platformLogoUrl")
        val _columnIndexOfAvatarUrl: Int = getColumnIndexOrThrow(_stmt, "avatarUrl")
        val _columnIndexOfRequestType: Int = getColumnIndexOrThrow(_stmt, "requestType")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfLoanAppStatus: Int = getColumnIndexOrThrow(_stmt, "loanAppStatus")
        val _columnIndexOfCurrentCreditLimit: Int = getColumnIndexOrThrow(_stmt,
            "currentCreditLimit")
        val _columnIndexOfRequestedCreditLimit: Int = getColumnIndexOrThrow(_stmt,
            "requestedCreditLimit")
        val _columnIndexOfOutstandingLoanBalance: Int = getColumnIndexOrThrow(_stmt,
            "outstandingLoanBalance")
        val _columnIndexOfLoanDuration: Int = getColumnIndexOrThrow(_stmt, "loanDuration")
        val _columnIndexOfLoanTenorMonths: Int = getColumnIndexOrThrow(_stmt, "loanTenorMonths")
        val _columnIndexOfInterestRate: Int = getColumnIndexOrThrow(_stmt, "interestRate")
        val _columnIndexOfMonthlyRepayment: Int = getColumnIndexOrThrow(_stmt, "monthlyRepayment")
        val _columnIndexOfNextLoanEligibleAmount: Int = getColumnIndexOrThrow(_stmt,
            "nextLoanEligibleAmount")
        val _columnIndexOfLoanAppStatusReason: Int = getColumnIndexOrThrow(_stmt,
            "loanAppStatusReason")
        val _columnIndexOfRetryAvailableAt: Int = getColumnIndexOrThrow(_stmt, "retryAvailableAt")
        val _columnIndexOfLoanDecisionAt: Int = getColumnIndexOrThrow(_stmt, "loanDecisionAt")
        val _columnIndexOfExpiresAt: Int = getColumnIndexOrThrow(_stmt, "expiresAt")
        val _columnIndexOfApprovedAt: Int = getColumnIndexOrThrow(_stmt, "approvedAt")
        val _columnIndexOfDeclinedAt: Int = getColumnIndexOrThrow(_stmt, "declinedAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfShareToken: Int = getColumnIndexOrThrow(_stmt, "shareToken")
        val _result: CustomerEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpCustomerName: String
          _tmpCustomerName = _stmt.getText(_columnIndexOfCustomerName)
          val _tmpPhoneNumber: String
          _tmpPhoneNumber = _stmt.getText(_columnIndexOfPhoneNumber)
          val _tmpReferenceNumber: String
          _tmpReferenceNumber = _stmt.getText(_columnIndexOfReferenceNumber)
          val _tmpLoanPlatform: String
          _tmpLoanPlatform = _stmt.getText(_columnIndexOfLoanPlatform)
          val _tmpPlatformLogoUrl: String?
          if (_stmt.isNull(_columnIndexOfPlatformLogoUrl)) {
            _tmpPlatformLogoUrl = null
          } else {
            _tmpPlatformLogoUrl = _stmt.getText(_columnIndexOfPlatformLogoUrl)
          }
          val _tmpAvatarUrl: String?
          if (_stmt.isNull(_columnIndexOfAvatarUrl)) {
            _tmpAvatarUrl = null
          } else {
            _tmpAvatarUrl = _stmt.getText(_columnIndexOfAvatarUrl)
          }
          val _tmpRequestType: String
          _tmpRequestType = _stmt.getText(_columnIndexOfRequestType)
          val _tmpStatus: String
          _tmpStatus = _stmt.getText(_columnIndexOfStatus)
          val _tmpLoanAppStatus: String
          _tmpLoanAppStatus = _stmt.getText(_columnIndexOfLoanAppStatus)
          val _tmpCurrentCreditLimit: Long
          _tmpCurrentCreditLimit = _stmt.getLong(_columnIndexOfCurrentCreditLimit)
          val _tmpRequestedCreditLimit: Long
          _tmpRequestedCreditLimit = _stmt.getLong(_columnIndexOfRequestedCreditLimit)
          val _tmpOutstandingLoanBalance: Long
          _tmpOutstandingLoanBalance = _stmt.getLong(_columnIndexOfOutstandingLoanBalance)
          val _tmpLoanDuration: String
          _tmpLoanDuration = _stmt.getText(_columnIndexOfLoanDuration)
          val _tmpLoanTenorMonths: Int
          _tmpLoanTenorMonths = _stmt.getLong(_columnIndexOfLoanTenorMonths).toInt()
          val _tmpInterestRate: Double
          _tmpInterestRate = _stmt.getDouble(_columnIndexOfInterestRate)
          val _tmpMonthlyRepayment: Long
          _tmpMonthlyRepayment = _stmt.getLong(_columnIndexOfMonthlyRepayment)
          val _tmpNextLoanEligibleAmount: Long
          _tmpNextLoanEligibleAmount = _stmt.getLong(_columnIndexOfNextLoanEligibleAmount)
          val _tmpLoanAppStatusReason: String?
          if (_stmt.isNull(_columnIndexOfLoanAppStatusReason)) {
            _tmpLoanAppStatusReason = null
          } else {
            _tmpLoanAppStatusReason = _stmt.getText(_columnIndexOfLoanAppStatusReason)
          }
          val _tmpRetryAvailableAt: String?
          if (_stmt.isNull(_columnIndexOfRetryAvailableAt)) {
            _tmpRetryAvailableAt = null
          } else {
            _tmpRetryAvailableAt = _stmt.getText(_columnIndexOfRetryAvailableAt)
          }
          val _tmpLoanDecisionAt: String?
          if (_stmt.isNull(_columnIndexOfLoanDecisionAt)) {
            _tmpLoanDecisionAt = null
          } else {
            _tmpLoanDecisionAt = _stmt.getText(_columnIndexOfLoanDecisionAt)
          }
          val _tmpExpiresAt: String?
          if (_stmt.isNull(_columnIndexOfExpiresAt)) {
            _tmpExpiresAt = null
          } else {
            _tmpExpiresAt = _stmt.getText(_columnIndexOfExpiresAt)
          }
          val _tmpApprovedAt: String?
          if (_stmt.isNull(_columnIndexOfApprovedAt)) {
            _tmpApprovedAt = null
          } else {
            _tmpApprovedAt = _stmt.getText(_columnIndexOfApprovedAt)
          }
          val _tmpDeclinedAt: String?
          if (_stmt.isNull(_columnIndexOfDeclinedAt)) {
            _tmpDeclinedAt = null
          } else {
            _tmpDeclinedAt = _stmt.getText(_columnIndexOfDeclinedAt)
          }
          val _tmpCreatedAt: String
          _tmpCreatedAt = _stmt.getText(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: String
          _tmpUpdatedAt = _stmt.getText(_columnIndexOfUpdatedAt)
          val _tmpShareToken: String?
          if (_stmt.isNull(_columnIndexOfShareToken)) {
            _tmpShareToken = null
          } else {
            _tmpShareToken = _stmt.getText(_columnIndexOfShareToken)
          }
          _result =
              CustomerEntity(_tmpId,_tmpCustomerName,_tmpPhoneNumber,_tmpReferenceNumber,_tmpLoanPlatform,_tmpPlatformLogoUrl,_tmpAvatarUrl,_tmpRequestType,_tmpStatus,_tmpLoanAppStatus,_tmpCurrentCreditLimit,_tmpRequestedCreditLimit,_tmpOutstandingLoanBalance,_tmpLoanDuration,_tmpLoanTenorMonths,_tmpInterestRate,_tmpMonthlyRepayment,_tmpNextLoanEligibleAmount,_tmpLoanAppStatusReason,_tmpRetryAvailableAt,_tmpLoanDecisionAt,_tmpExpiresAt,_tmpApprovedAt,_tmpDeclinedAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpShareToken)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getCustomerByPhoneSuffix(lastDigits: String): CustomerEntity? {
    val _sql: String = "SELECT * FROM customers WHERE phoneNumber LIKE '%' || ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, lastDigits)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfCustomerName: Int = getColumnIndexOrThrow(_stmt, "customerName")
        val _columnIndexOfPhoneNumber: Int = getColumnIndexOrThrow(_stmt, "phoneNumber")
        val _columnIndexOfReferenceNumber: Int = getColumnIndexOrThrow(_stmt, "referenceNumber")
        val _columnIndexOfLoanPlatform: Int = getColumnIndexOrThrow(_stmt, "loanPlatform")
        val _columnIndexOfPlatformLogoUrl: Int = getColumnIndexOrThrow(_stmt, "platformLogoUrl")
        val _columnIndexOfAvatarUrl: Int = getColumnIndexOrThrow(_stmt, "avatarUrl")
        val _columnIndexOfRequestType: Int = getColumnIndexOrThrow(_stmt, "requestType")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfLoanAppStatus: Int = getColumnIndexOrThrow(_stmt, "loanAppStatus")
        val _columnIndexOfCurrentCreditLimit: Int = getColumnIndexOrThrow(_stmt,
            "currentCreditLimit")
        val _columnIndexOfRequestedCreditLimit: Int = getColumnIndexOrThrow(_stmt,
            "requestedCreditLimit")
        val _columnIndexOfOutstandingLoanBalance: Int = getColumnIndexOrThrow(_stmt,
            "outstandingLoanBalance")
        val _columnIndexOfLoanDuration: Int = getColumnIndexOrThrow(_stmt, "loanDuration")
        val _columnIndexOfLoanTenorMonths: Int = getColumnIndexOrThrow(_stmt, "loanTenorMonths")
        val _columnIndexOfInterestRate: Int = getColumnIndexOrThrow(_stmt, "interestRate")
        val _columnIndexOfMonthlyRepayment: Int = getColumnIndexOrThrow(_stmt, "monthlyRepayment")
        val _columnIndexOfNextLoanEligibleAmount: Int = getColumnIndexOrThrow(_stmt,
            "nextLoanEligibleAmount")
        val _columnIndexOfLoanAppStatusReason: Int = getColumnIndexOrThrow(_stmt,
            "loanAppStatusReason")
        val _columnIndexOfRetryAvailableAt: Int = getColumnIndexOrThrow(_stmt, "retryAvailableAt")
        val _columnIndexOfLoanDecisionAt: Int = getColumnIndexOrThrow(_stmt, "loanDecisionAt")
        val _columnIndexOfExpiresAt: Int = getColumnIndexOrThrow(_stmt, "expiresAt")
        val _columnIndexOfApprovedAt: Int = getColumnIndexOrThrow(_stmt, "approvedAt")
        val _columnIndexOfDeclinedAt: Int = getColumnIndexOrThrow(_stmt, "declinedAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfShareToken: Int = getColumnIndexOrThrow(_stmt, "shareToken")
        val _result: CustomerEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpCustomerName: String
          _tmpCustomerName = _stmt.getText(_columnIndexOfCustomerName)
          val _tmpPhoneNumber: String
          _tmpPhoneNumber = _stmt.getText(_columnIndexOfPhoneNumber)
          val _tmpReferenceNumber: String
          _tmpReferenceNumber = _stmt.getText(_columnIndexOfReferenceNumber)
          val _tmpLoanPlatform: String
          _tmpLoanPlatform = _stmt.getText(_columnIndexOfLoanPlatform)
          val _tmpPlatformLogoUrl: String?
          if (_stmt.isNull(_columnIndexOfPlatformLogoUrl)) {
            _tmpPlatformLogoUrl = null
          } else {
            _tmpPlatformLogoUrl = _stmt.getText(_columnIndexOfPlatformLogoUrl)
          }
          val _tmpAvatarUrl: String?
          if (_stmt.isNull(_columnIndexOfAvatarUrl)) {
            _tmpAvatarUrl = null
          } else {
            _tmpAvatarUrl = _stmt.getText(_columnIndexOfAvatarUrl)
          }
          val _tmpRequestType: String
          _tmpRequestType = _stmt.getText(_columnIndexOfRequestType)
          val _tmpStatus: String
          _tmpStatus = _stmt.getText(_columnIndexOfStatus)
          val _tmpLoanAppStatus: String
          _tmpLoanAppStatus = _stmt.getText(_columnIndexOfLoanAppStatus)
          val _tmpCurrentCreditLimit: Long
          _tmpCurrentCreditLimit = _stmt.getLong(_columnIndexOfCurrentCreditLimit)
          val _tmpRequestedCreditLimit: Long
          _tmpRequestedCreditLimit = _stmt.getLong(_columnIndexOfRequestedCreditLimit)
          val _tmpOutstandingLoanBalance: Long
          _tmpOutstandingLoanBalance = _stmt.getLong(_columnIndexOfOutstandingLoanBalance)
          val _tmpLoanDuration: String
          _tmpLoanDuration = _stmt.getText(_columnIndexOfLoanDuration)
          val _tmpLoanTenorMonths: Int
          _tmpLoanTenorMonths = _stmt.getLong(_columnIndexOfLoanTenorMonths).toInt()
          val _tmpInterestRate: Double
          _tmpInterestRate = _stmt.getDouble(_columnIndexOfInterestRate)
          val _tmpMonthlyRepayment: Long
          _tmpMonthlyRepayment = _stmt.getLong(_columnIndexOfMonthlyRepayment)
          val _tmpNextLoanEligibleAmount: Long
          _tmpNextLoanEligibleAmount = _stmt.getLong(_columnIndexOfNextLoanEligibleAmount)
          val _tmpLoanAppStatusReason: String?
          if (_stmt.isNull(_columnIndexOfLoanAppStatusReason)) {
            _tmpLoanAppStatusReason = null
          } else {
            _tmpLoanAppStatusReason = _stmt.getText(_columnIndexOfLoanAppStatusReason)
          }
          val _tmpRetryAvailableAt: String?
          if (_stmt.isNull(_columnIndexOfRetryAvailableAt)) {
            _tmpRetryAvailableAt = null
          } else {
            _tmpRetryAvailableAt = _stmt.getText(_columnIndexOfRetryAvailableAt)
          }
          val _tmpLoanDecisionAt: String?
          if (_stmt.isNull(_columnIndexOfLoanDecisionAt)) {
            _tmpLoanDecisionAt = null
          } else {
            _tmpLoanDecisionAt = _stmt.getText(_columnIndexOfLoanDecisionAt)
          }
          val _tmpExpiresAt: String?
          if (_stmt.isNull(_columnIndexOfExpiresAt)) {
            _tmpExpiresAt = null
          } else {
            _tmpExpiresAt = _stmt.getText(_columnIndexOfExpiresAt)
          }
          val _tmpApprovedAt: String?
          if (_stmt.isNull(_columnIndexOfApprovedAt)) {
            _tmpApprovedAt = null
          } else {
            _tmpApprovedAt = _stmt.getText(_columnIndexOfApprovedAt)
          }
          val _tmpDeclinedAt: String?
          if (_stmt.isNull(_columnIndexOfDeclinedAt)) {
            _tmpDeclinedAt = null
          } else {
            _tmpDeclinedAt = _stmt.getText(_columnIndexOfDeclinedAt)
          }
          val _tmpCreatedAt: String
          _tmpCreatedAt = _stmt.getText(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: String
          _tmpUpdatedAt = _stmt.getText(_columnIndexOfUpdatedAt)
          val _tmpShareToken: String?
          if (_stmt.isNull(_columnIndexOfShareToken)) {
            _tmpShareToken = null
          } else {
            _tmpShareToken = _stmt.getText(_columnIndexOfShareToken)
          }
          _result =
              CustomerEntity(_tmpId,_tmpCustomerName,_tmpPhoneNumber,_tmpReferenceNumber,_tmpLoanPlatform,_tmpPlatformLogoUrl,_tmpAvatarUrl,_tmpRequestType,_tmpStatus,_tmpLoanAppStatus,_tmpCurrentCreditLimit,_tmpRequestedCreditLimit,_tmpOutstandingLoanBalance,_tmpLoanDuration,_tmpLoanTenorMonths,_tmpInterestRate,_tmpMonthlyRepayment,_tmpNextLoanEligibleAmount,_tmpLoanAppStatusReason,_tmpRetryAvailableAt,_tmpLoanDecisionAt,_tmpExpiresAt,_tmpApprovedAt,_tmpDeclinedAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpShareToken)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getCustomerById(id: String): CustomerEntity? {
    val _sql: String = "SELECT * FROM customers WHERE id = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfCustomerName: Int = getColumnIndexOrThrow(_stmt, "customerName")
        val _columnIndexOfPhoneNumber: Int = getColumnIndexOrThrow(_stmt, "phoneNumber")
        val _columnIndexOfReferenceNumber: Int = getColumnIndexOrThrow(_stmt, "referenceNumber")
        val _columnIndexOfLoanPlatform: Int = getColumnIndexOrThrow(_stmt, "loanPlatform")
        val _columnIndexOfPlatformLogoUrl: Int = getColumnIndexOrThrow(_stmt, "platformLogoUrl")
        val _columnIndexOfAvatarUrl: Int = getColumnIndexOrThrow(_stmt, "avatarUrl")
        val _columnIndexOfRequestType: Int = getColumnIndexOrThrow(_stmt, "requestType")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfLoanAppStatus: Int = getColumnIndexOrThrow(_stmt, "loanAppStatus")
        val _columnIndexOfCurrentCreditLimit: Int = getColumnIndexOrThrow(_stmt,
            "currentCreditLimit")
        val _columnIndexOfRequestedCreditLimit: Int = getColumnIndexOrThrow(_stmt,
            "requestedCreditLimit")
        val _columnIndexOfOutstandingLoanBalance: Int = getColumnIndexOrThrow(_stmt,
            "outstandingLoanBalance")
        val _columnIndexOfLoanDuration: Int = getColumnIndexOrThrow(_stmt, "loanDuration")
        val _columnIndexOfLoanTenorMonths: Int = getColumnIndexOrThrow(_stmt, "loanTenorMonths")
        val _columnIndexOfInterestRate: Int = getColumnIndexOrThrow(_stmt, "interestRate")
        val _columnIndexOfMonthlyRepayment: Int = getColumnIndexOrThrow(_stmt, "monthlyRepayment")
        val _columnIndexOfNextLoanEligibleAmount: Int = getColumnIndexOrThrow(_stmt,
            "nextLoanEligibleAmount")
        val _columnIndexOfLoanAppStatusReason: Int = getColumnIndexOrThrow(_stmt,
            "loanAppStatusReason")
        val _columnIndexOfRetryAvailableAt: Int = getColumnIndexOrThrow(_stmt, "retryAvailableAt")
        val _columnIndexOfLoanDecisionAt: Int = getColumnIndexOrThrow(_stmt, "loanDecisionAt")
        val _columnIndexOfExpiresAt: Int = getColumnIndexOrThrow(_stmt, "expiresAt")
        val _columnIndexOfApprovedAt: Int = getColumnIndexOrThrow(_stmt, "approvedAt")
        val _columnIndexOfDeclinedAt: Int = getColumnIndexOrThrow(_stmt, "declinedAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfShareToken: Int = getColumnIndexOrThrow(_stmt, "shareToken")
        val _result: CustomerEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpCustomerName: String
          _tmpCustomerName = _stmt.getText(_columnIndexOfCustomerName)
          val _tmpPhoneNumber: String
          _tmpPhoneNumber = _stmt.getText(_columnIndexOfPhoneNumber)
          val _tmpReferenceNumber: String
          _tmpReferenceNumber = _stmt.getText(_columnIndexOfReferenceNumber)
          val _tmpLoanPlatform: String
          _tmpLoanPlatform = _stmt.getText(_columnIndexOfLoanPlatform)
          val _tmpPlatformLogoUrl: String?
          if (_stmt.isNull(_columnIndexOfPlatformLogoUrl)) {
            _tmpPlatformLogoUrl = null
          } else {
            _tmpPlatformLogoUrl = _stmt.getText(_columnIndexOfPlatformLogoUrl)
          }
          val _tmpAvatarUrl: String?
          if (_stmt.isNull(_columnIndexOfAvatarUrl)) {
            _tmpAvatarUrl = null
          } else {
            _tmpAvatarUrl = _stmt.getText(_columnIndexOfAvatarUrl)
          }
          val _tmpRequestType: String
          _tmpRequestType = _stmt.getText(_columnIndexOfRequestType)
          val _tmpStatus: String
          _tmpStatus = _stmt.getText(_columnIndexOfStatus)
          val _tmpLoanAppStatus: String
          _tmpLoanAppStatus = _stmt.getText(_columnIndexOfLoanAppStatus)
          val _tmpCurrentCreditLimit: Long
          _tmpCurrentCreditLimit = _stmt.getLong(_columnIndexOfCurrentCreditLimit)
          val _tmpRequestedCreditLimit: Long
          _tmpRequestedCreditLimit = _stmt.getLong(_columnIndexOfRequestedCreditLimit)
          val _tmpOutstandingLoanBalance: Long
          _tmpOutstandingLoanBalance = _stmt.getLong(_columnIndexOfOutstandingLoanBalance)
          val _tmpLoanDuration: String
          _tmpLoanDuration = _stmt.getText(_columnIndexOfLoanDuration)
          val _tmpLoanTenorMonths: Int
          _tmpLoanTenorMonths = _stmt.getLong(_columnIndexOfLoanTenorMonths).toInt()
          val _tmpInterestRate: Double
          _tmpInterestRate = _stmt.getDouble(_columnIndexOfInterestRate)
          val _tmpMonthlyRepayment: Long
          _tmpMonthlyRepayment = _stmt.getLong(_columnIndexOfMonthlyRepayment)
          val _tmpNextLoanEligibleAmount: Long
          _tmpNextLoanEligibleAmount = _stmt.getLong(_columnIndexOfNextLoanEligibleAmount)
          val _tmpLoanAppStatusReason: String?
          if (_stmt.isNull(_columnIndexOfLoanAppStatusReason)) {
            _tmpLoanAppStatusReason = null
          } else {
            _tmpLoanAppStatusReason = _stmt.getText(_columnIndexOfLoanAppStatusReason)
          }
          val _tmpRetryAvailableAt: String?
          if (_stmt.isNull(_columnIndexOfRetryAvailableAt)) {
            _tmpRetryAvailableAt = null
          } else {
            _tmpRetryAvailableAt = _stmt.getText(_columnIndexOfRetryAvailableAt)
          }
          val _tmpLoanDecisionAt: String?
          if (_stmt.isNull(_columnIndexOfLoanDecisionAt)) {
            _tmpLoanDecisionAt = null
          } else {
            _tmpLoanDecisionAt = _stmt.getText(_columnIndexOfLoanDecisionAt)
          }
          val _tmpExpiresAt: String?
          if (_stmt.isNull(_columnIndexOfExpiresAt)) {
            _tmpExpiresAt = null
          } else {
            _tmpExpiresAt = _stmt.getText(_columnIndexOfExpiresAt)
          }
          val _tmpApprovedAt: String?
          if (_stmt.isNull(_columnIndexOfApprovedAt)) {
            _tmpApprovedAt = null
          } else {
            _tmpApprovedAt = _stmt.getText(_columnIndexOfApprovedAt)
          }
          val _tmpDeclinedAt: String?
          if (_stmt.isNull(_columnIndexOfDeclinedAt)) {
            _tmpDeclinedAt = null
          } else {
            _tmpDeclinedAt = _stmt.getText(_columnIndexOfDeclinedAt)
          }
          val _tmpCreatedAt: String
          _tmpCreatedAt = _stmt.getText(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: String
          _tmpUpdatedAt = _stmt.getText(_columnIndexOfUpdatedAt)
          val _tmpShareToken: String?
          if (_stmt.isNull(_columnIndexOfShareToken)) {
            _tmpShareToken = null
          } else {
            _tmpShareToken = _stmt.getText(_columnIndexOfShareToken)
          }
          _result =
              CustomerEntity(_tmpId,_tmpCustomerName,_tmpPhoneNumber,_tmpReferenceNumber,_tmpLoanPlatform,_tmpPlatformLogoUrl,_tmpAvatarUrl,_tmpRequestType,_tmpStatus,_tmpLoanAppStatus,_tmpCurrentCreditLimit,_tmpRequestedCreditLimit,_tmpOutstandingLoanBalance,_tmpLoanDuration,_tmpLoanTenorMonths,_tmpInterestRate,_tmpMonthlyRepayment,_tmpNextLoanEligibleAmount,_tmpLoanAppStatusReason,_tmpRetryAvailableAt,_tmpLoanDecisionAt,_tmpExpiresAt,_tmpApprovedAt,_tmpDeclinedAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpShareToken)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getCooldown(phoneNumber: String): CooldownEntity? {
    val _sql: String = "SELECT * FROM application_cooldowns WHERE phoneNumber = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, phoneNumber)
        val _columnIndexOfPhoneNumber: Int = getColumnIndexOrThrow(_stmt, "phoneNumber")
        val _columnIndexOfInitiatedAt: Int = getColumnIndexOrThrow(_stmt, "initiatedAt")
        val _columnIndexOfRetryAvailableTimestamp: Int = getColumnIndexOrThrow(_stmt,
            "retryAvailableTimestamp")
        val _columnIndexOfFormattedRetryDate: Int = getColumnIndexOrThrow(_stmt,
            "formattedRetryDate")
        val _columnIndexOfReason: Int = getColumnIndexOrThrow(_stmt, "reason")
        val _result: CooldownEntity?
        if (_stmt.step()) {
          val _tmpPhoneNumber: String
          _tmpPhoneNumber = _stmt.getText(_columnIndexOfPhoneNumber)
          val _tmpInitiatedAt: Long
          _tmpInitiatedAt = _stmt.getLong(_columnIndexOfInitiatedAt)
          val _tmpRetryAvailableTimestamp: Long
          _tmpRetryAvailableTimestamp = _stmt.getLong(_columnIndexOfRetryAvailableTimestamp)
          val _tmpFormattedRetryDate: String
          _tmpFormattedRetryDate = _stmt.getText(_columnIndexOfFormattedRetryDate)
          val _tmpReason: String
          _tmpReason = _stmt.getText(_columnIndexOfReason)
          _result =
              CooldownEntity(_tmpPhoneNumber,_tmpInitiatedAt,_tmpRetryAvailableTimestamp,_tmpFormattedRetryDate,_tmpReason)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun updateCustomerStatus(
    id: String,
    status: String,
    loanAppStatus: String,
  ) {
    val _sql: String = "UPDATE customers SET status = ?, loanAppStatus = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, status)
        _argIndex = 2
        _stmt.bindText(_argIndex, loanAppStatus)
        _argIndex = 3
        _stmt.bindText(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun updateCustomerBalances(
    id: String,
    outstandingBalance: Long,
    creditLimit: Long,
  ) {
    val _sql: String =
        "UPDATE customers SET outstandingLoanBalance = ?, currentCreditLimit = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, outstandingBalance)
        _argIndex = 2
        _stmt.bindLong(_argIndex, creditLimit)
        _argIndex = 3
        _stmt.bindText(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clearAll() {
    val _sql: String = "DELETE FROM customers"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clearCooldown(phoneNumber: String) {
    val _sql: String = "DELETE FROM application_cooldowns WHERE phoneNumber = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, phoneNumber)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
