package com.example.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.GradientButton
import com.example.ui.theme.PixBackground
import com.example.ui.theme.PixButtonGradient
import com.example.ui.theme.PixDanger
import com.example.ui.theme.PixDivider
import com.example.ui.theme.PixPrimary
import com.example.ui.theme.PixTextHint
import com.example.ui.theme.PixTextMain
import com.example.ui.theme.PixTextMuted
import com.example.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.delay
import kotlin.random.Random

private val FieldShape = RoundedCornerShape(14.dp)

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onPrivacyPolicyClick: () -> Unit
) {
    val uiState by authViewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current

    var step by remember { mutableStateOf(0) }
    var otp by remember { mutableStateOf("") }

    // Auto-generate a 4-digit code 5 seconds after landing on the OTP step,
    // filling the code boxes so the user can simply confirm and continue.
    LaunchedEffect(step) {
        if (step == 1) {
            delay(5000)
            otp = Random.nextInt(0, 10000).toString().padStart(4, '0')
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PixBackground)
            .statusBarsPadding()
            .testTag("login_screen")
    ) {
        if (step == 0) {
            PhoneStep(
                phoneNumber = uiState.phoneNumber,
                isLoading = uiState.isLoading,
                onPhoneChange = { authViewModel.onPhoneNumberChanged(it) },
                onNext = {
                    focusManager.clearFocus()
                    authViewModel.clearMessages()
                    step = 1
                }
            )
        } else {
            OtpStep(
                phoneNumber = uiState.phoneNumber,
                otp = otp,
                isAgreed = uiState.isAgreed,
                isLoading = uiState.isLoading,
                errorMessage = uiState.errorMessage,
                onOtpChange = { new -> otp = new.filter { it.isDigit() }.take(4) },
                onEditPhone = {
                    otp = ""
                    step = 0
                },
                onAgreeToggle = { authViewModel.onAgreeToggled(it) },
                onPrivacyPolicyClick = onPrivacyPolicyClick,
                onLogin = {
                    focusManager.clearFocus()
                    authViewModel.loginOrRegister(onLoginSuccess)
                }
            )
        }
    }
}

