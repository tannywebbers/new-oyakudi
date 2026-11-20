package com.example.ui.components

import android.graphics.Color as AndroidColor
import android.webkit.WebView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.PixPrimary
import com.example.ui.theme.PixTextMain

/**
 * Popup modal that renders announcement HTML fetched from the database and offers a
 * single Okay button to dismiss it.
 */
@Composable
fun AnnouncementPopup(
    html: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .padding(20.dp)
                .testTag("announcement_popup")
        ) {
            Text(
                text = "Announcement",
                color = PixTextMain,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(14.dp))

            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        settings.javaScriptEnabled = false
                        setBackgroundColor(AndroidColor.TRANSPARENT)
                        loadDataWithBaseURL(null, wrapAnnouncementHtml(html), "text/html", "UTF-8", null)
                        tag = html
                    }
                },
                update = { webView ->
                    if (webView.tag != html) {
                        webView.loadDataWithBaseURL(null, wrapAnnouncementHtml(html), "text/html", "UTF-8", null)
                        webView.tag = html
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(340.dp)
                    .testTag("announcement_html")
            )

            Spacer(modifier = Modifier.height(18.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(PixPrimary)
                    .clickable(onClick = onDismiss)
                    .testTag("announcement_okay_button"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Okay",
                    color = PixTextMain,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

private fun wrapAnnouncementHtml(html: String): String {
    return """
        <!DOCTYPE html>
        <html>
        <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <style>
        html, body { margin: 0; padding: 0; background: transparent; }
        body {
            font-family: -apple-system, 'Segoe UI', Roboto, Arial, sans-serif;
            font-size: 15px;
            line-height: 1.5;
            color: #0F172A;
            word-wrap: break-word;
        }
        img { max-width: 100%; height: auto; }
        a { color: #3E5C0D; }
        h1, h2, h3 { font-size: 17px; margin: 12px 0 6px; }
        p { margin: 0 0 10px; }
        </style>
        </head>
        <body>$html</body>
        </html>
    """.trimIndent()
}
