package com.example.ui.screens.statement

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.CreditStatement
import com.example.data.model.CustomerCase
import com.example.data.model.RepaymentStatus
import com.example.data.model.VirtualRepaymentAccount
import com.example.ui.components.AppHeader
import com.example.ui.components.GradientButton
import com.example.ui.components.RepaymentModal
import com.example.ui.theme.PixAmountDue
import com.example.ui.theme.PixDanger
import com.example.ui.theme.PixPrimarySoft
import com.example.ui.theme.PixRedSoft
import com.example.ui.theme.PixTextMain
import com.example.ui.theme.PixTextMuted
import com.example.ui.theme.PixWellFeedback
import com.example.ui.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Locale

internal val LedgerBackground = Color(0xFFFAFBFD)
internal val LedgerCardStroke = Color(0xFFEEF2F6)
internal val LedgerDivider = Color(0xFFF1F5F9)

internal data class LedgerEntry(
    val id: String,
    val title: String,
    val amount: String,
    val date: String,
    val status: String
)

internal enum class LedgerStatus { ACTIVE, REVIEW, OVERDUE, REJECTED, EXPIRING }

internal fun ledgerStatusOf(status: String): LedgerStatus {
    val value = status.lowercase()
    return when {
        value.contains("overdue") -> LedgerStatus.OVERDUE
        value.contains("reject") || value.contains("declin") || value.contains("fail") -> LedgerStatus.REJECTED
        value.contains("expir") -> LedgerStatus.EXPIRING
        value.contains("review") || value.contains("pending") || value.contains("process") -> LedgerStatus.REVIEW
        else -> LedgerStatus.ACTIVE
    }
}

private fun daysOverdue(date: String): Int {
    return try {
        val due = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(date) ?: return 1
        val diff = ((System.currentTimeMillis() - due.time) / 86_400_000L).toInt()
        diff.coerceAtLeast(1)
    } catch (e: Exception) {
        1
    }
}

/**
 * Fail-open debt check: the "Repay Now" action is hidden only when the debt is known to be
 * zero (or less) AND the status clearly reports the loan as settled/paid. Anything unclear
 * (unknown status, parse issues) keeps the action visible.
 */
private fun shouldShowRepayNow(customer: CustomerCase): Boolean {
    val debt = customer.outstandingLoanBalance
    if (debt > 0) return true
    val status = "${customer.status} ${customer.loanAppStatus}".lowercase(Locale.getDefault())
    val settled = listOf("paid", "clear", "settle", "closed").any { status.contains(it) }
    return !settled
}

