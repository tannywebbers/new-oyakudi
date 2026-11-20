package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.data.model.RepaymentStatus
import kotlinx.coroutines.delay

// Palette taken verbatim from the reference HTML design.
private val OverlayScrim = Color(0x80000000)
private val CardWhite = Color.White
private val CloseGray = Color(0xFF6B7280)
private val TitleDark = Color(0xFF1F2937)
private val BigAmountBlue = Color(0xFF2563EB)
private val SubtitleGray = Color(0xFF6B7280)
private val LoanRowBg = Color(0xFFF9FAFB)
private val LoanRowLabel = Color(0xFF4B5563)
private val LoanRowValue = Color(0xFF1F2937)
private val AccountBorder = Color(0xFF93C5FD)
private val AccountTitleGray = Color(0xFF4B5563)
private val AccountDescGray = Color(0xFF9CA3AF)
private val FieldLabelGray = Color(0xFF6B7280)
private val BankValueColor = Color(0xFF1F2937)
private val AccountNumberBlue = Color(0xFF1D4ED8)
private val CopyBg = Color(0xFFEFF6FF)
private val CopyText = Color(0xFF1D4ED8)
private val CopyBorder = Color(0xFFBFDBFE)
private val AccountHolderColor = Color(0xFF1F2937)
private val SecurityGray = Color(0xFF6B7280)
private val PaidGreen = Color(0xFF10B981)
private val PaidDisabled = Color(0xFF9CA3AF)

/**
 * Repayment bottom-sheet style modal. Mirrors the reference HTML "Payment Method" design:
 * shows the exact amount to pay plus a virtual account with a working Copy button, and the
 * green "Paid — Refresh status" action that notifies the backend.
 */
@Composable
fun RepaymentModal(
    amountToPay: String,
    loanAmount: String,
    bank: String,
    accountNumber: String,
    accountHolder: String,
    status: RepaymentStatus,
    onPaidClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val paidButtonText = when (status) {
        RepaymentStatus.IDLE -> stringResource(R.string.repayment_paid_button)
        RepaymentStatus.LOADING -> stringResource(R.string.repayment_notifying)
        RepaymentStatus.SUCCESS -> stringResource(R.string.repayment_success_button)
        RepaymentStatus.ERROR -> stringResource(R.string.repayment_failed_button)
    }
    val paidEnabled = status != RepaymentStatus.LOADING

    var copied by remember { mutableStateOf(false) }
    LaunchedEffect(copied) {
        if (copied) {
            delay(2000)
            copied = false
        }
    }

    var appeared by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { appeared = true }

    val scrimInteraction = remember { MutableInteractionSource() }
    val cardInteraction = remember { MutableInteractionSource() }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(OverlayScrim)
                .clickable(
                    interactionSource = scrimInteraction,
                    indication = null,
                    onClick = onDismiss
                ),
            contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(
                visible = appeared,
                enter = fadeIn(animationSpec = tween(300)) +
                    slideInVertically(animationSpec = tween(300)) { it / 10 }
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 420.dp)
                        .padding(20.dp)
                        .clickable(
                            interactionSource = cardInteraction,
                            indication = null,
                            onClick = {}
                        )
                        .testTag("repayment_modal"),
                    shape = RoundedCornerShape(16.dp),
                    color = CardWhite,
                    shadowElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(24.dp)
                    ) {
                        // Close "×"
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            val closeDescription = stringResource(R.string.repayment_close_description)
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .clickable(onClick = onDismiss)
                                    .semantics { contentDescription = closeDescription }
                                    .testTag("repayment_close"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "\u00D7",
                                    color = CloseGray,
                                    fontSize = 20.sp
                                )
                            }
                        }

                        Text(
                            text = stringResource(R.string.repayment_modal_title),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = TitleDark,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = amountToPay,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            textAlign = TextAlign.Center,
                            color = BigAmountBlue,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold
                        )

                        Text(
                            text = stringResource(R.string.repayment_total_to_pay),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                            textAlign = TextAlign.Center,
                            color = SubtitleGray,
                            fontSize = 13.sp
                        )

                        // Loan amount row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(LoanRowBg)
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.repayment_loan_amount_label),
                                color = LoanRowLabel,
                                fontSize = 14.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = loanAmount,
                                color = LoanRowValue,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Virtual account card
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(CardWhite)
                                .border(2.dp, AccountBorder, RoundedCornerShape(12.dp))
                                .padding(16.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.repayment_account_title),
                                color = AccountTitleGray,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = stringResource(R.string.repayment_account_desc),
                                color = AccountDescGray,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(top = 2.dp)
                            )

                            FieldLabel(stringResource(R.string.repayment_field_bank))
                            Text(
                                text = bank,
                                color = BankValueColor,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            FieldLabel(stringResource(R.string.repayment_field_account_number))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = accountNumber,
                                    color = AccountNumberBlue,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp,
                                    modifier = Modifier.weight(1f)
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(CopyBg)
                                        .border(1.dp, CopyBorder, RoundedCornerShape(6.dp))
                                        .clickable {
                                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                            clipboard.setPrimaryClip(
                                                ClipData.newPlainText("Account Number", accountNumber)
                                            )
                                            Toast.makeText(
                                                context,
                                                context.getString(R.string.repayment_copied_toast),
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            copied = true
                                        }
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                        .testTag("repayment_copy"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (copied) {
                                            stringResource(R.string.repayment_copied)
                                        } else {
                                            stringResource(R.string.repayment_copy)
                                        },
                                        color = CopyText,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        letterSpacing = 0.5.sp
                                    )
                                }
                            }

                            FieldLabel(stringResource(R.string.repayment_field_account_holder))
                            Text(
                                text = accountHolder,
                                color = AccountHolderColor,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Text(
                            text = stringResource(R.string.repayment_security_notice),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            textAlign = TextAlign.Center,
                            color = SecurityGray,
                            fontSize = 11.sp
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (paidEnabled) PaidGreen else PaidDisabled)
                                .clickable(enabled = paidEnabled, onClick = onPaidClick)
                                .padding(vertical = 12.dp)
                                .testTag("repayment_paid_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = paidButtonText,
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text,
        color = FieldLabelGray,
        fontSize = 10.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.5.sp,
        modifier = Modifier.padding(top = 8.dp)
    )
}
