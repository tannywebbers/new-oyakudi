package com.example.data.model

data class UserProfile(
    val id: String = "user_001",
    val name: String = "PixKudi",
    val phoneNumber: String = "+814***2512",
    val rawPhoneNumber: String = "+2349136275122",
    val email: String = "PixKudiService@gmail.com",
    val isLoggedIn: Boolean = true,
    val avatarUrl: String? = null
)

data class AuthState(
    val isAuthenticated: Boolean = true,
    val user: UserProfile? = UserProfile(),
    val otpSent: Boolean = false,
    val otpCode: String = "1234"
)
