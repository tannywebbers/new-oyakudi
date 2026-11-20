package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val SapaLightColorScheme = lightColorScheme(
    primary = PixPrimary,
    onPrimary = PixTextMain,
    primaryContainer = PixPrimarySoft,
    onPrimaryContainer = PixAmountDue,
    secondary = PixYellow,
    onSecondary = PixTextMain,
    secondaryContainer = PixPrimarySoft,
    onSecondaryContainer = PixAmountDue,
    tertiary = PixGreen,
    background = PixBackground,
    onBackground = PixTextMain,
    surface = PixSurface,
    onSurface = PixTextMain,
    surfaceVariant = PixFieldBg,
    onSurfaceVariant = PixTextMuted,
    outline = PixDivider,
    outlineVariant = PixDivider,
    error = PixDanger,
    onError = PixSurface
)

private val SapaDarkColorScheme = darkColorScheme(
    primary = SapaTeal,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF004D40),
    onPrimaryContainer = SapaMint,
    secondary = SapaLime,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF1B382B),
    onSecondaryContainer = SapaLime,
    tertiary = SapaGreen,
    background = Color(0xFF0E1621),
    onBackground = Color(0xFFE6EDF3),
    surface = Color(0xFF17212B),
    onSurface = Color(0xFFE6EDF3),
    surfaceVariant = Color(0xFF1F2B37),
    onSurfaceVariant = Color(0xFF8B98A5),
    outline = Color(0xFF2E3D4D),
    outlineVariant = Color(0xFF2E3D4D),
    error = SapaRed,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) SapaDarkColorScheme else SapaLightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                val insetsController = WindowCompat.getInsetsController(window, view)
                // When darkTheme is false (light background), isAppearanceLightStatusBars = true ensures dark icons (time, battery, wifi) are crisp and visible
                // When darkTheme is true (dark background), isAppearanceLightStatusBars = false ensures white icons are crisp and visible
                insetsController.isAppearanceLightStatusBars = !darkTheme
                insetsController.isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
