package com.example.data.supabase

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Dedicated SupabaseClient object to initialize and communicate with Supabase REST endpoints.
 */
object SupabaseClient {

    private var api: SupabasePostgrestApi? = null

    /**
     * Initializes or re-initializes the Supabase client with the given project URL and public anon key.
     */
    fun initialize(url: String = SupabaseConfig.supabaseUrl, key: String = SupabaseConfig.supabaseAnonKey) {
        val sanitizedUrl = if (url.endsWith("/")) url else "$url/"
        
        val authInterceptor = Interceptor { chain ->
            val original: Request = chain.request()
            val request = original.newBuilder()
                .header("apikey", key)
                .header("Authorization", "Bearer $key")
                .header("Content-Type", "application/json")
                .build()
            chain.proceed(request)
        }

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(sanitizedUrl)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        api = retrofit.create(SupabasePostgrestApi::class.java)
    }

    private fun getApi(): SupabasePostgrestApi {
        if (api == null) {
            initialize()
        }
        return api ?: throw IllegalStateException("SupabaseClient is not initialized.")
    }

    /**
     * Queries the 'customers' table directly, filtering by 'phone_number'.
     */
    suspend fun getCustomerByPhoneNumber(phoneNumber: String): Result<SupabaseCustomerDto> = withContext(Dispatchers.IO) {
        try {
            val apiInstance = getApi()
            val cleanDigits = phoneNumber.filter { it.isDigit() }
            val suffix = if (cleanDigits.length >= 7) cleanDigits.takeLast(7) else cleanDigits
            val formatted = if (phoneNumber.startsWith("+")) phoneNumber.trim() else "+234${cleanDigits.removePrefix("234").removePrefix("0")}"

            // 1. Direct eq query on phone_number
            val exactResponse = apiInstance.getCustomers(phoneNumberFilter = "eq.$formatted")
            if (exactResponse.isSuccessful && !exactResponse.body().isNullOrEmpty()) {
                return@withContext Result.success(exactResponse.body()!!.first())
            }

            // 2. Query with raw digits
            val rawResponse = apiInstance.getCustomers(phoneNumberFilter = "eq.$cleanDigits")
            if (rawResponse.isSuccessful && !rawResponse.body().isNullOrEmpty()) {
                return@withContext Result.success(rawResponse.body()!!.first())
            }

            // 3. Fallback: fetch all and match phone number suffix
            val allResponse = apiInstance.getCustomers()
            if (allResponse.isSuccessful) {
                val list = allResponse.body().orEmpty()
                val found = list.find { dto ->
                    val dtoDigits = dto.phoneNumber?.filter { it.isDigit() } ?: ""
                    dtoDigits.isNotBlank() && (
                        dtoDigits == cleanDigits ||
                        dtoDigits.endsWith(suffix) ||
                        cleanDigits.endsWith(dtoDigits.takeLast(7)) ||
                        dto.phoneNumber?.trim() == formatted
                    )
                }
                if (found != null) {
                    return@withContext Result.success(found)
                } else {
                    return@withContext Result.failure(Exception("Customer not found or not migrated, contact support"))
                }
            } else {
                return@withContext Result.failure(Exception("Supabase query failed: HTTP ${allResponse.code()} ${allResponse.message()}"))
            }
        } catch (e: Exception) {
            val message = when {
                e is java.net.UnknownHostException -> "Unable to resolve database host (${e.message}). Please verify your Supabase Project URL in settings."
                e is java.net.ConnectException -> "Failed to connect to database server. Please check your internet connection."
                e is java.net.SocketTimeoutException -> "Database connection timed out. Please retry."
                else -> e.localizedMessage ?: (e.message ?: "Database error occurred")
            }
            return@withContext Result.failure(Exception(message, e))
        }
    }

    /**
     * Updates customer balances in the 'customers' table.
     */
    suspend fun updateBalances(customerId: String, outstanding: Long, creditLimit: Long): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val apiInstance = getApi()
            val response = apiInstance.updateCustomerBalances(
                idFilter = "eq.$customerId",
                updateBody = mapOf(
                    "outstanding_loan_balance" to outstanding,
                    "current_credit_limit" to creditLimit
                )
            )
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to update database: HTTP ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun reset() {
        api = null
    }
}
