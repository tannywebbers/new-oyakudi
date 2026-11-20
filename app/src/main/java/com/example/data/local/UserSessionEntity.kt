package com.example.data.local

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "user_sessions")
data class UserSessionEntity(
    @PrimaryKey val id: String,
    val name: String,
    val phoneNumber: String,
    val rawPhoneNumber: String,
    val email: String,
    val isLoggedIn: Boolean,
    val lastActiveTimestamp: Long = System.currentTimeMillis()
) {
    fun toUserProfile(): UserProfile {
        return UserProfile(
            id = id,
            name = name,
            phoneNumber = phoneNumber,
            rawPhoneNumber = rawPhoneNumber,
            email = email,
            isLoggedIn = isLoggedIn
        )
    }

    companion object {
        fun fromUserProfile(user: UserProfile): UserSessionEntity {
            return UserSessionEntity(
                id = user.id,
                name = user.name,
                phoneNumber = user.phoneNumber,
                rawPhoneNumber = user.rawPhoneNumber,
                email = user.email,
                isLoggedIn = user.isLoggedIn
            )
        }
    }
}

@Dao
interface UserSessionDao {

    @Query("SELECT * FROM user_sessions WHERE isLoggedIn = 1 ORDER BY lastActiveTimestamp DESC LIMIT 1")
    suspend fun getActiveSession(): UserSessionEntity?

    @Query("SELECT * FROM user_sessions WHERE isLoggedIn = 1 ORDER BY lastActiveTimestamp DESC LIMIT 1")
    fun observeActiveSession(): Flow<UserSessionEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSession(session: UserSessionEntity)

    @Query("UPDATE user_sessions SET isLoggedIn = 0")
    suspend fun clearActiveSessions()

    @Query("DELETE FROM user_sessions")
    suspend fun deleteAllSessions()
}