@Composable
fun CreditStatementScreen(
    mainViewModel: MainViewModel,
    onBackClick: () -> Unit,
    onViewUsedCreditClick: () -> Unit
) {
    var statement by remember { mutableStateOf(CreditStatement()) }
    var refreshKey by remember { mutableStateOf(0) }

    val customerCase by mainViewModel.customerCase.collectAsState()
    val showRepaymentModal by mainViewModel.showRepaymentModal.collectAsState()
    val repaymentStatus by mainViewModel.repaymentStatus.collectAsState()
    val context = LocalContext.current

    val showRepayNow = shouldShowRepayNow(customerCase)
    val amountToPay = customerCase.outstandingLoanBalance
    val formattedAmountToPay = "₦%,d".format(amountToPay)

    LaunchedEffect(refreshKey) {
        if (refreshKey > 0) {
            mainViewModel.refreshCustomerDebt()
        }
        statement = mainViewModel.getCreditStatement()
    }

    LaunchedEffect(repaymentStatus) {
        if (repaymentStatus == RepaymentStatus.SUCCESS) {
            Toast.makeText(
                context,
                context.getString(R.string.repayment_confirmation_message),
                Toast.LENGTH_SHORT
            ).show()
            delay(2000)
            mainViewModel.dismissRepaymentModal()
            refreshKey++
        }
    }

    val ledgerTitleText = stringResource(R.string.ledger_title)
    val entries: List<LedgerEntry> = buildList<LedgerEntry> {
        if (statement.loanStatus != "None" && statement.outstandingBalance.isNotBlank()) {
            add(
                LedgerEntry(
                    id = "summary",
                    title = ledgerTitleText,
                    amount = statement.outstandingBalance,
                    date = statement.repaymentDate,
                    status = statement.loanStatus
                )
            )
        }
        statement.transactions.forEach { tx ->
            add(
                LedgerEntry(
                    id = tx.id,
                    title = tx.title,
                    amount = tx.repaymentAmount.ifBlank { tx.formattedAmount },
                    date = tx.dueDate.ifBlank { tx.date },
                    status = tx.status
                )
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LedgerBackground)
            .testTag("credit_statement_screen")
    ) {
        AppHeader(
            title = stringResource(R.string.ledger_title),
            onBackClick = onBackClick,
            rightContent = {
                Text(
                    text = stringResource(R.string.ledger_refresh),
                    color = PixAmountDue,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { refreshKey++ }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .testTag("ledger_refresh_button")
                )
            }
        )

        if (entries.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.ledger_empty),
                    color = PixTextMuted,
                    fontSize = 16.sp,
                    modifier = Modifier.testTag("ledger_empty")
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(entries, key = { it.id }) { entry ->
                    val isDebtRow = entry.id == "summary"
                    LedgerRowCard(
                        entry = entry,
                        showRepayButton = if (isDebtRow) showRepayNow else true,
                        repayLabel = if (isDebtRow) {
                            stringResource(R.string.repay_now)
                        } else {
                            stringResource(R.string.ledger_repay)
                        },
                        onClick = onViewUsedCreditClick,
                        onRepayClick = if (isDebtRow) {
                            { mainViewModel.openRepaymentModal() }
                        } else {
                            onViewUsedCreditClick
                        }
                    )
                }
            }
        }
    }

    if (showRepaymentModal) {
        RepaymentModal(
            amountToPay = formattedAmountToPay,
            loanAmount = formattedAmountToPay,
            bank = VirtualRepaymentAccount.BANK,
            accountNumber = VirtualRepaymentAccount.ACCOUNT_NUMBER,
            accountHolder = VirtualRepaymentAccount.ACCOUNT_HOLDER,
            status = repaymentStatus,
            onPaidClick = { mainViewModel.submitRepayment() },
            onDismiss = { mainViewModel.dismissRepaymentModal() }
        )
    }
}

@Composable
internal fun LedgerRowCard(
    entry: LedgerEntry,
    showRepayButton: Boolean = false,
    repayLabel: String = "Repay",
    onClick: (() -> Unit)? = null,
    onRepayClick: () -> Unit = {}
) {
    val shape = RoundedCornerShape(16.dp)
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) Modifier.clip(shape).clickable(onClick = onClick)
                else Modifier
            ),
        shape = shape,
        color = Color.White,
        border = BorderStroke(1.dp, LedgerCardStroke)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 52.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = entry.title,
                    color = PixTextMain,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )
                LedgerStatusChip(status = entry.status, date = entry.date)
            }

            HorizontalDivider(
                color = LedgerDivider,
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.ledger_amount_due_label),
                    color = PixTextMain,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = entry.amount,
                    color = PixAmountDue,
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp
                )
                if (showRepayButton) {
                    GradientButton(
                        text = repayLabel,
                        onClick = onRepayClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        height = 52.dp,
                        cornerRadius = 16.dp,
                        testTag = "ledger_repay_button_${entry.id}"
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.ledger_on_or_before_line, entry.date),
                    color = PixTextMain,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
internal fun LedgerStatusChip(status: String, date: String) {
    val label: String
    val background: Color
    val foreground: Color
    when (ledgerStatusOf(status)) {
        LedgerStatus.ACTIVE -> {
            label = if (status.isBlank()) "Active" else status
            background = PixPrimarySoft
            foreground = PixAmountDue
        }
        LedgerStatus.REVIEW -> {
            label = stringResource(R.string.ledger_status_review)
            background = PixWellFeedback
            foreground = Color(0xFFB45309)
        }
        LedgerStatus.OVERDUE -> {
            label = stringResource(R.string.ledger_status_overdue_days, daysOverdue(date))
            background = PixDanger.copy(alpha = 0.12f)
            foreground = PixDanger
        }
        LedgerStatus.REJECTED -> {
            label = stringResource(R.string.ledger_status_rejected)
            background = PixRedSoft
            foreground = PixDanger
        }
        LedgerStatus.EXPIRING -> {
            label = stringResource(R.string.ledger_status_expiring)
            background = PixWellFeedback
            foreground = Color(0xFFB45309)
        }
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(background)
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(
            text = label,
            color = foreground,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )
    }
}
