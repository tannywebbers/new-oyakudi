package com.example.`data`.local

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
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

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class FeedbackDao_Impl(
  __db: RoomDatabase,
) : FeedbackDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfFeedbackEntity: EntityInsertAdapter<FeedbackEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfFeedbackEntity = object : EntityInsertAdapter<FeedbackEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `user_feedbacks` (`id`,`customerId`,`issueType`,`description`,`submittedAt`) VALUES (?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: FeedbackEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.customerId)
        statement.bindText(3, entity.issueType)
        statement.bindText(4, entity.description)
        statement.bindLong(5, entity.submittedAt)
      }
    }
  }

  public override suspend fun insertFeedback(feedback: FeedbackEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfFeedbackEntity.insert(_connection, feedback)
  }

  public override suspend fun getFeedbacksForCustomer(customerId: String): List<FeedbackEntity> {
    val _sql: String = "SELECT * FROM user_feedbacks WHERE customerId = ? ORDER BY submittedAt DESC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, customerId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfCustomerId: Int = getColumnIndexOrThrow(_stmt, "customerId")
        val _columnIndexOfIssueType: Int = getColumnIndexOrThrow(_stmt, "issueType")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfSubmittedAt: Int = getColumnIndexOrThrow(_stmt, "submittedAt")
        val _result: MutableList<FeedbackEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: FeedbackEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpCustomerId: String
          _tmpCustomerId = _stmt.getText(_columnIndexOfCustomerId)
          val _tmpIssueType: String
          _tmpIssueType = _stmt.getText(_columnIndexOfIssueType)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpSubmittedAt: Long
          _tmpSubmittedAt = _stmt.getLong(_columnIndexOfSubmittedAt)
          _item =
              FeedbackEntity(_tmpId,_tmpCustomerId,_tmpIssueType,_tmpDescription,_tmpSubmittedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
