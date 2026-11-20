package com.example.ui.screens.feedback

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.IssueType
import com.example.ui.components.AppHeader
import com.example.ui.components.GradientButton
import com.example.ui.components.SapaSuccessDialog
import com.example.ui.theme.PixDanger
import com.example.ui.theme.PixFieldBg
import com.example.ui.theme.PixPrimary
import com.example.ui.theme.PixPrimarySoft
import com.example.ui.theme.PixTextMain
import com.example.ui.theme.PixDivider
import com.example.ui.theme.PixTextMuted
import com.example.ui.viewmodel.MainViewModel

@Composable
fun FeedbackScreen(
    mainViewModel: MainViewModel,
    onBackClick: () -> Unit
) {
    val uiState by mainViewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current

    val topics = listOf(
        stringResource(R.string.suggest_issue_activation) to IssueType.ACTIVATION,
        stringResource(R.string.suggest_issue_payout) to IssueType.WITHDRAWAL,
        stringResource(R.string.suggest_issue_technical) to IssueType.APP_TECHNICAL,
        stringResource(R.string.suggest_issue_other) to IssueType.SUGGESTIONS_OTHERS
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .testTag("feedback_screen")
    ) {
        AppHeader(
            title = stringResource(R.string.suggest_title),
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 16.dp, bottom = 16.dp)
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
                    text = stringResource(R.string.suggest_hero_title),
                    fontSize = 20.sp,
                    color = PixTextMain,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.suggest_subtitle),
                    fontSize = 14.sp,
                    color = PixTextMuted,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Text(
                text = stringResource(R.string.suggest_pick_topic),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = PixTextMain,
                modifier = Modifier.padding(top = 24.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            val topicRows = topics.chunked(2)
            topicRows.forEachIndexed { rowIndex, row ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    row.forEachIndexed { index, topic ->
                        TopicTile(
                            text = topic.first,
                            isSelected = uiState.selectedIssueType == topic.second,
                            onClick = { mainViewModel.onIssueTypeSelected(topic.second) },
                            modifier = Modifier.weight(1f)
                        )
                        if (index < row.size - 1) {
                            Spacer(modifier = Modifier.width(10.dp))
                        }
                    }
                    if (row.size < 2) {
                        Spacer(modifier = Modifier.width(10.dp))
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                if (rowIndex < topicRows.size - 1) {
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            Text(
                text = stringResource(R.string.suggest_description),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = PixTextMain,
                modifier = Modifier.padding(top = 26.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                shape = RoundedCornerShape(16.dp),
                color = PixFieldBg,
                border = BorderStroke(1.dp, PixDivider)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    BasicTextField(
                        value = uiState.feedbackDescription,
                        onValueChange = { mainViewModel.onFeedbackDescriptionChanged(it) },
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("feedback_description_input"),
                        textStyle = TextStyle(
                            color = PixTextMain,
                            fontSize = 15.sp,
                            lineHeight = 22.sp
                        ),
                        cursorBrush = SolidColor(PixPrimary),
                        decorationBox = { innerTextField ->
                            if (uiState.feedbackDescription.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.suggest_hint_detail),
                                    color = PixTextMuted,
                                    fontSize = 15.sp
                                )
                            }
                            innerTextField()
                        }
                    )
                }
            }

            if (uiState.feedbackErrorMessage != null) {
                Text(
                    text = uiState.feedbackErrorMessage ?: "",
                    color = PixDanger,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .navigationBarsPadding()
                .padding(horizontal = 20.dp)
                .padding(top = 12.dp, bottom = 20.dp)
        ) {
            GradientButton(
                text = stringResource(R.string.suggest_submit),
                onClick = {
                    focusManager.clearFocus()
                    mainViewModel.submitFeedback { }
                },
                isLoading = uiState.isSubmittingFeedback,
                modifier = Modifier.fillMaxWidth(),
                testTag = "submit_feedback_button"
            )
        }
    }

    SapaSuccessDialog(
        show = uiState.feedbackSuccess,
        title = stringResource(R.string.suggest_submit_success),
        message = "Thank you for your feedback! Our support team will review your message promptly.",
        buttonText = "Done",
        onDismiss = {
            mainViewModel.dismissSuccessDialog()
            onBackClick()
        }
    )
}

@Composable
private fun TopicTile(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val shape = RoundedCornerShape(16.dp)

    Box(
        modifier = modifier
            .height(96.dp)
            .clip(shape)
            .background(if (isSelected) PixPrimarySoft else Color.White)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) PixPrimary else PixDivider,
                shape = shape
            )
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true),
                onClick = onClick
            )
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = PixTextMain,
            textAlign = TextAlign.Center,
            maxLines = 2,
            lineHeight = 18.sp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
