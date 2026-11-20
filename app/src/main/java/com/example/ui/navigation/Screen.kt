package com.example.ui.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Login : Screen("login")
    data object MainHost : Screen("main_host")
    data object Home : Screen("home")
    data object Withdraw : Screen("withdraw")
    data object ConfirmApplication : Screen("confirm_application")
    data object Mine : Screen("mine")
    data object CreditStatement : Screen("credit_statement")
    data object UsedCredit : Screen("used_credit")
    data object HelpCenter : Screen("help_center")
    data object Feedback : Screen("feedback")
    data object AboutUs : Screen("about_us")
    data object ContactUs : Screen("contact_us")
    data object PrivacyPolicy : Screen("privacy_policy")
}
