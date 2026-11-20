package com.example.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.screens.MainHostScreen
import com.example.ui.screens.about.AboutUsScreen
import com.example.ui.screens.auth.LoginScreen
import com.example.ui.screens.contact.ContactUsScreen
import com.example.ui.screens.feedback.FeedbackScreen
import com.example.ui.screens.help.HelpCenterScreen
import com.example.ui.screens.privacy.PrivacyPolicyScreen
import com.example.ui.screens.splash.SplashScreen
import com.example.ui.screens.statement.CreditStatementScreen
import com.example.ui.screens.statement.UsedCreditScreen
import com.example.ui.screens.withdraw.ConfirmApplicationScreen
import com.example.ui.screens.withdraw.WithdrawScreen
import com.example.ui.viewmodel.AuthViewModel
import com.example.ui.viewmodel.MainViewModel

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = viewModel(),
    mainViewModel: MainViewModel = viewModel()
) {
    val authState by authViewModel.authState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        }
    ) {
        // Splash Screen
        composable(Screen.Splash.route) {
            SplashScreen(
                onSplashFinished = {
                    val target = if (authState.isAuthenticated) Screen.MainHost.route else Screen.Login.route
                    navController.navigate(target) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // Login / Register Screen
        composable(Screen.Login.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.MainHost.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onPrivacyPolicyClick = {
                    navController.navigate(Screen.PrivacyPolicy.route)
                }
            )
        }

        // Main Host (Home + Mine Tabs)
        composable(Screen.MainHost.route) {
            MainHostScreen(
                authViewModel = authViewModel,
                mainViewModel = mainViewModel,
                onWithdrawClick = {
                    navController.navigate(Screen.Withdraw.route)
                },
                onCreditStatementClick = {
                    navController.navigate(Screen.CreditStatement.route)
                },
                onHelpCenterClick = {
                    navController.navigate(Screen.HelpCenter.route)
                },
                onFeedbackClick = {
                    navController.navigate(Screen.Feedback.route)
                },
                onAboutUsClick = {
                    navController.navigate(Screen.AboutUs.route)
                },
                onContactUsClick = {
                    navController.navigate(Screen.ContactUs.route)
                },
                onLoggedOut = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.MainHost.route) { inclusive = true }
                    }
                }
            )
        }

        // Withdraw Screen
        composable(Screen.Withdraw.route) {
            WithdrawScreen(
                mainViewModel = mainViewModel,
                onBackClick = { navController.popBackStack() },
                onConfirmDetailsClick = {
                    navController.navigate(Screen.ConfirmApplication.route)
                }
            )
        }

        // Confirm Application Screen
        composable(Screen.ConfirmApplication.route) {
            ConfirmApplicationScreen(
                mainViewModel = mainViewModel,
                onBackClick = { navController.popBackStack() },
                onConfirmedSuccess = {
                    navController.navigate(Screen.MainHost.route) {
                        popUpTo(Screen.MainHost.route) { inclusive = false }
                    }
                }
            )
        }

        // Credit Statement Screen
        composable(Screen.CreditStatement.route) {
            CreditStatementScreen(
                mainViewModel = mainViewModel,
                onBackClick = { navController.popBackStack() },
                onViewUsedCreditClick = {
                    navController.navigate(Screen.UsedCredit.route)
                }
            )
        }

        // Used Credit Screen
        composable(Screen.UsedCredit.route) {
            UsedCreditScreen(
                mainViewModel = mainViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        // Help Center Screen
        composable(Screen.HelpCenter.route) {
            HelpCenterScreen(
                mainViewModel = mainViewModel,
                onBackClick = { navController.popBackStack() },
                onContactUsClick = {
                    navController.navigate(Screen.ContactUs.route)
                }
            )
        }

        // Feedback Screen
        composable(Screen.Feedback.route) {
            FeedbackScreen(
                mainViewModel = mainViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        // About Us Screen
        composable(Screen.AboutUs.route) {
            AboutUsScreen(
                mainViewModel = mainViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        // Contact Us Screen
        composable(Screen.ContactUs.route) {
            ContactUsScreen(
                mainViewModel = mainViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        // Privacy Policy Screen
        composable(Screen.PrivacyPolicy.route) {
            PrivacyPolicyScreen(
                mainViewModel = mainViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
