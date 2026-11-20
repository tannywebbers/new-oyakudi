package com.example.data.model

data class CompanyInfo(
    val name: String = "PixKudi",
    val version: String = "1.0.4",
    val description: String = "PixKudi is focused on providing simple and reliable financial services. We keep improving the product experience to make every operation clear, fast, and secure.",
    val email: String = "support@PixKudi.app",
    val phone: String = "+234 9136275122",
    val whatsapp: String = "+234 9136275122",
    val address: String = "Lagos, Nigeria"
)

data class FaqItem(
    val id: String,
    val question: String,
    val answer: String
)

data class PrivacySection(
    val title: String,
    val content: String
)
