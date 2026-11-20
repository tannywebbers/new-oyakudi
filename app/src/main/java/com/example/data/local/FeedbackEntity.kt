package com.example.data.local

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import com.example.data.model.FeedbackItem
import com.example.data.model.IssueType

@Entity(tableName = "user_feedbacks")
data class FeedbackEntity(
    @PrimaryKey val id: String,
    val customerId: String,
    val issueType: String,
    val description: String,
    val submittedAt: Long = System.currentTimeMillis()
) {
    fun toFeedbackItem(): FeedbackItem {
        val type = IssueType.entries.find { it.name == issueType } ?: IssueType.SUGGESTIONS_OTHERS
        return FeedbackItem(
            id = id,
            issueType = type,
            description = description
        )
    }
}

@Dao
interface FeedbackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeedback(feedback: FeedbackEntity)

    @Query("SELECT * FROM user_feedbacks WHERE customerId = :customerId ORDER BY submittedAt DESC")
    suspend fun getFeedbacksForCustomer(customerId: String): List<FeedbackEntity>
}
