package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// ============================================================================
// OyaKudi palette — extracted from the OyaKudi app resources (res/values/colors.xml,
// the `qcash_*` tokens). This is the visual identity used across the app.
// ============================================================================

// Brand accents
val OyaPrimary = Color(0xFF059669)        // qcash_green — primary brand green
val OyaPrimarySoft = Color(0xFFD1FAE5)    // qcash_green_light
val OyaPrimaryDark = Color(0xFF047857)    // darker shade for pressed states
val OyaYellow = Color(0xFFFCE404)         // pc_card_yellow equivalent for accents
val OyaYellowLight = Color(0xFFFFF176)    // pc_card_grad_start equivalent
val OyaAmountDue = Color(0xFF065F46)      // dark green for amounts due
val OyaDanger = Color(0xFFF04438)         // qcash_red
val OyaRedSoft = Color(0xFFFEECEB)

// Neutrals
val OyaBackground = Color(0xFFFFFFFF)
val OyaSurface = Color(0xFFFFFFFF)
val OyaFieldBg = Color(0xFFF7F7F8)        // qcash_input_bg
val OyaDivider = Color(0xFFE5E7EB)        // qcash_border
val OyaBtnMuted = Color(0xFFD1D5DB)
val OyaTextMain = Color(0xFF0F172A)       // qcash_text_primary
val OyaTextMuted = Color(0xFF6B7280)      // qcash_text_secondary
val OyaTextHint = Color(0xFF999999)
val OyaTabInactive = Color(0xFF64748B)
val OyaGreen = Color(0xFF22C55E)

// Profile "well" backgrounds (reusing OyaKudi green tones)
val OyaWellAbout = Color(0xFFEEF1F8)
val OyaWellFeedback = Color(0xFFFFF6DF)
val OyaWellHelp = Color(0xFFE9F8EF)
val OyaWellLedger = Color(0xFFEAF5D6)
val OyaWellLegal = Color(0xFFF0F4FF)

// Gradients
val OyaButtonGradient = Brush.horizontalGradient(
    colors = listOf(
        Color(0xFF6EE7B7),  // lighter green
        OyaPrimary
    )
)

val OyaCardGradient = Brush.linearGradient(
    colors = listOf(
        OyaYellowLight,
        OyaYellow
    )
)

val OyaLogoGradient = Brush.linearGradient(
    colors = listOf(
        OyaYellow,
        OyaPrimary
    )
)

// ============================================================================
// Backward-compatible aliases. The screens/components still reference these
// legacy names; they now resolve to OyaKudi colors so the whole app adopts the
// OyaKudi identity without touching every call site.
// ============================================================================
val SapaTeal = OyaPrimary
val SapaTealDark = OyaAmountDue
val SapaLime = OyaYellow
val SapaGreen = OyaGreen
val SapaMint = OyaPrimarySoft
val SapaMintDark = OyaPrimarySoft

val SapaBackground = OyaBackground
val SapaSurface = OyaSurface
val SapaBorder = OyaDivider
val SapaTextNavy = OyaTextMain
val SapaTextSecondary = OyaTextMuted
val SapaTextMuted = OyaTextHint

val SapaRed = OyaDanger
val SapaRedLight = OyaRedSoft
val SapaGold = OyaYellow

val SapaButtonGradient = OyaButtonGradient
val SapaCardGradient = OyaCardGradient
val SapaLogoGradient = OyaLogoGradient