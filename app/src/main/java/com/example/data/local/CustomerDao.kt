package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {

    @Query("SELECT * FROM customers LIMIT 1")
    fun observeCurrentCustomer(): Flow<CustomerEntity?>

    @Query("SELECT * FROM customers WHERE phoneNumber LIKE '%' || :lastDigits LIMIT 1")
    suspend fun getCustomerByPhoneSuffix(lastDigits: String): CustomerEntity?

    @Query("SELECT * FROM customers WHERE id = :id LIMIT 1")
    suspend fun getCustomerById(id: String): CustomerEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: CustomerEntity)

    @Query("UPDATE customers SET status = :status, loanAppStatus = :loanAppStatus WHERE id = :id")
    suspend fun updateCustomerStatus(id: String, status: String, loanAppStatus: String)

    @Query("UPDATE customers SET outstandingLoanBalance = :outstandingBalance, currentCreditLimit = :creditLimit WHERE id = :id")
    suspend fun updateCustomerBalances(id: String, outstandingBalance: Long, creditLimit: Long)

    @Query("DELETE FROM customers")
    suspend fun clearAll()

    // 14-day Cooldowns queries
    @Query("SELECT * FROM application_cooldowns WHERE phoneNumber = :phoneNumber LIMIT 1")
    suspend fun getCooldown(phoneNumber: String): CooldownEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCooldown(cooldown: CooldownEntity)

    @Query("DELETE FROM application_cooldowns WHERE phoneNumber = :phoneNumber")
    suspend fun clearCooldown(phoneNumber: String)
}
