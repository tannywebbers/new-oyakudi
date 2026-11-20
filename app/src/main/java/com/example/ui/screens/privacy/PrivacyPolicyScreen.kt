package com.example.ui.screens.privacy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.AppHeader
import com.example.ui.components.RoundedCard
import com.example.ui.theme.PixTextMain
import com.example.ui.theme.PixTextMuted
import com.example.ui.viewmodel.MainViewModel

@Composable
fun PrivacyPolicyScreen(
    mainViewModel: MainViewModel,
    onBackClick: () -> Unit
) {
    val sections = remember { mainViewModel.getPrivacyPolicy() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .testTag("privacy_policy_screen")
    ) {
        AppHeader(
            title = stringResource(R.string.legal_title),
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 16.dp, bottom = 24.dp)
        ) {
            Text(
                text = "PixKudi Privacy & Data Policy",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = PixTextMain
            )

            Text(
                text = "Last updated: August 2026",
                fontSize = 13.sp,
                color = PixTextMuted,
                modifier = Modifier.padding(top = 6.dp)
            )

            sections.forEach { section ->
                RoundedCard(
                    cornerRadius = 16.dp,
                    contentPadding = 18.dp,
                    modifier = Modifier.padding(top = 12.dp)
                ) {
                    Text(
                        text = section.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = PixTextMain
                    )

                    Text(
                        text = section.content,
                        fontSize = 14.sp,
                        color = PixTextMuted,
                        lineHeight = 22.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}
