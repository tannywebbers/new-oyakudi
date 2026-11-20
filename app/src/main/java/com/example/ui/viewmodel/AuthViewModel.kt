package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.RepositoryProvider
import com.example.data.model.AuthState
import com.example.data.model.UserProfile
import com.example.data.repository.UserRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class LoginUiState(
    val phoneNumber: String = "",
    val isAgreed: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

class AuthViewModel(
    private val userRepository: UserRepository = RepositoryProvider.userRepository,
    private val customerRepository: com.example.data.repository.CustomerRepository = RepositoryProvider.customerRepository
) : ViewModel() {

    val authState: StateFlow<AuthState> = userRepository.authState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AuthState(isAuthenticated = false, user = null)
        )

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onPhoneNumberChanged(newNumber: String) {
        // Digits only, up to 11 chars
        val filtered = newNumber.filter { it.isDigit() }.take(11)
        _uiState.value = _uiState.value.copy(
            phoneNumber = filtered,
            errorMessage = null
        )
    }

    fun onAgreeToggled(agreed: Boolean) {
        _uiState.value = _uiState.value.copy(
            isAgreed = agreed,
            errorMessage = null
        )
    }

    fun loginOrRegister(onSuccess: () -> Unit) {
        val state = _uiState.value
        if (state.phoneNumber.isBlank() || state.phoneNumber.length < 9) {
            _uiState.value = state.copy(errorMessage = "Please enter a valid phone number")
            return
        }
        if (!state.isAgreed) {
            _uiState.value = state.copy(errorMessage = "Please accept the Privacy Policy to continue")
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(isLoading = true, errorMessage = null)
            val digits = state.phoneNumber.filter { it.isDigit() }
            val fullPhone = "+234${digits.removePrefix("234").removePrefix("0")}"
            val result = userRepository.loginOrRegister(fullPhone)
            result.onSuccess {
                _uiState.value = _uiState.value.copy(isLoading = false)
                onSuccess()
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.localizedMessage ?: "Login failed"
                )
            }
        }
    }

    fun logout(onComplete: () -> Unit) {
        viewModelScope.launch {
            userRepository.logout()
            onComplete()
        }
    }

    fun deleteAccount(onComplete: () -> Unit) {
        viewModelScope.launch {
            userRepository.deleteAccount()
            onComplete()
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(errorMessage = null, successMessage = null)
    }

    fun updateDatabaseCredentials(context: android.content.Context, url: String, anonKey: String) {
        com.example.data.supabase.SupabaseConfig.updateCredentials(context, url, anonKey)
        com.example.data.supabase.SupabaseClient.initialize(url, anonKey)
        _uiState.value = _uiState.value.copy(
            errorMessage = null,
            successMessage = "Database credentials updated successfully!"
        )
    }
}
