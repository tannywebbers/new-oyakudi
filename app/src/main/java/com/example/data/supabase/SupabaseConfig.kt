package com.example.data.supabase

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.CustomerCase
import com.squareup.moshi.Json
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
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

/**
 * Stores and manages Supabase connection credentials dynamically.
 */
object SupabaseConfig {
    private const val PREFS_NAME = "sapa_supabase_prefs"
    private const val KEY_URL = "supabase_url"
    private const val KEY_ANON_KEY = "supabase_anon_key"

    private const val DEFAULT_URL = "https://gcglkblilfsonhufigvr.supabase.co"
    private const val DEFAULT_ANON_KEY = "sb_publishable_rD2CVlH7gCNzxXmRPCLFzw_y_IOB7yr"

    // Live Supabase project credentials
    var supabaseUrl: String = DEFAULT_URL
        private set
    var supabaseAnonKey: String = DEFAULT_ANON_KEY
        private set

    fun init(context: Context) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val storedUrl = prefs.getString(KEY_URL, null)
        val storedKey = prefs.getString(KEY_ANON_KEY, null)

        if (!storedUrl.isNullOrBlank() && 
            !storedUrl.contains("your-project-ref") && 
            !storedUrl.contains("yltisudvcdasawlnudzp") && 
            storedUrl.startsWith("http")) {
            supabaseUrl = storedUrl.trim().removeSuffix("/")
        } else {
            supabaseUrl = DEFAULT_URL
            prefs.edit().putString(KEY_URL, DEFAULT_URL).apply()
        }

        if (!storedKey.isNullOrBlank() && 
            !storedKey.contains("your-anon-key") && 
            !storedKey.contains("_0BHyU7hB7XWqjyqCasGpA") && 
            storedKey.length > 20) {
            supabaseAnonKey = storedKey.trim()
        } else {
            supabaseAnonKey = DEFAULT_ANON_KEY
            prefs.edit().putString(KEY_ANON_KEY, DEFAULT_ANON_KEY).apply()
        }
    }

    fun updateCredentials(context: Context, url: String, anonKey: String) {
        val sanitizedUrl = url.trim().removeSuffix("/")
        val sanitizedKey = anonKey.trim()
        supabaseUrl = sanitizedUrl
        supabaseAnonKey = sanitizedKey

        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_URL, sanitizedUrl)
            .putString(KEY_ANON_KEY, sanitizedKey)
            .apply()

        // Reset and reinitialize clients
        SupabaseClientProvider.reset()
        SupabaseClient.initialize(sanitizedUrl, sanitizedKey)
    }

    val isConfigured: Boolean
        get() = supabaseUrl.startsWith("http") && !supabaseUrl.contains("your-project-ref") && supabaseAnonKey.length > 20
}

/**
 * Data transfer model directly matching PostgreSQL public.customers table schema
 */
data class SupabaseCustomerDto(
    @Json(name = "id") val id: String? = null,
    @Json(name = "owner_id") val ownerId: String? = null,
    @Json(name = "customer_name") val customerName: String? = "Customer",
    @Json(name = "phone_number") val phoneNumber: String? = null,
    @Json(name = "reference_id") val referenceId: String? = null,
    @Json(name = "reference_number") val referenceNumber: String? = null,
    @Json(name = "loan_platform") val loanPlatform: String? = "PixKudi",
    @Json(name = "platform_logo_url") val platformLogoUrl: String? = null,
    @Json(name = "avatar_url") val avatarUrl: String? = null,
    @Json(name = "current_credit_limit") val currentCreditLimit: Double? = 0.0,
    @Json(name = "requested_credit_limit") val requestedCreditLimit: Double? = 0.0,
    @Json(name = "outstanding_loan_balance") val outstandingLoanBalance: Double? = 0.0,
    @Json(name = "loan_duration") val loanDuration: String? = "180",
    @Json(name = "collector_notes") val collectorNotes: String? = null,
    @Json(name = "internal_remarks") val internalRemarks: String? = null,
    @Json(name = "collector_name") val collectorName: String? = null,
    @Json(name = "request_type") val requestType: String? = "upgrade",
    @Json(name = "status") val status: String? = "pending", // 'pending','processing','approved','declined'
    @Json(name = "loan_app_status") val loanAppStatus: String? = "pending", // 'approved','pending','failed','disbursement_suspended'
    @Json(name = "loan_app_status_reason") val loanAppStatusReason: String? = null,
    @Json(name = "retry_available_at") val retryAvailableAt: String? = null,
    @Json(name = "interest_rate") val interestRate: Double? = 0.08,
    @Json(name = "monthly_repayment") val monthlyRepayment: Double? = 0.0,
    @Json(name = "loan_tenor_months") val loanTenorMonths: Int? = 6,
    @Json(name = "next_loan_eligible_amount") val nextLoanEligibleAmount: Double? = 0.0,
    @Json(name = "share_token") val shareToken: String? = null,
    @Json(name = "created_at") val createdAt: String? = null,
    @Json(name = "updated_at") val updatedAt: String? = null
) {
    fun toDomain(): CustomerCase {
        val reqLimit = (requestedCreditLimit ?: currentCreditLimit ?: 0.0).toLong()
        val currLimit = (currentCreditLimit ?: requestedCreditLimit ?: 0.0).toLong()
        return CustomerCase(
            id = id ?: "cust_${System.currentTimeMillis()}",
            customerName = customerName ?: "Customer",
            phoneNumber = phoneNumber ?: "",
            referenceNumber = referenceNumber ?: (referenceId ?: "REF-SAPA"),
            loanPlatform = loanPlatform ?: "PixKudi",
            status = (status ?: "pending").lowercase(),
            loanAppStatus = (loanAppStatus ?: status ?: "pending").lowercase(),
            currentCreditLimit = currLimit,
            requestedCreditLimit = reqLimit,
            outstandingLoanBalance = (outstandingLoanBalance ?: 0.0).toLong(),
            loanDuration = loanDuration ?: "180",
            loanTenorMonths = loanTenorMonths ?: 6,
            interestRate = interestRate ?: 0.08,
            monthlyRepayment = (monthlyRepayment ?: 0.0).toLong(),
            nextLoanEligibleAmount = (nextLoanEligibleAmount ?: reqLimit.toDouble()).toLong(),
            loanAppStatusReason = loanAppStatusReason,
            retryAvailableAt = retryAvailableAt
        )
    }
}

