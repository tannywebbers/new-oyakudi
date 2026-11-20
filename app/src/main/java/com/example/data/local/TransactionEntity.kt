package com.example.data.local

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import com.example.data.model.CreditTransaction
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "credit_transactions")
data class TransactionEntity(
    @PrimaryKey val id: String,
    val customerId: String,
    val title: String,
    val amount: Long,
    val date: String,
    val status: String,
    val repaymentAmount: String,
    val dueDate: String,
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toCreditTransaction(): CreditTransaction {
        return CreditTransaction(
            id = id,
            title = title,
            amount = amount,
            date = date,
            status = status,
            repaymentAmount = repaymentAmount,
            dueDate = dueDate
        )
    }

    companion object {
        fun fromCreditTransaction(tx: CreditTransaction, customerId: String): TransactionEntity {
            return TransactionEntity(
                id = tx.id,
                customerId = customerId,
                title = tx.title,
                amount = tx.amount,
                date = tx.date,
                status = tx.status,
                repaymentAmount = tx.repaymentAmount,
                dueDate = tx.dueDate
            )
        }
    }
}

@Dao
interface TransactionDao {

    @Query("SELECT * FROM credit_transactions WHERE customerId = :customerId ORDER BY createdAt DESC")
    suspend fun getTransactionsForCustomer(customerId: String): List<TransactionEntity>

    @Query("SELECT * FROM credit_transactions WHERE customerId = :customerId ORDER BY createdAt DESC")
    fun observeTransactionsForCustomer(customerId: String): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Query("DELETE FROM credit_transactions WHERE customerId = :customerId")
    suspend fun clearTransactionsForCustomer(customerId: String)
}
