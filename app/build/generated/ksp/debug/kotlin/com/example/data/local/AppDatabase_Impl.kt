package com.example.`data`.local

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class AppDatabase_Impl : AppDatabase() {
  private val _customerDao: Lazy<CustomerDao> = lazy {
    CustomerDao_Impl(this)
  }

  private val _userSessionDao: Lazy<UserSessionDao> = lazy {
    UserSessionDao_Impl(this)
  }

  private val _transactionDao: Lazy<TransactionDao> = lazy {
    TransactionDao_Impl(this)
  }

  private val _feedbackDao: Lazy<FeedbackDao> = lazy {
    FeedbackDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(2,
        "5eaabb415e4740a330979ec5c5548cc1", "61c1cdd8eba8fe398d16afadd196b8f6") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `customers` (`id` TEXT NOT NULL, `customerName` TEXT NOT NULL, `phoneNumber` TEXT NOT NULL, `referenceNumber` TEXT NOT NULL, `loanPlatform` TEXT NOT NULL, `platformLogoUrl` TEXT, `avatarUrl` TEXT, `requestType` TEXT NOT NULL, `status` TEXT NOT NULL, `loanAppStatus` TEXT NOT NULL, `currentCreditLimit` INTEGER NOT NULL, `requestedCreditLimit` INTEGER NOT NULL, `outstandingLoanBalance` INTEGER NOT NULL, `loanDuration` TEXT NOT NULL, `loanTenorMonths` INTEGER NOT NULL, `interestRate` REAL NOT NULL, `monthlyRepayment` INTEGER NOT NULL, `nextLoanEligibleAmount` INTEGER NOT NULL, `loanAppStatusReason` TEXT, `retryAvailableAt` TEXT, `loanDecisionAt` TEXT, `expiresAt` TEXT, `approvedAt` TEXT, `declinedAt` TEXT, `createdAt` TEXT NOT NULL, `updatedAt` TEXT NOT NULL, `shareToken` TEXT, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `application_cooldowns` (`phoneNumber` TEXT NOT NULL, `initiatedAt` INTEGER NOT NULL, `retryAvailableTimestamp` INTEGER NOT NULL, `formattedRetryDate` TEXT NOT NULL, `reason` TEXT NOT NULL, PRIMARY KEY(`phoneNumber`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `user_sessions` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `phoneNumber` TEXT NOT NULL, `rawPhoneNumber` TEXT NOT NULL, `email` TEXT NOT NULL, `isLoggedIn` INTEGER NOT NULL, `lastActiveTimestamp` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `credit_transactions` (`id` TEXT NOT NULL, `customerId` TEXT NOT NULL, `title` TEXT NOT NULL, `amount` INTEGER NOT NULL, `date` TEXT NOT NULL, `status` TEXT NOT NULL, `repaymentAmount` TEXT NOT NULL, `dueDate` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `user_feedbacks` (`id` TEXT NOT NULL, `customerId` TEXT NOT NULL, `issueType` TEXT NOT NULL, `description` TEXT NOT NULL, `submittedAt` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '5eaabb415e4740a330979ec5c5548cc1')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `customers`")
        connection.execSQL("DROP TABLE IF EXISTS `application_cooldowns`")
        connection.execSQL("DROP TABLE IF EXISTS `user_sessions`")
        connection.execSQL("DROP TABLE IF EXISTS `credit_transactions`")
        connection.execSQL("DROP TABLE IF EXISTS `user_feedbacks`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection):
          RoomOpenDelegate.ValidationResult {
        val _columnsCustomers: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsCustomers.put("id", TableInfo.Column("id", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCustomers.put("customerName", TableInfo.Column("customerName", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCustomers.put("phoneNumber", TableInfo.Column("phoneNumber", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCustomers.put("referenceNumber", TableInfo.Column("referenceNumber", "TEXT", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCustomers.put("loanPlatform", TableInfo.Column("loanPlatform", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCustomers.put("platformLogoUrl", TableInfo.Column("platformLogoUrl", "TEXT", false,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCustomers.put("avatarUrl", TableInfo.Column("avatarUrl", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCustomers.put("requestType", TableInfo.Column("requestType", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCustomers.put("status", TableInfo.Column("status", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCustomers.put("loanAppStatus", TableInfo.Column("loanAppStatus", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCustomers.put("currentCreditLimit", TableInfo.Column("currentCreditLimit",
            "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCustomers.put("requestedCreditLimit", TableInfo.Column("requestedCreditLimit",
            "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCustomers.put("outstandingLoanBalance", TableInfo.Column("outstandingLoanBalance",
            "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCustomers.put("loanDuration", TableInfo.Column("loanDuration", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCustomers.put("loanTenorMonths", TableInfo.Column("loanTenorMonths", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCustomers.put("interestRate", TableInfo.Column("interestRate", "REAL", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCustomers.put("monthlyRepayment", TableInfo.Column("monthlyRepayment", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCustomers.put("nextLoanEligibleAmount", TableInfo.Column("nextLoanEligibleAmount",
            "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCustomers.put("loanAppStatusReason", TableInfo.Column("loanAppStatusReason", "TEXT",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCustomers.put("retryAvailableAt", TableInfo.Column("retryAvailableAt", "TEXT",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCustomers.put("loanDecisionAt", TableInfo.Column("loanDecisionAt", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCustomers.put("expiresAt", TableInfo.Column("expiresAt", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCustomers.put("approvedAt", TableInfo.Column("approvedAt", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCustomers.put("declinedAt", TableInfo.Column("declinedAt", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCustomers.put("createdAt", TableInfo.Column("createdAt", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCustomers.put("updatedAt", TableInfo.Column("updatedAt", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCustomers.put("shareToken", TableInfo.Column("shareToken", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysCustomers: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesCustomers: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoCustomers: TableInfo = TableInfo("customers", _columnsCustomers,
            _foreignKeysCustomers, _indicesCustomers)
        val _existingCustomers: TableInfo = read(connection, "customers")
        if (!_infoCustomers.equals(_existingCustomers)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |customers(com.example.data.local.CustomerEntity).
              | Expected:
              |""".trimMargin() + _infoCustomers + """
              |
              | Found:
              |""".trimMargin() + _existingCustomers)
        }
        val _columnsApplicationCooldowns: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsApplicationCooldowns.put("phoneNumber", TableInfo.Column("phoneNumber", "TEXT",
            true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsApplicationCooldowns.put("initiatedAt", TableInfo.Column("initiatedAt", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsApplicationCooldowns.put("retryAvailableTimestamp",
            TableInfo.Column("retryAvailableTimestamp", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsApplicationCooldowns.put("formattedRetryDate",
            TableInfo.Column("formattedRetryDate", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsApplicationCooldowns.put("reason", TableInfo.Column("reason", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysApplicationCooldowns: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesApplicationCooldowns: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoApplicationCooldowns: TableInfo = TableInfo("application_cooldowns",
            _columnsApplicationCooldowns, _foreignKeysApplicationCooldowns,
            _indicesApplicationCooldowns)
        val _existingApplicationCooldowns: TableInfo = read(connection, "application_cooldowns")
        if (!_infoApplicationCooldowns.equals(_existingApplicationCooldowns)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |application_cooldowns(com.example.data.local.CooldownEntity).
              | Expected:
              |""".trimMargin() + _infoApplicationCooldowns + """
              |
              | Found:
              |""".trimMargin() + _existingApplicationCooldowns)
        }
        val _columnsUserSessions: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsUserSessions.put("id", TableInfo.Column("id", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUserSessions.put("name", TableInfo.Column("name", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUserSessions.put("phoneNumber", TableInfo.Column("phoneNumber", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserSessions.put("rawPhoneNumber", TableInfo.Column("rawPhoneNumber", "TEXT", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserSessions.put("email", TableInfo.Column("email", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUserSessions.put("isLoggedIn", TableInfo.Column("isLoggedIn", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserSessions.put("lastActiveTimestamp", TableInfo.Column("lastActiveTimestamp",
            "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysUserSessions: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesUserSessions: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoUserSessions: TableInfo = TableInfo("user_sessions", _columnsUserSessions,
            _foreignKeysUserSessions, _indicesUserSessions)
        val _existingUserSessions: TableInfo = read(connection, "user_sessions")
        if (!_infoUserSessions.equals(_existingUserSessions)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |user_sessions(com.example.data.local.UserSessionEntity).
              | Expected:
              |""".trimMargin() + _infoUserSessions + """
              |
              | Found:
              |""".trimMargin() + _existingUserSessions)
        }
        val _columnsCreditTransactions: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsCreditTransactions.put("id", TableInfo.Column("id", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCreditTransactions.put("customerId", TableInfo.Column("customerId", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCreditTransactions.put("title", TableInfo.Column("title", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCreditTransactions.put("amount", TableInfo.Column("amount", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCreditTransactions.put("date", TableInfo.Column("date", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCreditTransactions.put("status", TableInfo.Column("status", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCreditTransactions.put("repaymentAmount", TableInfo.Column("repaymentAmount",
            "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCreditTransactions.put("dueDate", TableInfo.Column("dueDate", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCreditTransactions.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysCreditTransactions: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesCreditTransactions: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoCreditTransactions: TableInfo = TableInfo("credit_transactions",
            _columnsCreditTransactions, _foreignKeysCreditTransactions, _indicesCreditTransactions)
        val _existingCreditTransactions: TableInfo = read(connection, "credit_transactions")
        if (!_infoCreditTransactions.equals(_existingCreditTransactions)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |credit_transactions(com.example.data.local.TransactionEntity).
              | Expected:
              |""".trimMargin() + _infoCreditTransactions + """
              |
              | Found:
              |""".trimMargin() + _existingCreditTransactions)
        }
        val _columnsUserFeedbacks: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsUserFeedbacks.put("id", TableInfo.Column("id", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUserFeedbacks.put("customerId", TableInfo.Column("customerId", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserFeedbacks.put("issueType", TableInfo.Column("issueType", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUserFeedbacks.put("description", TableInfo.Column("description", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserFeedbacks.put("submittedAt", TableInfo.Column("submittedAt", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysUserFeedbacks: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesUserFeedbacks: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoUserFeedbacks: TableInfo = TableInfo("user_feedbacks", _columnsUserFeedbacks,
            _foreignKeysUserFeedbacks, _indicesUserFeedbacks)
        val _existingUserFeedbacks: TableInfo = read(connection, "user_feedbacks")
        if (!_infoUserFeedbacks.equals(_existingUserFeedbacks)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |user_feedbacks(com.example.data.local.FeedbackEntity).
              | Expected:
              |""".trimMargin() + _infoUserFeedbacks + """
              |
              | Found:
              |""".trimMargin() + _existingUserFeedbacks)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "customers",
        "application_cooldowns", "user_sessions", "credit_transactions", "user_feedbacks")
  }

  public override fun clearAllTables() {
    super.performClear(false, "customers", "application_cooldowns", "user_sessions",
        "credit_transactions", "user_feedbacks")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(CustomerDao::class, CustomerDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(UserSessionDao::class, UserSessionDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(TransactionDao::class, TransactionDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(FeedbackDao::class, FeedbackDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override
      fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>):
      List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun customerDao(): CustomerDao = _customerDao.value

  public override fun userSessionDao(): UserSessionDao = _userSessionDao.value

  public override fun transactionDao(): TransactionDao = _transactionDao.value

  public override fun feedbackDao(): FeedbackDao = _feedbackDao.value
}
