package com.example.data.repository

import android.util.Log
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response

/**
 * Exact JSON body expected by the open Supabase "payment-webhook" Edge Function.
 * Field names must not change; `amount` is a number (not a string).
 */
data class RepaymentWebhookPayload(
    @Json(name = "event") val event: String,
    @Json(name = "customerName") val customerName: String,
    @Json(name = "amount") val amount: Long,
    @Json(name = "customerId") val customerId: String,
    @Json(name = "loanId") val loanId: String,
    @Json(name = "bank") val bank: String,
    @Json(name = "accountNumber") val accountNumber: String,
    @Json(name = "accountHolder") val accountHolder: String,
    @Json(name = "timestamp") val timestamp: String
)

interface RepaymentRepository {
    /**
     * Notifies the backend that the customer marked their repayment as paid.
     * Returns success only for a 2xx response, and never throws.
     */
    suspend fun markRepaymentPaid(payload: RepaymentWebhookPayload): Result<Boolean>
}

class RepaymentRepositoryImpl(
    private val client: OkHttpClient = defaultClient(),
    private val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
) : RepaymentRepository {

    private val adapter = moshi.adapter(RepaymentWebhookPayload::class.java)

    override suspend fun markRepaymentPaid(payload: RepaymentWebhookPayload): Result<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                val json = adapter.toJson(payload)
                val body = json.toRequestBody(JSON_MEDIA_TYPE)
                val request = Request.Builder()
                    .url(WEBHOOK_URL)
                    .header("Content-Type", "application/json")
                    .post(body)
                    .build()

                val call = client.newCall(request)
                suspendCancellableCoroutine { continuation ->
                    continuation.invokeOnCancellation { call.cancel() }
                    call.enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            Log.e(TAG, "Repayment webhook error", e)
                            if (continuation.isActive) {
                                continuation.resume(Result.failure(e))
                            }
                        }

                        override fun onResponse(call: Call, response: Response) {
                            response.use { received ->
                                if (received.isSuccessful) {
                                    Log.d(TAG, "Repayment webhook delivered (HTTP ${received.code})")
                                    if (continuation.isActive) {
                                        continuation.resume(Result.success(true))
                                    }
                                } else {
                                    Log.e(TAG, "Repayment webhook failed: HTTP ${received.code}")
                                    if (continuation.isActive) {
                                        continuation.resume(
                                            Result.failure(Exception("Server returned HTTP ${received.code}"))
                                        )
                                    }
                                }
                            }
                        }
                    })
                }
            } catch (e: Exception) {
                Log.e(TAG, "Repayment webhook error", e)
                Result.failure(e)
            }
        }

    companion object {
        private const val TAG = "RepaymentRepository"

        /** Open Edge Function — intentionally no auth header is sent. */
        const val WEBHOOK_URL =
            "https://gcglkblilfsonhufigvr.supabase.co/functions/v1/payment-webhook"

        private val JSON_MEDIA_TYPE = "application/json; charset=utf-8".toMediaType()

        private fun defaultClient(): OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()
    }
}
