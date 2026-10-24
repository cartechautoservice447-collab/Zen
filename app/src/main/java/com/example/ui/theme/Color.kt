package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Nocturne Learning Lounge / Midnight Focus exact design tokens
val MidnightBackground = Color(0xFF0B1020)
val MidnightCard = Color(0xFF131A2B)
val MidnightSurface2 = Color(0xFF192238)
val MidnightMuted = Color(0xFF1B2439)
val MidnightBorder = Color(0x17E2E8FF) // rgba(226, 232, 255, 0.09)
val MidnightBorderStrong = Color(0x2EE2E8FF)
val MidnightForeground = Color(0xFFF5F7FF)
val MidnightFgSecondary = Color(0xFFA2ABC2)
val MidnightFgMuted = Color(0xFF707B95)

// Accent and brand colors
val MidnightPrimary = Color(0xFF7266FF)
val MidnightPrimaryVariant = Color(0xFF9B72FF)
val MidnightCyan = Color(0xFF55D8F2)
val MidnightLime = Color(0xFFC4F36A)
val MidnightSuccess = Color(0xFF44D7A1)
val MidnightWarning = Color(0xFFFFC46B)
val MidnightDestructive = Color(0xFFFF6B7A)

// Primary Gradients
val MidnightPrimaryGradient = Brush.linearGradient(
    listOf(MidnightPrimary, MidnightPrimaryVariant)
)

val MidnightCyanGradient = Brush.linearGradient(
    listOf(Color(0xFF55D8F2), Color(0xFF38BDF8))
)

val MidnightLimeGradient = Brush.linearGradient(
    listOf(Color(0xFFC4F36A), Color(0xFF10B981))
)

// Legacy compatibility aliases for existing screens and components
val Indigo900 = Color(0xFF0B1020)
val Indigo800 = Color(0xFF131A2B)
val Indigo700 = Color(0xFF192238)
val Indigo600 = MidnightPrimary
val Indigo500 = MidnightPrimary
val Indigo400 = MidnightPrimaryVariant
val Indigo300 = Color(0xFF818CF8)
val Indigo100 = Color(0xFFE0E7FF)
val Indigo50 = Color(0xFF192238)

val Sky500 = MidnightCyan
val Sky400 = MidnightCyan
val Sky100 = Color(0x2655D8F2)

val Mint500 = MidnightSuccess
val Mint400 = MidnightSuccess
val Mint100 = Color(0x2644D7A1)

val Amber500 = MidnightWarning
val Amber400 = MidnightWarning
val Amber100 = Color(0x26FFC46B)

val Rose500 = MidnightDestructive
val Rose400 = MidnightDestructive
val Rose100 = Color(0x26FF6B7A)

val Coral500 = Color(0xFFF97316)
val Coral100 = Color(0x26F97316)

val WarmWhite = MidnightBackground
val WarmGray50 = MidnightCard
val WarmGray100 = MidnightSurface2

val Slate900 = MidnightBackground
val Slate800 = MidnightCard
val Slate700 = MidnightSurface2
val Slate600 = MidnightMuted
val Slate500 = MidnightFgMuted
val Slate400 = MidnightFgSecondary
val Slate300 = MidnightFgSecondary
val Slate200 = MidnightBorder
val Slate100 = MidnightForeground
val Slate50 = MidnightForeground
val PureWhite = MidnightForeground

val DarkBackground = MidnightBackground
val DarkSurface = MidnightCard
val DarkSurfaceVariant = MidnightSurface2
val DarkOutline = MidnightBorder

val Cobalt600 = MidnightPrimary
val Cobalt500 = MidnightPrimary
val Emerald500 = MidnightSuccess
val Emerald100 = Mint100
