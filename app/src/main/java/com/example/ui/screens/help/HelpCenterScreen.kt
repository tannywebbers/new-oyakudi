package com.example.ui.screens.help

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.FaqItem
import com.example.ui.components.AppHeader
import com.example.ui.components.RoundedCard
import com.example.ui.screens.about.RowDivider
import com.example.ui.screens.about.SectionLabel
import com.example.ui.screens.about.SupportChannelRow
import com.example.ui.screens.about.launchDialerIntent
import com.example.ui.screens.about.launchEmailIntent
import com.example.ui.screens.about.launchWhatsAppIntent
import com.example.ui.theme.PixDivider
import com.example.ui.theme.PixPrimary
import com.example.ui.theme.PixPrimarySoft
import com.example.ui.theme.PixTextMain
import com.example.ui.theme.PixTextMuted
import com.example.ui.theme.PixWellHelp
import com.example.ui.theme.PixWellLedger
import com.example.ui.theme.PixWellLegal
import com.example.ui.viewmodel.MainViewModel

@Composable
fun HelpCenterScreen(
    mainViewModel: MainViewModel,
    onBackClick: () -> Unit,
    onContactUsClick: () -> Unit
) {
    val faqs = remember { mainViewModel.getFaqs() }
    val uiState by mainViewModel.uiState.collectAsState()
    val context = LocalContext.current

    val email = stringResource(R.string.support_email_value)
    val phone = stringResource(R.string.support_call_value)
    val whatsapp = stringResource(R.string.support_whatsapp_value)
    val noEmailApp = stringResource(R.string.support_no_email_app)
    val noDialerApp = stringResource(R.string.support_no_dialer_app)
    val noWhatsAppApp = stringResource(R.string.support_no_whatsapp_app)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .testTag("help_center_screen")
    ) {
        AppHeader(
            title = stringResource(R.string.support_title),
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 16.dp, bottom = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Brush.linearGradient(listOf(PixPrimarySoft, Color(0xFFE8F9B8))))
                    .padding(horizontal = 18.dp)
                    .padding(vertical = 20.dp)
            ) {
                Text(
                    text = stringResource(R.string.support_hero_title),
                    fontSize = 20.sp,
                    color = PixTextMain,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.support_subtitle),
                    fontSize = 14.sp,
                    color = PixTextMuted,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            SectionLabel(
                text = stringResource(R.string.support_channels_label),
                modifier = Modifier.padding(top = 24.dp)
            )

            RoundedCard(
                cornerRadius = 16.dp,
                contentPadding = 0.dp,
                modifier = Modifier.padding(top = 10.dp)
            ) {
                SupportChannelRow(
                    title = stringResource(R.string.support_email_title),
                    value = email,
                    iconRes = R.drawable.ic_contact_email,
                    wellColor = PixWellLegal,
                    onClick = { launchEmailIntent(context, email, noEmailApp) }
                )

                RowDivider()

                SupportChannelRow(
                    title = stringResource(R.string.support_call_title),
                    value = phone,
                    iconRes = R.drawable.ic_contact_phone,
                    wellColor = PixWellHelp,
                    onClick = { launchDialerIntent(context, phone, noDialerApp) }
                )

                RowDivider()

                SupportChannelRow(
                    title = stringResource(R.string.support_whatsapp_title),
                    value = whatsapp,
                    iconRes = R.drawable.ic_contact_chat,
                    wellColor = PixWellLedger,
                    onClick = { launchWhatsAppIntent(context, whatsapp, noWhatsAppApp) }
                )
            }

            Text(
                text = stringResource(R.string.support_hours_tip),
                fontSize = 13.sp,
                color = PixTextMuted,
                lineHeight = 19.sp,
                modifier = Modifier.padding(top = 16.dp)
            )

            SectionLabel(
                text = "Frequently Asked Questions",
                modifier = Modifier.padding(top = 28.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            faqs.forEach { faq ->
                FaqCardItem(
                    faq = faq,
                    isExpanded = uiState.activeFaqExpandedId == faq.id,
                    onToggle = { mainViewModel.toggleFaq(faq.id) }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            RoundedCard(
                cornerRadius = 16.dp,
                contentPadding = 18.dp,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clickable(onClick = onContactUsClick)
                    .testTag("help_contact_shortcut")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Still have questions?",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = PixTextMain
                        )
                        Text(
                            text = "Our support team is always ready to assist you.",
                            fontSize = 13.sp,
                            color = PixTextMuted,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(PixPrimarySoft),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_contact_chat),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FaqCardItem(
    faq: FaqItem,
    isExpanded: Boolean,
    onToggle: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    RoundedCard(
        cornerRadius = 16.dp,
        contentPadding = 0.dp,
        modifier = Modifier.testTag("faq_${faq.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = interactionSource,
                    indication = ripple(bounded = true),
                    onClick = onToggle
                )
                .padding(horizontal = 18.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = faq.question,
                    color = PixTextMain,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = PixPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column {
                    HorizontalDivider(
                        color = PixDivider,
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                    Text(
                        text = faq.answer,
                        color = PixTextMuted,
                        fontSize = 14.sp,
                        lineHeight = 21.sp
                    )
                }
            }
        }
    }
}
