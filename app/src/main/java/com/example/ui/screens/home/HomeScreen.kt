package com.example.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.NewsTicker
import com.example.ui.theme.PixDivider
import com.example.ui.theme.PixPrimary
import com.example.ui.theme.PixPrimarySoft
import com.example.ui.theme.PixTabInactive
import com.example.ui.theme.PixTextMain
import com.example.ui.theme.PixTextMuted
import com.example.ui.theme.PixWellFeedback
import com.example.ui.theme.PixWellHelp
import com.example.ui.theme.PixWellLedger
import com.example.ui.theme.PixYellow
import com.example.ui.theme.PixYellowLight
import com.example.ui.viewmodel.MainViewModel

@Composable
fun HomeScreen(
    mainViewModel: MainViewModel,
    onWithdrawClick: () -> Unit,
    onCreditStatementClick: () -> Unit,
    onHelpCenterClick: () -> Unit,
    onFeedbackClick: () -> Unit,
    onAboutUsClick: () -> Unit,
    onContactUsClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val customerCase by mainViewModel.customerCase.collectAsState()
    val announcement by mainViewModel.announcement.collectAsState()
    val creditInfo by mainViewModel.creditInfo.collectAsState()

    val isApproved = customerCase.isStatusApproved
    val displayAmount = if (isApproved) {
        customerCase.formattedAvailableCredit
    } else {
        customerCase.formattedOutstandingBalance
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp, bottom = 24.dp)
            .testTag("home_screen")
    ) {
        // (a) Header row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(id = R.string.dash_welcome_back),
                    color = PixTextMuted,
                    fontSize = 13.sp
                )
                val displayName = if (customerCase.customerName.isNotBlank() &&
                    customerCase.customerName != "Customer"
                ) {
                    customerCase.customerName
                } else {
                    "PixKudi"
                }
                Text(
                    text = displayName,
                    color = PixTextMain,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(PixPrimarySoft)
                    .clickable(onClick = onProfileClick)
                    .testTag("home_profile_avatar"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_hub_account),
                    contentDescription = stringResource(id = R.string.tab_account),
                    tint = PixTextMain,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // (b) Credit card panel
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 18.dp)
                .heightIn(min = 196.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(PixYellowLight, PixYellow, PixYellow)
                    )
                )
                .testTag("home_credit_card")
        ) {
            // Decorative orbs
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 28.dp, y = (-36).dp)
                    .size(140.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.35f))
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .offset(x = (-24).dp, y = 20.dp)
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.16f))
            )

            // Watermark
            Text(
                text = stringResource(id = R.string.dash_card_watermark),
                modifier = Modifier
                    .align(Alignment.Center)
                    .rotate(-16f),
                color = Color.White.copy(alpha = 0.12f),
                fontSize = 52.sp,
                fontWeight = FontWeight.Bold
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 22.dp, end = 22.dp, top = 22.dp, bottom = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(id = R.string.dash_card_title),
                            color = PixTextMain,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(id = R.string.dash_card_subtitle),
                            color = Color(0xFF475569),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(999.dp))
                            .background(Color.White.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_card_secure),
                            contentDescription = stringResource(id = R.string.dash_card_subtitle),
                            tint = PixTextMain,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Text(
                    text = creditInfo.cardInfo.cardNumber,
                    color = PixTextMain,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 0.08.em,
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 28.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 22.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(id = R.string.dash_status_label),
                            color = PixTabInactive,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        val statusActive = isApproved
                        val pillBg = if (statusActive) Color(0xFF1E293B) else Color.White.copy(alpha = 0.20f)
                        Box(
                            modifier = Modifier
                                .padding(top = 6.dp)
                                .clip(RoundedCornerShape(999.dp))
                                .background(pillBg)
                                .then(
                                    if (statusActive) Modifier
                                    else Modifier.border(
                                        1.dp,
                                        Color.White.copy(alpha = 0.40f),
                                        RoundedCornerShape(999.dp)
                                    )
                                )
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = stringResource(
                                    id = if (statusActive) R.string.dash_status_active
                                    else R.string.dash_status_inactive
                                ),
                                color = if (statusActive) Color.White else PixTextMain,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = stringResource(id = R.string.dash_expire_label),
                            color = PixTabInactive,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = creditInfo.cardInfo.expireOn,
                            color = PixTextMain,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 6.dp)
                        )
                    }
                }
            }
        }

        // (c) News ticker (content fetched from the database)
        if (announcement.hasTicker) {
            NewsTicker(
                text = announcement.newsTickerText.orEmpty(),
                modifier = Modifier.padding(top = 14.dp)
            )
        }

        // (d) Quota action panel
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 18.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(20.dp))
                .padding(bottom = 20.dp)
                .testTag("available_credit_card")
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 14.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.dash_credit_title_ready),
                    color = PixTextMain,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            HorizontalDivider(
                color = PixDivider,
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.dash_credit_title),
                    color = PixTextMuted,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = displayAmount,
                    color = PixTextMain,
                    fontSize = 44.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .testTag("credit_amount_display")
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                        .height(52.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(PixPrimary)
                        .clickable(onClick = onWithdrawClick)
                        .testTag("withdraw_now_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.dash_payout_now),
                        color = PixTextMain,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // (e) Quick action tiles
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 14.dp)
        ) {
            QuickTile(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 10.dp),
                wellColor = PixWellLedger,
                iconRes = R.drawable.ic_nav_ledger_outline,
                label = stringResource(id = R.string.dash_quick_ledger),
                onClick = onCreditStatementClick,
                testTag = "home_tile_ledger"
            )
            QuickTile(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 10.dp),
                wellColor = PixWellHelp,
                iconRes = R.drawable.ic_contact_phone,
                label = stringResource(id = R.string.dash_quick_contact),
                onClick = onContactUsClick,
                testTag = "home_tile_support"
            )
            QuickTile(
                modifier = Modifier.weight(1f),
                wellColor = PixWellFeedback,
                iconRes = R.drawable.ic_dash_feedback,
                label = stringResource(id = R.string.dash_quick_feedback),
                onClick = onFeedbackClick,
                testTag = "home_tile_feedback"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun QuickTile(
    modifier: Modifier,
    wellColor: Color,
    iconRes: Int,
    label: String,
    onClick: () -> Unit,
    testTag: String
) {
    Column(
        modifier = modifier
            .heightIn(min = 104.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFEEF2F6), RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .testTag(testTag),
        horizontalAlignment = Alignment.CenterHorizontally
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
                contentDescription = label,
                tint = PixTextMain,
                modifier = Modifier.size(20.dp)
            )
        }

        Text(
            text = label,
            color = PixTextMain,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 2,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        )
    }
}
