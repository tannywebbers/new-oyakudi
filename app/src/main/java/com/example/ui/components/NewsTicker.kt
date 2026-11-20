package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PixPrimarySoft
import com.example.ui.theme.PixTextMain
import kotlin.math.roundToInt

/**
 * Continuously scrolling news ticker (marquee) whose text is fetched from the database.
 */
@Composable
fun NewsTicker(
    text: String,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()
    val textStyle = remember {
        TextStyle(color = PixTextMain, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }

    val textWidthPx = remember(text, textMeasurer) {
        textMeasurer.measure(
            text = AnnotatedString(text),
            style = textStyle,
            softWrap = false,
            maxLines = 1,
            overflow = TextOverflow.Visible
        ).size.width
    }

    var containerWidthPx by remember { mutableStateOf(0) }

    val transition = rememberInfiniteTransition(label = "news_ticker")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 16000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "news_ticker_progress"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(34.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(PixPrimarySoft)
            .onSizeChanged { containerWidthPx = it.width }
            .testTag("news_ticker"),
        contentAlignment = Alignment.CenterStart
    ) {
        val total = containerWidthPx + textWidthPx
        val offsetX = containerWidthPx - (progress * total)
        Text(
            text = text,
            style = textStyle,
            maxLines = 1,
            softWrap = false,
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .wrapContentWidth(unbounded = true)
        )
    }
}
