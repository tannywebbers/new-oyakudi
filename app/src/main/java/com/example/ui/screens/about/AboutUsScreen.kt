package com.example.ui.screens.about

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.AppHeader
import com.example.ui.components.RoundedCard
import com.example.ui.theme.PixDivider
import com.example.ui.theme.PixPrimarySoft
import com.example.ui.theme.PixTextMain
import com.example.ui.theme.PixTextMuted
import com.example.ui.theme.PixWellHelp
import com.example.ui.theme.PixWellLedger
import com.example.ui.theme.PixWellLegal
import com.example.ui.viewmodel.MainViewModel

@Composable
fun AboutUsScreen(
    mainViewModel: MainViewModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val email = stringResource(R.string.company_contact_email)
    val phone = stringResource(R.string.support_call_value)
    val whatsapp = stringResource(R.string.support_whatsapp_value)
    val noEmailApp = stringResource(R.string.support_no_email_app)
    val noDialerApp = stringResource(R.string.support_no_dialer_app)
    val noWhatsAppApp = stringResource(R.string.support_no_whatsapp_app)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .testTag("about_us_screen")
    ) {
        AppHeader(
            title = stringResource(R.string.company_title),
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 16.dp, bottom = 24.dp)
        ) {
            // Brand hero
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Brush.linearGradient(listOf(PixPrimarySoft, Color(0xFFE8F9B8))))
                    .padding(horizontal = 20.dp)
                    .padding(top = 28.dp, bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .shadow(2.dp, RoundedCornerShape(18.dp))
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.pc_brand_logo),
                        contentDescription = stringResource(R.string.app_name),
                        modifier = Modifier.size(56.dp)
                    )
                }

                Text(
                    text = stringResource(R.string.app_name),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = PixTextMain,
                    modifier = Modifier.padding(top = 14.dp)
                )

                Text(
                    text = stringResource(R.string.company_hero_tagline),
                    fontSize = 14.sp,
                    color = PixTextMuted,
                    modifier = Modifier.padding(top = 6.dp)
                )

                Box(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color.White)
                        .border(1.dp, PixDivider, RoundedCornerShape(50))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = stringResource(R.string.company_version),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = PixTextMain
                    )
                }
            }

            SectionLabel(
                text = stringResource(R.string.company_mission_label),
                modifier = Modifier.padding(top = 24.dp)
            )

            RoundedCard(
                cornerRadius = 16.dp,
                contentPadding = 18.dp,
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Text(
                    text = stringResource(R.string.company_company_desc),
                    fontSize = 15.sp,
                    color = PixTextMain,
                    lineHeight = 22.sp
                )
            }

            SectionLabel(
                text = stringResource(R.string.company_contact_title),
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
                text = stringResource(R.string.company_footer_note),
                fontSize = 12.sp,
                color = PixTextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp)
            )
        }
    }
}

@Composable
fun SectionLabel(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text.uppercase(),
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = PixTextMuted,
        letterSpacing = 0.06.em,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun SupportChannelRow(
    title: String,
    value: String,
    iconRes: Int,
    wellColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 72.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true),
                onClick = onClick
            )
            .padding(horizontal = 14.dp, vertical = 14.dp),
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
                tint = Color.Unspecified,
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
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = PixTextMain
            )
            Text(
                text = value,
                fontSize = 13.sp,
                color = PixTextMuted,
                modifier = Modifier.padding(top = 3.dp)
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_end),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
fun RowDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 66.dp)
            .height(1.dp)
            .background(PixDivider)
    )
}

fun launchEmailIntent(context: Context, email: String, noAppMessage: String = "No email app found") {
    try {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$email")
            putExtra(Intent.EXTRA_SUBJECT, "PixKudi Inquiry")
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, noAppMessage, Toast.LENGTH_LONG).show()
    }
}

fun launchDialerIntent(context: Context, phone: String, noAppMessage: String = "No dialer app found") {
    try {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:${phone.replace(" ", "")}")
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, noAppMessage, Toast.LENGTH_LONG).show()
    }
}

fun launchWhatsAppIntent(context: Context, phone: String, noAppMessage: String = "No WhatsApp app found") {
    try {
        val cleanNumber = phone.replace("+", "").replace(" ", "")
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("https://wa.me/$cleanNumber")
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, noAppMessage, Toast.LENGTH_LONG).show()
    }
}
