package com.example.ui.screens.withdraw

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.AppHeader
import com.example.ui.components.GradientButton
import com.example.ui.theme.PixBackground
import com.example.ui.theme.PixDanger
import com.example.ui.theme.PixFieldBg
import com.example.ui.theme.PixPrimary
import com.example.ui.theme.PixPrimarySoft
import com.example.ui.theme.PixTextMain
import com.example.ui.theme.PixTextMuted
import com.example.ui.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.roundToLong

private val PixHeroGradient = Brush.linearGradient(
    colors = listOf(PixPrimarySoft, Color(0xFFE8F9B8))
)

private val PixCardStroke = Color(0xFFEEF2F6)

@Composable
fun WithdrawScreen(
    mainViewModel: MainViewModel,
    onBackClick: () -> Unit,
    onConfirmDetailsClick: () -> Unit
) {
    val customerCase by mainViewModel.customerCase.collectAsState()
    val selectedAmount by mainViewModel.selectedWithdrawAmount.collectAsState()

    val maxAmount = if (customerCase.requestedCreditLimit > 0) customerCase.requestedCreditLimit else customerCase.effectiveAvailableCredit.coerceAtLeast(3000L)
    val minAmount = (maxAmount / 3L).coerceAtLeast(1000L)
    val midAmount = (maxAmount * 2L / 3L).coerceIn(minAmount, maxAmount)

    val currentAmount = selectedAmount.coerceIn(minAmount, maxAmount)
    val durationDays = customerCase.durationDays
    val interestRate = customerCase.interestRate
    val repaymentAmount = mainViewModel.calculateRepaymentAmount(currentAmount, durationDays, interestRate)
    val formattedCurrentAmount = "₦%,d".format(currentAmount)
    val formattedRepaymentAmount = "₦%,d".format(repaymentAmount)
    val repayCal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, durationDays) }
    val repaymentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(repayCal.time)
    val tiers = remember(minAmount, midAmount, maxAmount) {
        listOf(minAmount, midAmount, maxAmount).distinct()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PixBackground)
            .testTag("withdraw_screen")
    ) {
        AppHeader(
            title = stringResource(R.string.payout_title),
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Hero amount banner
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(PixHeroGradient)
                    .padding(horizontal = 18.dp, vertical = 22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.payout_select_amount),
                    color = PixTextMuted,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = formattedCurrentAmount,
                    color = PixTextMain,
                    fontWeight = FontWeight.Bold,
                    fontSize = 36.sp,
                    modifier = Modifier.testTag("selected_withdraw_amount_text")
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Tier options + slider card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                border = BorderStroke(1.dp, PixCardStroke)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 14.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        tiers.forEach { tier ->
                            TierOptionRow(
                                text = "₦%,d".format(tier),
                                isSelected = currentAmount == tier,
                                onClick = { mainViewModel.setSelectedWithdrawAmount(tier) },
                                testTag = "preset_chip_$tier"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Slider(
                        value = currentAmount.toFloat(),
                        onValueChange = { newValue ->
                            val rounded = (newValue / 500f).roundToLong() * 500L
                            val clamped = rounded.coerceIn(minAmount, maxAmount)
                            mainViewModel.setSelectedWithdrawAmount(clamped)
                        },
                        valueRange = minAmount.toFloat()..maxAmount.toFloat(),
                        steps = if (maxAmount > minAmount) ((maxAmount - minAmount) / 500 - 1).toInt().coerceAtLeast(0) else 0,
                        colors = SliderDefaults.colors(
                            thumbColor = PixPrimary,
                            activeTrackColor = PixPrimary,
                            inactiveTrackColor = PixPrimarySoft,
                            activeTickColor = Color.Transparent,
                            inactiveTickColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("withdraw_amount_slider")
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                    ) {
                        Text(
                            text = "₦%,d".format(minAmount),
                            color = PixTextMuted,
                            fontSize = 12.sp,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "₦%,d".format(maxAmount),
                            color = PixTextMuted,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Loan summary
            Text(
                text = stringResource(R.string.payout_summary_label),
                color = PixTextMain,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                border = BorderStroke(1.dp, PixCardStroke)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 4.dp)
                ) {
                    SummaryRow(
                        label = stringResource(R.string.payout_detail_amount),
                        value = formattedCurrentAmount
                    )
                    HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)
                    SummaryRow(
                        label = stringResource(R.string.payout_detail_interest_rate),
                        value = "%.2f%%".format(interestRate)
                    )
                    HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)
                    SummaryRow(
                        label = stringResource(R.string.payout_detail_period),
                        value = "$durationDays days"
                    )
                    HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)
                    SummaryRow(
                        label = stringResource(R.string.submit_order_due_label),
                        value = repaymentDate
                    )
                    HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)
                    SummaryRow(
                        label = stringResource(R.string.payout_detail_total),
                        value = formattedRepaymentAmount,
                        valueColor = PixDanger
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Footer action
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(PixBackground)
                .navigationBarsPadding()
                .padding(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 24.dp)
        ) {
            GradientButton(
                text = stringResource(R.string.payout_confirm),
                onClick = onConfirmDetailsClick,
                modifier = Modifier.fillMaxWidth(),
                height = 52.dp,
                cornerRadius = 16.dp,
                testTag = "confirm_details_button"
            )
        }
    }
}

@Composable
private fun TierOptionRow(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) PixPrimary else PixFieldBg)
            .clickable(onClick = onClick)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = PixTextMain,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun SummaryRow(
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
