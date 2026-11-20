package com.example.ui.screens.contact

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.AppHeader
import com.example.ui.components.RoundedCard
import com.example.ui.screens.about.RowDivider
import com.example.ui.screens.about.SectionLabel
import com.example.ui.screens.about.SupportChannelRow
import com.example.ui.screens.about.launchDialerIntent
import com.example.ui.screens.about.launchEmailIntent
import com.example.ui.screens.about.launchWhatsAppIntent
import com.example.ui.theme.PixPrimarySoft
import com.example.ui.theme.PixTextMain
import com.example.ui.theme.PixTextMuted
import com.example.ui.theme.PixWellHelp
import com.example.ui.theme.PixWellLedger
import com.example.ui.theme.PixWellLegal
import com.example.ui.viewmodel.MainViewModel

@Composable
fun ContactUsScreen(
    mainViewModel: MainViewModel,
    onBackClick: () -> Unit
) {
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
            .testTag("contact_us_screen")
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
        }
    }
}
