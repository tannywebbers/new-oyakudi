package com.example.`data`.local

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class TransactionDao_Impl(
  __db: RoomDatabase,
) : TransactionDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfTransactionEntity: EntityInsertAdapter<TransactionEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfTransactionEntity = object : EntityInsertAdapter<TransactionEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `credit_transactions` (`id`,`customerId`,`title`,`amount`,`date`,`status`,`repaymentAmount`,`dueDate`,`createdAt`) VALUES (?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: TransactionEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.customerId)
        statement.bindText(3, entity.title)
        statement.bindLong(4, entity.amount)
        statement.bindText(5, entity.date)
        statement.bindText(6, entity.status)
        statement.bindText(7, entity.repaymentAmount)
        statement.bindText(8, entity.dueDate)
        statement.bindLong(9, entity.createdAt)
      }
    }
  }

  public override suspend fun insertTransaction(transaction: TransactionEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfTransactionEntity.insert(_connection, transaction)
  }

  public override suspend fun getTransactionsForCustomer(customerId: String):
      List<TransactionEntity> {
    val _sql: String =
        "SELECT * FROM credit_transactions WHERE customerId = ? ORDER BY createdAt DESC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, customerId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfCustomerId: Int = getColumnIndexOrThrow(_stmt, "customerId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfAmount: Int = getColumnIndexOrThrow(_stmt, "amount")
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfRepaymentAmount: Int = getColumnIndexOrThrow(_stmt, "repaymentAmount")
        val _columnIndexOfDueDate: Int = getColumnIndexOrThrow(_stmt, "dueDate")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: MutableList<TransactionEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TransactionEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpCustomerId: String
          _tmpCustomerId = _stmt.getText(_columnIndexOfCustomerId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpAmount: Long
          _tmpAmount = _stmt.getLong(_columnIndexOfAmount)
          val _tmpDate: String
          _tmpDate = _stmt.getText(_columnIndexOfDate)
          val _tmpStatus: String
          _tmpStatus = _stmt.getText(_columnIndexOfStatus)
          val _tmpRepaymentAmount: String
          _tmpRepaymentAmount = _stmt.getText(_columnIndexOfRepaymentAmount)
          val _tmpDueDate: String
          _tmpDueDate = _stmt.getText(_columnIndexOfDueDate)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          _item =
              TransactionEntity(_tmpId,_tmpCustomerId,_tmpTitle,_tmpAmount,_tmpDate,_tmpStatus,_tmpRepaymentAmount,_tmpDueDate,_tmpCreatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeTransactionsForCustomer(customerId: String):
      Flow<List<TransactionEntity>> {
    val _sql: String =
        "SELECT * FROM credit_transactions WHERE customerId = ? ORDER BY createdAt DESC"
    return createFlow(__db, false, arrayOf("credit_transactions")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, customerId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfCustomerId: Int = getColumnIndexOrThrow(_stmt, "customerId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfAmount: Int = getColumnIndexOrThrow(_stmt, "amount")
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfRepaymentAmount: Int = getColumnIndexOrThrow(_stmt, "repaymentAmount")
        val _columnIndexOfDueDate: Int = getColumnIndexOrThrow(_stmt, "dueDate")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: MutableList<TransactionEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TransactionEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpCustomerId: String
          _tmpCustomerId = _stmt.getText(_columnIndexOfCustomerId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpAmount: Long
          _tmpAmount = _stmt.getLong(_columnIndexOfAmount)
          val _tmpDate: String
          _tmpDate = _stmt.getText(_columnIndexOfDate)
          val _tmpStatus: String
          _tmpStatus = _stmt.getText(_columnIndexOfStatus)
          val _tmpRepaymentAmount: String
          _tmpRepaymentAmount = _stmt.getText(_columnIndexOfRepaymentAmount)
          val _tmpDueDate: String
          _tmpDueDate = _stmt.getText(_columnIndexOfDueDate)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          _item =
              TransactionEntity(_tmpId,_tmpCustomerId,_tmpTitle,_tmpAmount,_tmpDate,_tmpStatus,_tmpRepaymentAmount,_tmpDueDate,_tmpCreatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clearTransactionsForCustomer(customerId: String) {
    val _sql: String = "DELETE FROM credit_transactions WHERE customerId = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, customerId)
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
