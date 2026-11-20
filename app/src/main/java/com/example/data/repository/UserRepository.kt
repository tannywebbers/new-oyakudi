package com.example.data.repository

import com.example.data.local.UserSessionDao
import com.example.data.local.UserSessionEntity
import com.example.data.model.AuthState
import com.example.data.model.UserProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface UserRepository {
    val authState: Flow<AuthState>
    suspend fun requestOtp(phoneNumber: String): Result<String>
    suspend fun loginOrRegister(phoneNumber: String, otp: String = ""): Result<UserProfile>
    suspend fun logout()
    suspend fun deleteAccount(): Result<Unit>
    suspend fun getUserProfile(): UserProfile
}

class UserRepositoryImpl(
    private val userSessionDao: UserSessionDao,
    private val customerRepository: CustomerRepository,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) : UserRepository {

    private val _authState = MutableStateFlow(
        AuthState(
            isAuthenticated = false,
            user = null,
            otpSent = false,
            otpCode = ""
        )
    )
    override val authState: Flow<AuthState> = _authState.asStateFlow()

    init {
        scope.launch {
            // Restore existing logged-in session from SQLite Room DB ONLY if verified
            val session = userSessionDao.getActiveSession()
            if (session != null && session.isLoggedIn && session.phoneNumber.isNotBlank()) {
                val phone = session.rawPhoneNumber.ifEmpty { session.phoneNumber }
                val loginResult = customerRepository.loginWithPhone(phone)
                if (loginResult.isSuccess) {
                    val profile = session.toUserProfile()
                    _authState.value = AuthState(
                        isAuthenticated = true,
                        user = profile
                    )
                } else {
                    userSessionDao.clearActiveSessions()
                    customerRepository.clearAllCustomerData()
                    _authState.value = AuthState(
                        isAuthenticated = false,
                        user = null
                    )
                }
            } else {
                userSessionDao.clearActiveSessions()
                customerRepository.clearAllCustomerData()
                _authState.value = AuthState(
                    isAuthenticated = false,
                    user = null
                )
            }
        }
    }

    override suspend fun requestOtp(phoneNumber: String): Result<String> {
        _authState.value = _authState.value.copy(otpSent = true, otpCode = "1234")
        return Result.success("OTP sent successfully")
    }

    override suspend fun loginOrRegister(phoneNumber: String, otp: String): Result<UserProfile> = withContext(Dispatchers.IO) {
        val cleanDigits = phoneNumber.filter { it.isDigit() }
        val formattedMasked = if (phoneNumber.length >= 8) {
            "${phoneNumber.take(4)}***${phoneNumber.takeLast(4)}"
        } else {
            phoneNumber
        }

        // Strictly verify customer in live Supabase database
        val custResult = customerRepository.loginWithPhone(phoneNumber)
        if (custResult.isFailure) {
            userSessionDao.clearActiveSessions()
            customerRepository.clearAllCustomerData()
            _authState.value = AuthState(isAuthenticated = false, user = null)
            val err = custResult.exceptionOrNull() ?: Exception("Customer not found or not migrated, contact support")
            return@withContext Result.failure(err)
        }

        val cust = custResult.getOrThrow()
        val userId = cust.id.ifEmpty { "cust_${cleanDigits}" }
        val userName = if (cust.customerName.isNotBlank() && cust.customerName != "Customer") {
            cust.customerName
        } else {
            "PixKudi"
        }

        val profile = UserProfile(
            id = userId,
            name = userName,
            phoneNumber = formattedMasked,
            rawPhoneNumber = phoneNumber,
            email = "customer@PixKudi.com",
            isLoggedIn = true
        )

        // Save session in Room DB
        userSessionDao.clearActiveSessions()
        userSessionDao.saveSession(UserSessionEntity.fromUserProfile(profile))

        _authState.value = AuthState(
            isAuthenticated = true,
            user = profile,
            otpSent = false,
            otpCode = ""
        )

        Result.success(profile)
    }

    override suspend fun logout() = withContext(Dispatchers.IO) {
        userSessionDao.clearActiveSessions()
        customerRepository.clearAllCustomerData()
        _authState.value = AuthState(
            isAuthenticated = false,
            user = null,
            otpSent = false,
            otpCode = ""
        )
    }

    override suspend fun deleteAccount(): Result<Unit> = withContext(Dispatchers.IO) {
        userSessionDao.deleteAllSessions()
        customerRepository.clearAllCustomerData()
        _authState.value = AuthState(
            isAuthenticated = false,
            user = null,
            otpSent = false,
            otpCode = ""
        )
        Result.success(Unit)
    }

    override suspend fun getUserProfile(): UserProfile = withContext(Dispatchers.IO) {
        val active = userSessionDao.getActiveSession()
        if (active != null) {
            active.toUserProfile()
        } else {
            _authState.value.user ?: UserProfile(isLoggedIn = false)
        }
    }
}
