package com.example.data.repository

import com.example.data.model.CompanyInfo
import com.example.data.model.FaqItem
import com.example.data.model.PrivacySection

interface AppConfigRepository {
    fun getCompanyInfo(): CompanyInfo
    fun getFaqs(): List<FaqItem>
    fun getPrivacyPolicy(): List<PrivacySection>
}

class AppConfigRepositoryImpl : AppConfigRepository {

    override fun getCompanyInfo(): CompanyInfo {
        return CompanyInfo(
            name = "PixKudi",
            version = "1.0.4",
            description = "PixKudi is focused on providing simple and reliable financial services. We keep improving the product experience to make every operation clear, fast, and secure.",
            email = "support@PixKudi.app",
            phone = "+234 9136275122",
            whatsapp = "+234 9136275122",
            address = "Lagos, Nigeria"
        )
    }

    override fun getFaqs(): List<FaqItem> {
        return listOf(
            FaqItem(
                id = "faq_1",
                question = "How do I apply for a loan?",
                answer = "Simply complete your registration on PixKudi, view your approved available credit on the home screen, and tap 'Withdraw Now' to choose your withdrawal amount and confirm details."
            ),
            FaqItem(
                id = "faq_2",
                question = "How do I withdraw my available credit?",
                answer = "On the home screen, tap the 'Withdraw Now' button. Check your repayment terms, interest rate, and repayment date, then tap 'Confirm Details' to finalize your request."
            ),
            FaqItem(
                id = "faq_3",
                question = "How is interest calculated?",
                answer = "Interest is calculated transparently based on your daily rate of 0.08% over the approved repayment duration of 180 days. No hidden charges or surprise deductions."
            ),
            FaqItem(
                id = "faq_4",
                question = "When should I repay my loan?",
                answer = "Your repayment date is clearly indicated on your loan confirmation and credit statement screen (e.g. 2027-02-10). Repaying on time improves your credit limit."
            ),
            FaqItem(
                id = "faq_5",
                question = "Why can't I withdraw?",
                answer = "Withdrawals may be restricted if you have an active pending loan, if your card status is unverified, or if you have reached your current credit limit. Please contact support if you need assistance."
            ),
            FaqItem(
                id = "faq_6",
                question = "How can I contact support?",
                answer = "You can reach our dedicated support team 24/7 via Email at support@PixKudi.app, Phone Call at +234 9136275122, or instant WhatsApp chat at +234 9136275122."
            )
        )
    }

    override fun getPrivacyPolicy(): List<PrivacySection> {
        return listOf(
            PrivacySection(
                title = "1. Introduction",
                content = "Welcome to PixKudi. We respect your privacy and are committed to protecting the personal data and financial credentials you share with us."
            ),
            PrivacySection(
                title = "2. Information We Collect",
                content = "We collect your phone number, basic device identification, and profile data necessary for account authentication, credit assessment, and secure financial transactions."
            ),
            PrivacySection(
                title = "3. How We Use Information",
                content = "Information collected is used strictly to provide, maintain, and enhance loan services, process withdrawals, verify OTPs, and provide customer support."
            ),
            PrivacySection(
                title = "4. Data Security",
                content = "We employ industry-standard encryption, tokenized transmission protocols, and strict access controls to safeguard your personal data from unauthorized access."
            ),
            PrivacySection(
                title = "5. Third-Party Services",
                content = "We only share necessary data with licensed banking partners and payment service providers solely to execute verified withdrawals and repayments."
            ),
            PrivacySection(
                title = "6. Communication",
                content = "We may send SMS OTPs and transactional notifications regarding your loan approval status, repayment reminders, and security alerts."
            ),
            PrivacySection(
                title = "7. Data Retention",
                content = "We retain your information as long as your account remains active or as required by regulatory financial authorities in accordance with applicable laws."
            ),
            PrivacySection(
                title = "8. Account Deletion",
                content = "You have the right to request deletion of your account and associated personal data anytime directly through the 'Delete My Account' option in the Mine section."
            ),
            PrivacySection(
                title = "9. Contact Us",
                content = "For questions or concerns about this Privacy Policy, contact us at support@PixKudi.app or via WhatsApp at +234 9136275122."
            )
        )
    }
}
