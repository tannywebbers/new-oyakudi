package com.example.ui.screens.mine

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.SapaConfirmationDialog
import com.example.ui.theme.PixDanger
import com.example.ui.theme.PixPrimary
import com.example.ui.theme.PixTextMain
import com.example.ui.theme.PixTextMuted
import com.example.ui.theme.PixWellAbout
import com.example.ui.theme.PixWellFeedback
import com.example.ui.theme.PixWellHelp
import com.example.ui.theme.PixWellLedger
import com.example.ui.theme.PixWellLegal
import com.example.ui.viewmodel.AuthViewModel
import com.example.ui.viewmodel.MainViewModel

private val Lime = Color(0xFFB8E63A)
private val AvatarBg = Color(0xFF1A2433)
private val CardStroke = Color(0xFFEEF2F6)
private val DividerColor = Color(0xFFF1F5F9)
private val DangerWell = Color(0xFFFDEDEE)
private val ArrowTint = Color(0xFFCBD5E1)

@Composable
fun MineScreen(
    authViewModel: AuthViewModel,
    mainViewModel: MainViewModel,
    onCreditStatementClick: () -> Unit,
    onHelpCenterClick: () -> Unit,
    onFeedbackClick: () -> Unit,
    onAboutUsClick: () -> Unit,
    onContactUsClick: () -> Unit,
    onLoggedOut: () -> Unit
) {
    val authState by authViewModel.authState.collectAsState()
    val mainUiState by mainViewModel.uiState.collectAsState()
    val user = authState.user

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 4.dp)
            .testTag("mine_screen")
    ) {
        // Profile header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(PixPrimary, Lime)
                    )
                )
                .padding(top = 24.dp, bottom = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(AvatarBg),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "P",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = user?.name ?: "PixKudi",
                color = PixTextMain,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 14.dp)
            )

            Box(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White.copy(alpha = 0.20f))
                    .border(
                        1.dp,
                        Color.White.copy(alpha = 0.33f),
                        RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = user?.phoneNumber ?: "+814***2512",
                    color = PixTextMain,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Content shell (overlaps the header by 28dp)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-28).dp)
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .background(Color.White)
                .padding(horizontal = 20.dp)
                .padding(top = 24.dp, bottom = 8.dp)
        ) {
            SectionLabel(text = "Loans")
            GroupCard {
                MenuRow(
                    wellColor = PixWellLedger,
                    iconRes = R.drawable.ic_nav_ledger_outline,
                    title = "Loan Records",
                    subtitle = "View borrowing and repayment history",
                    onClick = onCreditStatementClick,
                    testTag = "mine_menu_statement"
                )
            }

            SectionLabel(text = "Support", topPadding = 22.dp)
            GroupCard {
                MenuRow(
                    wellColor = PixWellHelp,
                    iconRes = R.drawable.ic_contact_phone,
                    title = "Contact Us",
                    onClick = onContactUsClick,
                    testTag = "mine_menu_contact_us"
                )
                RowDivider()
                MenuRow(
                    wellColor = PixWellFeedback,
                    iconRes = R.drawable.ic_nav_suggest,
                    title = "Send Feedback",
                    onClick = onFeedbackClick,
                    testTag = "mine_menu_feedback"
                )
                RowDivider()
                MenuRow(
                    wellColor = PixWellAbout,
                    iconRes = R.drawable.ic_about_brand,
                    title = "About Us",
                    onClick = onAboutUsClick,
                    testTag = "mine_menu_about_us"
                )
            }

            SectionLabel(text = "Legal", topPadding = 22.dp)
            GroupCard {
                MenuRow(
                    wellColor = PixWellLegal,
                    iconRes = R.drawable.ic_nav_ledger_outline,
                    title = "Privacy Policy",
                    onClick = onHelpCenterClick,
                    testTag = "mine_menu_privacy"
                )
            }

            SectionLabel(text = "Account", topPadding = 22.dp, color = PixDanger)
            GroupCard {
                MenuRow(
                    wellColor = DangerWell,
                    iconRes = R.drawable.ic_nav_remove,
                    iconTint = PixDanger,
                    title = "Delete My Account",
                    titleColor = PixDanger,
                    onClick = { mainViewModel.setDeleteAccountDialogVisible(true) },
                    testTag = "mine_menu_delete_account"
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
                    .height(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
                    .border(1.5.dp, PixTextMain, RoundedCornerShape(14.dp))
                    .clickable { mainViewModel.setLogoutDialogVisible(true) }
                    .testTag("mine_menu_logout"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Sign Out",
                    color = PixTextMain,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    // Logout Confirmation Dialog
    SapaConfirmationDialog(
        show = mainUiState.showLogoutDialog,
        title = "Logout",
        message = "Are you sure you want to log out?",
        confirmText = "Logout",
        cancelText = "Cancel",
        isDestructive = false,
        onConfirm = {
            mainViewModel.setLogoutDialogVisible(false)
            authViewModel.logout(onLoggedOut)
        },
        onDismiss = {
            mainViewModel.setLogoutDialogVisible(false)
        },
        testTag = "dialog_logout"
    )

    // Delete Account Confirmation Dialog
    SapaConfirmationDialog(
        show = mainUiState.showDeleteAccountDialog,
        title = "Delete Account",
        message = "Are you sure you want to delete your account? This action cannot be undone.",
        confirmText = "Delete",
        cancelText = "Cancel",
        isDestructive = true,
        onConfirm = {
            mainViewModel.setDeleteAccountDialogVisible(false)
            authViewModel.deleteAccount(onLoggedOut)
        },
        onDismiss = {
            mainViewModel.setDeleteAccountDialogVisible(false)
        },
        testTag = "dialog_delete_account"
    )
}

@Composable
private fun SectionLabel(
    text: String,
    topPadding: androidx.compose.ui.unit.Dp = 0.dp,
    color: Color = PixTextMuted
) {
    Text(
        text = text.uppercase(),
        color = color,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.06.em,
        modifier = Modifier.padding(top = topPadding)
    )
}

@Composable
private fun GroupCard(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.dp, CardStroke, RoundedCornerShape(16.dp))
    ) {
        content()
    }
}

@Composable
private fun RowDivider() {
    HorizontalDivider(
        color = DividerColor,
        thickness = 1.dp,
        modifier = Modifier.padding(start = 66.dp)
    )
}

@Composable
private fun MenuRow(
    wellColor: Color,
    iconRes: Int,
    title: String,
    onClick: () -> Unit,
    testTag: String,
    subtitle: String? = null,
    titleColor: Color = PixTextMain,
    iconTint: Color = PixTextMain
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = if (subtitle != null) 64.dp else 60.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(wellColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        ) {
            Text(
                text = title,
                color = titleColor,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    color = PixTextMuted,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_end),
            contentDescription = null,
            tint = ArrowTint,
            modifier = Modifier.size(18.dp)
        )
    }
}
