package com.example.`data`.local

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Boolean
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
public class UserSessionDao_Impl(
  __db: RoomDatabase,
) : UserSessionDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfUserSessionEntity: EntityInsertAdapter<UserSessionEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfUserSessionEntity = object : EntityInsertAdapter<UserSessionEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `user_sessions` (`id`,`name`,`phoneNumber`,`rawPhoneNumber`,`email`,`isLoggedIn`,`lastActiveTimestamp`) VALUES (?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: UserSessionEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.name)
        statement.bindText(3, entity.phoneNumber)
        statement.bindText(4, entity.rawPhoneNumber)
        statement.bindText(5, entity.email)
        val _tmp: Int = if (entity.isLoggedIn) 1 else 0
        statement.bindLong(6, _tmp.toLong())
        statement.bindLong(7, entity.lastActiveTimestamp)
      }
    }
  }

  public override suspend fun saveSession(session: UserSessionEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfUserSessionEntity.insert(_connection, session)
  }

  public override suspend fun getActiveSession(): UserSessionEntity? {
    val _sql: String =
        "SELECT * FROM user_sessions WHERE isLoggedIn = 1 ORDER BY lastActiveTimestamp DESC LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfPhoneNumber: Int = getColumnIndexOrThrow(_stmt, "phoneNumber")
        val _columnIndexOfRawPhoneNumber: Int = getColumnIndexOrThrow(_stmt, "rawPhoneNumber")
        val _columnIndexOfEmail: Int = getColumnIndexOrThrow(_stmt, "email")
        val _columnIndexOfIsLoggedIn: Int = getColumnIndexOrThrow(_stmt, "isLoggedIn")
        val _columnIndexOfLastActiveTimestamp: Int = getColumnIndexOrThrow(_stmt,
            "lastActiveTimestamp")
        val _result: UserSessionEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpPhoneNumber: String
          _tmpPhoneNumber = _stmt.getText(_columnIndexOfPhoneNumber)
          val _tmpRawPhoneNumber: String
          _tmpRawPhoneNumber = _stmt.getText(_columnIndexOfRawPhoneNumber)
          val _tmpEmail: String
          _tmpEmail = _stmt.getText(_columnIndexOfEmail)
          val _tmpIsLoggedIn: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsLoggedIn).toInt()
          _tmpIsLoggedIn = _tmp != 0
          val _tmpLastActiveTimestamp: Long
          _tmpLastActiveTimestamp = _stmt.getLong(_columnIndexOfLastActiveTimestamp)
          _result =
              UserSessionEntity(_tmpId,_tmpName,_tmpPhoneNumber,_tmpRawPhoneNumber,_tmpEmail,_tmpIsLoggedIn,_tmpLastActiveTimestamp)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeActiveSession(): Flow<UserSessionEntity?> {
    val _sql: String =
        "SELECT * FROM user_sessions WHERE isLoggedIn = 1 ORDER BY lastActiveTimestamp DESC LIMIT 1"
    return createFlow(__db, false, arrayOf("user_sessions")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfPhoneNumber: Int = getColumnIndexOrThrow(_stmt, "phoneNumber")
        val _columnIndexOfRawPhoneNumber: Int = getColumnIndexOrThrow(_stmt, "rawPhoneNumber")
        val _columnIndexOfEmail: Int = getColumnIndexOrThrow(_stmt, "email")
        val _columnIndexOfIsLoggedIn: Int = getColumnIndexOrThrow(_stmt, "isLoggedIn")
        val _columnIndexOfLastActiveTimestamp: Int = getColumnIndexOrThrow(_stmt,
            "lastActiveTimestamp")
        val _result: UserSessionEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpPhoneNumber: String
          _tmpPhoneNumber = _stmt.getText(_columnIndexOfPhoneNumber)
          val _tmpRawPhoneNumber: String
          _tmpRawPhoneNumber = _stmt.getText(_columnIndexOfRawPhoneNumber)
          val _tmpEmail: String
          _tmpEmail = _stmt.getText(_columnIndexOfEmail)
          val _tmpIsLoggedIn: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsLoggedIn).toInt()
          _tmpIsLoggedIn = _tmp != 0
          val _tmpLastActiveTimestamp: Long
          _tmpLastActiveTimestamp = _stmt.getLong(_columnIndexOfLastActiveTimestamp)
          _result =
              UserSessionEntity(_tmpId,_tmpName,_tmpPhoneNumber,_tmpRawPhoneNumber,_tmpEmail,_tmpIsLoggedIn,_tmpLastActiveTimestamp)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clearActiveSessions() {
    val _sql: String = "UPDATE user_sessions SET isLoggedIn = 0"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteAllSessions() {
    val _sql: String = "DELETE FROM user_sessions"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
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
