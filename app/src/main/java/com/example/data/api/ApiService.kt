package com.example.data.api

import com.example.data.model.CompanyInfo
import com.example.data.model.CreditInfo
import com.example.data.model.CreditTransaction
import com.example.data.model.FeedbackItem
import com.example.data.model.LoanApplicationRequest
import com.example.data.model.LoanApplicationResult
import com.example.data.model.LoanDetail
import com.example.data.model.UserProfile
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Retrofit interface prepared for future external backend/database integration.
 */
interface ApiService {

    @POST("auth/request-otp")
    suspend fun requestOtp(@Body request: OtpRequest): Response<OtpResponse>

    @POST("auth/verify-otp")
    suspend fun verifyOtp(@Body request: VerifyOtpRequest): Response<AuthResponse>

    @GET("user/profile")
    suspend fun getUserProfile(): Response<UserProfile>

    @GET("user/credit")
    suspend fun getCreditInfo(): Response<CreditInfo>

    @GET("loans")
    suspend fun getLoans(): Response<List<LoanDetail>>

    @GET("loans/current")
    suspend fun getCurrentLoan(): Response<LoanDetail>

    @GET("loans/history")
    suspend fun getLoanHistory(): Response<List<CreditTransaction>>

    @POST("loans/apply")
    suspend fun applyLoan(@Body request: LoanApplicationRequest): Response<LoanApplicationResult>

    @POST("loans/withdraw")
    suspend fun withdrawLoan(@Body request: LoanApplicationRequest): Response<LoanApplicationResult>

    @POST("feedback")
    suspend fun submitFeedback(@Body feedback: FeedbackItem): Response<Map<String, String>>

    @GET("app/config")
    suspend fun getAppConfig(): Response<Map<String, Any>>

    @GET("contact")
    suspend fun getContactInfo(): Response<CompanyInfo>
}

data class OtpRequest(val phoneNumber: String)
data class OtpResponse(val success: Boolean, val message: String)
data class VerifyOtpRequest(val phoneNumber: String, val otp: String)
data class AuthResponse(val success: Boolean, val token: String, val user: UserProfile)
