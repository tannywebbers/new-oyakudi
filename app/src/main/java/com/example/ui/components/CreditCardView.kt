package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CardInfo
import com.example.ui.theme.PixAmountDue
import com.example.ui.theme.PixCardGradient
import com.example.ui.theme.PixTextMain

@Composable
fun CreditCardView(
    cardInfo: CardInfo,
    modifier: Modifier = Modifier
) {
    val cardShape = RoundedCornerShape(24.dp)
    val cardGradient = PixCardGradient

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = cardShape,
                ambientColor = Color(0x33FCE404),
                spotColor = Color(0x33FCE404)
            )
            .clip(cardShape),
        shape = cardShape,
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(cardGradient)
                .padding(horizontal = 24.dp, vertical = 22.dp)
        ) {
            // Stylized Naira watermark in the background
            Text(
                text = "₦",
                fontSize = 130.sp,
                fontWeight = FontWeight.Black,
                color = PixAmountDue.copy(alpha = 0.12f),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .offset(x = 10.dp, y = 5.dp)
            )

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Card Title
                Text(
                    text = cardInfo.cardTitle,
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = PixAmountDue,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Card Number
                Text(
                    text = cardInfo.cardNumber,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = PixTextMain,
                        fontWeight = FontWeight.Bold,
                        fontSize = 21.sp,
                        letterSpacing = 1.2.sp,
                        fontFamily = FontFamily.Monospace
                    )
                )

                Spacer(modifier = Modifier.height(22.dp))

                // Bottom Row (Status & Expire On)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(
                            text = "Status",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = PixAmountDue,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = cardInfo.status,
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = PixTextMain,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Expire On",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = PixAmountDue,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = cardInfo.expireOn,
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = PixTextMain,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        )
                    }
                }
            }
        }
    }
}
