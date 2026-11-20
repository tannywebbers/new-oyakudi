package com.example.ui.screens.withdraw

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.LoanAppSubmissionResult
import com.example.ui.components.AppHeader
import com.example.ui.components.GradientButton
import com.example.ui.theme.PixBackground
import com.example.ui.theme.PixDanger
import com.example.ui.theme.PixPrimary
import com.example.ui.theme.PixPrimarySoft
import com.example.ui.theme.PixTextMain
import com.example.ui.theme.PixTextMuted
import com.example.ui.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private val SubmitHeroGradient = Brush.linearGradient(
    colors = listOf(PixPrimarySoft, Color(0xFFE8F9B8))
)

private val SubmitCardStroke = Color(0xFFEEF2F6)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmApplicationScreen(
    mainViewModel: MainViewModel,
    onBackClick: () -> Unit,
    onConfirmedSuccess: () -> Unit
) {
    val customerCase by mainViewModel.customerCase.collectAsState()
    val selectedAmount by mainViewModel.selectedWithdrawAmount.collectAsState()
    val uiState by mainViewModel.uiState.collectAsState()

    val amount = selectedAmount
    val durationDays = customerCase.durationDays
    val interestRate = customerCase.interestRate
    val repaymentAmount = mainViewModel.calculateRepaymentAmount(amount, durationDays, interestRate)
    val amountStr = "₦%,d".format(amount)
    val repaymentAmountStr = "₦%,d".format(repaymentAmount)

    val cal = Calendar.getInstance()
    cal.add(Calendar.DAY_OF_YEAR, durationDays)
    val repaymentDateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PixBackground)
            .testTag("confirm_application_screen")
    ) {
        AppHeader(
            title = stringResource(R.string.submit_order_title),
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Hero receive amount
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(SubmitHeroGradient)
                    .padding(horizontal = 18.dp, vertical = 22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.submit_order_receive_label),
                    color = PixTextMuted,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = amountStr,
                    color = PixTextMain,
                    fontWeight = FontWeight.Bold,
                    fontSize = 36.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = stringResource(R.string.submit_order_terms_label),
                color = PixTextMain,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                border = BorderStroke(1.dp, SubmitCardStroke)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 4.dp)
                ) {
                    OrderSummaryRow(
                        label = stringResource(R.string.payout_detail_total),
                        value = repaymentAmountStr,
                        valueColor = PixDanger
                    )
                    HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)
                    OrderSummaryRow(
                        label = stringResource(R.string.submit_order_due_label),
                        value = repaymentDateStr
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Human-readable terms (SapaClear style): what you receive, what you repay, and by when.
            val termsText = buildAnnotatedString {
                append("You will receive ")
                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = PixTextMain)) {
                    append(amountStr)
                }
                append(" into your bank account and repay ")
                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = PixDanger)) {
                    append(repaymentAmountStr)
                }
                append(" in ")
                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = PixTextMain)) {
                    append("$durationDays days")
                }
                append(" by ")
                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = PixTextMain)) {
                    append(repaymentDateStr)
                }
                append(".")
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                border = BorderStroke(1.dp, SubmitCardStroke)
            ) {
                Text(
                    text = termsText,
                    color = PixTextMuted,
                    fontSize = 15.sp,
                    lineHeight = 24.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 18.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Confirm action
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(PixBackground)
                .navigationBarsPadding()
                .padding(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 24.dp)
        ) {
            GradientButton(
                text = stringResource(R.string.submit_order_agree),
                onClick = {
                    mainViewModel.submitLoanApplication(
                        amount = amount,
                        periodDays = durationDays,
                        onResult = {
                            // Handled by result dialog
                        }
                    )
                },
                isLoading = uiState.isSubmittingLoan,
                modifier = Modifier.fillMaxWidth(),
                height = 52.dp,
                cornerRadius = 16.dp,
                testTag = "agree_and_confirm_button"
            )
        }
    }

    // Modal Status Dialogs for DB Result Outcomes
    uiState.loanSubmissionResult?.let { result ->
        when (result) {
            is LoanAppSubmissionResult.Approved -> {
                StatusOutcomeDialog(
                    icon = Icons.Default.CheckCircle,
                    iconTint = PixPrimary,
                    iconBg = PixPrimarySoft,
                    title = "Application Approved!",
                    message = "${result.message}\n\nYour repayment of ₦%,d is due on ${result.dueDate}. Your home balance is now updated.".format(result.totalRepayment),
                    buttonText = stringResource(R.string.payout_review_back_home),
                    onDismiss = {
                        mainViewModel.dismissSubmissionDialog()
                        onConfirmedSuccess()
                    }
                )
            }
            is LoanAppSubmissionResult.Pending -> {
                StatusOutcomeDialog(
                    icon = Icons.Default.HourglassTop,
                    iconTint = Color(0xFFE67E22),
                    iconBg = Color(0xFFFEF3C7),
                    title = "Application Pending",
                    message = "${result.message}\nExpected decision by: ${result.decisionDate ?: "2 business days"}.",
                    buttonText = "Got it",
                    onDismiss = {
                        mainViewModel.dismissSubmissionDialog()
                        onConfirmedSuccess()
                    }
                )
            }
            is LoanAppSubmissionResult.Declined -> {
                StatusOutcomeDialog(
                    icon = Icons.Default.Block,
                    iconTint = Color(0xFFE74C3C),
                    iconBg = Color(0xFFFEE2E2),
                    title = "Application Declined",
                    message = "We could not approve this request at this time.\nReason: ${result.reason}\n\nYou can re-apply on ${result.retryDate}.",
                    buttonText = "Close",
                    onDismiss = {
                        mainViewModel.dismissSubmissionDialog()
                        onConfirmedSuccess()
                    }
                )
            }
            is LoanAppSubmissionResult.FailedOrSuspended -> {
                StatusOutcomeDialog(
                    icon = Icons.Default.WarningAmber,
                    iconTint = Color(0xFFD97706),
                    iconBg = Color(0xFFFFEDD5),
                    title = "Application Failed / Suspended",
                    message = "${result.reason}\n\n⚠️ You can make one withdrawal per day. Your next withdrawal will be available on ${result.retryDate}.",
                    buttonText = "Understood",
                    onDismiss = {
                        mainViewModel.dismissSubmissionDialog()
                        onConfirmedSuccess()
                    }
                )
            }
            is LoanAppSubmissionResult.CooldownBlocked -> {
                StatusOutcomeDialog(
                    icon = Icons.Default.Lock,
                    iconTint = Color(0xFFC2410C),
                    iconBg = Color(0xFFFFEDD5),
                    title = "Daily Withdrawal Limit Reached",
                    message = "You have already made a withdrawal attempt today. You can withdraw again in about ${result.hoursRemaining} hour(s), on ${result.retryDate}.",
                    buttonText = "OK",
                    onDismiss = {
                        mainViewModel.dismissSubmissionDialog()
                        onBackClick()
                    }
                )
            }
        }
    }
}

@Composable
private fun OrderSummaryRow(
    label: String,
    value: String,
    valueColor: Color = PixTextMain
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 52.dp)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = PixTextMuted,
            fontSize = 14.sp,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            color = valueColor,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatusOutcomeDialog(
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    title: String,
    message: String,
    buttonText: String,
    onDismiss: () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(iconBg, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = PixTextMain,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = PixTextMuted,
                        fontSize = 14.sp,
                        lineHeight = 22.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                GradientButton(
                    text = buttonText,
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