@Composable
private fun PhoneStep(
    phoneNumber: String,
    isLoading: Boolean,
    onPhoneChange: (String) -> Unit,
    onNext: () -> Unit
) {
    val isPhoneValid = phoneNumber.length >= 10

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 28.dp, end = 28.dp, top = 72.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.pc_brand_logo),
                contentDescription = "PixKudi Logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = stringResource(id = R.string.access_welcome),
                color = PixTextMain,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(id = R.string.access_subtitle),
                color = PixTextMuted,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = stringResource(id = R.string.access_phone_label_caps).uppercase(),
                color = PixTextMuted,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                letterSpacing = 0.08.em,
                modifier = Modifier
                    .align(Alignment.Start)
                    .testTag("phone_label")
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .width(84.dp)
                        .fillMaxHeight()
                        .clip(FieldShape)
                        .background(Color.White)
                        .border(1.dp, PixDivider, FieldShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.access_country_code),
                        color = PixTextMain,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(FieldShape)
                        .background(Color.White)
                        .border(1.dp, PixDivider, FieldShape)
                ) {
                    BasicTextField(
                        value = phoneNumber,
                        onValueChange = onPhoneChange,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                            .testTag("phone_input"),
                        singleLine = true,
                        textStyle = TextStyle(
                            color = PixTextMain,
                            fontSize = 16.sp
                        ),
                        cursorBrush = SolidColor(PixPrimary),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone,
                            imeAction = ImeAction.Done
                        ),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                if (phoneNumber.isEmpty()) {
                                    Text(
                                        text = stringResource(id = R.string.access_phone_hint_masked),
                                        color = PixTextHint,
                                        fontSize = 16.sp
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        GradientButton(
            text = stringResource(id = R.string.access_next),
            onClick = onNext,
            enabled = isPhoneValid && !isLoading,
            isLoading = isLoading,
            height = 58.dp,
            cornerRadius = 16.dp,
            gradient = PixButtonGradient,
            modifier = Modifier.padding(horizontal = 28.dp),
            testTag = "access_next_button"
        )

        Spacer(modifier = Modifier.height(38.dp))
    }
}

@Composable
private fun OtpStep(
    phoneNumber: String,
    otp: String,
    isAgreed: Boolean,
    isLoading: Boolean,
    errorMessage: String?,
    onOtpChange: (String) -> Unit,
    onEditPhone: () -> Unit,
    onAgreeToggle: (Boolean) -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    onLogin: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    var isOtpFocused by remember { mutableStateOf(false) }
    val isOtpComplete = otp.length == 4

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(start = 28.dp, end = 28.dp, top = 72.dp, bottom = 24.dp)
        ) {
            Text(
                text = stringResource(id = R.string.access_otp_title),
                color = PixTextMain,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(id = R.string.access_otp_hint),
                color = PixTextMuted,
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(id = R.string.access_otp_phone, phoneNumber),
                    color = PixTextMain,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Text(
                    text = stringResource(id = R.string.access_edit_phone),
                    color = PixPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    modifier = Modifier
                        .clickable(onClick = onEditPhone)
                        .padding(horizontal = 14.dp)
                        .testTag("edit_phone")
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.access_code_label_caps).uppercase(),
                    color = PixTextMuted,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    letterSpacing = 0.08.em,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = stringResource(id = R.string.access_resend_code),
                    color = PixTextMuted,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .clickable { }
                        .padding(start = 8.dp)
                        .testTag("resend_code")
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(64.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    repeat(4) { index ->
                        val char = otp.getOrNull(index)?.toString() ?: ""
                        val isActive = isOtpFocused && index == otp.length.coerceAtMost(3)

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(13.dp))
                                .background(Color.White)
                                .border(
                                    width = 1.5.dp,
                                    color = if (isActive) PixPrimary else Color(0xFFD7DCE2),
                                    shape = RoundedCornerShape(13.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = char,
                                color = PixTextMain,
                                fontWeight = FontWeight.Medium,
                                fontSize = 22.sp
                            )
                        }
                    }
                }

                BasicTextField(
                    value = otp,
                    onValueChange = onOtpChange,
                    modifier = Modifier
                        .matchParentSize()
                        .focusRequester(focusRequester)
                        .onFocusChanged { isOtpFocused = it.isFocused }
                        .testTag("otp_input"),
                    singleLine = true,
                    textStyle = TextStyle(color = Color.Transparent, fontSize = 22.sp),
                    cursorBrush = SolidColor(Color.Transparent),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
                )
            }

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = errorMessage,
                    color = PixDanger,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 28.dp, end = 28.dp, top = 8.dp, bottom = 12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Checkbox(
                checked = isAgreed,
                onCheckedChange = onAgreeToggle,
                colors = CheckboxDefaults.colors(
                    checkedColor = PixPrimary,
                    uncheckedColor = PixTextMuted,
                    checkmarkColor = PixTextMain
                ),
                modifier = Modifier.testTag("privacy_checkbox")
            )

            val prefix = stringResource(id = R.string.access_agree_prefix)
            val policy = stringResource(id = R.string.access_privacy_policy)
            val policyText = buildAnnotatedString {
                append(prefix)
                append(" ")
                pushStringAnnotation(tag = "privacy", annotation = policy)
                withStyle(SpanStyle(color = PixPrimary, fontWeight = FontWeight.Bold)) {
                    append(policy)
                }
                pop()
            }

            ClickableText(
                text = policyText,
                style = TextStyle(
                    color = PixTextMuted,
                    fontSize = 14.sp,
                    lineHeight = 18.sp
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 10.dp)
                    .testTag("privacy_policy_link"),
                onClick = { offset ->
                    if (policyText.getStringAnnotations("privacy", offset, offset).isNotEmpty()) {
                        onPrivacyPolicyClick()
                    }
                }
            )
        }

        GradientButton(
            text = stringResource(id = R.string.access_action),
            onClick = onLogin,
            enabled = isOtpComplete && !isLoading,
            isLoading = isLoading,
            height = 58.dp,
            cornerRadius = 16.dp,
            gradient = PixButtonGradient,
            modifier = Modifier.padding(horizontal = 28.dp),
            testTag = "verify_access_button"
        )

        Spacer(modifier = Modifier.height(38.dp))
    }
}
