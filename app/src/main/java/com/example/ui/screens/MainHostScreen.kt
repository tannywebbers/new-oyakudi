package com.example.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.ui.components.AnnouncementPopup
import com.example.ui.components.AppBottomBar
import com.example.ui.components.BottomTab
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.mine.MineScreen
import com.example.ui.viewmodel.AuthViewModel
import com.example.ui.viewmodel.MainViewModel

@Composable
fun MainHostScreen(
    authViewModel: AuthViewModel,
    mainViewModel: MainViewModel,
    onWithdrawClick: () -> Unit,
    onCreditStatementClick: () -> Unit,
    onHelpCenterClick: () -> Unit,
    onFeedbackClick: () -> Unit,
    onAboutUsClick: () -> Unit,
    onContactUsClick: () -> Unit,
    onLoggedOut: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(BottomTab.HOME) }

    val showAnnouncementPopup by mainViewModel.showAnnouncementPopup.collectAsState()
    val announcementPopupHtml by mainViewModel.announcementPopupHtml.collectAsState()

    LaunchedEffect(Unit) {
        mainViewModel.loadAnnouncementAndMaybeShowPopup()
    }

    if (showAnnouncementPopup && announcementPopupHtml.isNotBlank()) {
        AnnouncementPopup(
            html = announcementPopupHtml,
            onDismiss = { mainViewModel.dismissAnnouncementPopup() }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Crossfade(
                targetState = selectedTab,
                label = "tab_crossfade"
            ) { tab ->
                when (tab) {
                    BottomTab.HOME -> {
                        HomeScreen(
                            mainViewModel = mainViewModel,
                            onWithdrawClick = onWithdrawClick,
                            onCreditStatementClick = onCreditStatementClick,
                            onHelpCenterClick = onHelpCenterClick,
                            onFeedbackClick = onFeedbackClick,
                            onAboutUsClick = onAboutUsClick,
                            onContactUsClick = onContactUsClick,
                            onProfileClick = { selectedTab = BottomTab.MINE }
                        )
                    }
                    BottomTab.MINE -> {
                        MineScreen(
                            authViewModel = authViewModel,
                            mainViewModel = mainViewModel,
                            onCreditStatementClick = onCreditStatementClick,
                            onHelpCenterClick = onHelpCenterClick,
                            onFeedbackClick = onFeedbackClick,
                            onAboutUsClick = onAboutUsClick,
                            onContactUsClick = onContactUsClick,
                            onLoggedOut = onLoggedOut
                        )
                    }
                }
            }
        }

        AppBottomBar(
            currentTab = selectedTab,
            onTabSelected = { selectedTab = it }
        )
    }
}