/**
 * Data transfer model matching the public.brand_settings table columns used for
 * the announcement popup modal and the news ticker.
 */
data class BrandSettingsDto(
    @Json(name = "news_ticker_enabled") val newsTickerEnabled: Boolean? = false,
    @Json(name = "news_ticker_text") val newsTickerText: String? = null,
    @Json(name = "popup_modal_enabled") val popupModalEnabled: Boolean? = false,
    @Json(name = "popup_modal_html") val popupModalHtml: String? = null
)

/**
 * Retrofit interface targeting Supabase PostgREST endpoints
 */
interface SupabasePostgrestApi {

    @GET("rest/v1/brand_settings")
    suspend fun getBrandSettings(
        @Query("select") select: String = "news_ticker_enabled,news_ticker_text,popup_modal_enabled,popup_modal_html",
        @Query("limit") limit: Int = 1
    ): Response<List<BrandSettingsDto>>

    @GET("rest/v1/customers")
    suspend fun getCustomers(
        @Query("phone_number") phoneNumberFilter: String? = null,
        @Query("or") orFilter: String? = null,
        @Query("select") select: String = "*"
    ): Response<List<SupabaseCustomerDto>>

    @GET
    suspend fun getCustomersByUrl(
        @retrofit2.http.Url url: String
    ): Response<List<SupabaseCustomerDto>>

    @POST("rest/v1/customers")
    @Headers("Prefer: return=representation")
    suspend fun createCustomer(
        @Body customer: SupabaseCustomerDto
    ): Response<List<SupabaseCustomerDto>>

    @PATCH("rest/v1/customers")
    @Headers("Prefer: return=representation")
    suspend fun updateCustomerBalances(
        @Query("id") idFilter: String,
        @Body updateBody: Map<String, @JvmSuppressWildcards Any>
    ): Response<List<SupabaseCustomerDto>>

    @PATCH("rest/v1/customers")
    @Headers("Prefer: return=representation")
    suspend fun updateCustomerByPhone(
        @Query("phone_number") phoneFilter: String,
        @Body updateBody: Map<String, @JvmSuppressWildcards Any>
    ): Response<List<SupabaseCustomerDto>>
}

/**
 * Provider for Supabase Postgrest API instance
 */
object SupabaseClientProvider {
    private var api: SupabasePostgrestApi? = null

    fun getApi(): SupabasePostgrestApi? {
        if (!SupabaseConfig.isConfigured) return null
        if (api != null) return api

        val baseUrl = if (SupabaseConfig.supabaseUrl.endsWith("/")) SupabaseConfig.supabaseUrl else "${SupabaseConfig.supabaseUrl}/"

        val authInterceptor = Interceptor { chain ->
            val original: Request = chain.request()
            val request = original.newBuilder()
                .header("apikey", SupabaseConfig.supabaseAnonKey)
                .header("Authorization", "Bearer ${SupabaseConfig.supabaseAnonKey}")
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
            .build()

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        api = retrofit.create(SupabasePostgrestApi::class.java)
        return api
    }

    fun reset() {
        api = null
    }
}
