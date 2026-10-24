package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val MidnightColorScheme = darkColorScheme(
    primary = MidnightPrimary,
    onPrimary = MidnightForeground,
    primaryContainer = MidnightSurface2,
    onPrimaryContainer = MidnightForeground,
    secondary = MidnightCyan,
    onSecondary = MidnightBackground,
    secondaryContainer = MidnightSurface2,
    onSecondaryContainer = MidnightCyan,
    tertiary = MidnightLime,
    onTertiary = MidnightBackground,
    background = MidnightBackground,
    onBackground = MidnightForeground,
    surface = MidnightCard,
    onSurface = MidnightForeground,
    surfaceVariant = MidnightSurface2,
    onSurfaceVariant = MidnightFgSecondary,
    outline = MidnightBorder,
    error = MidnightDestructive,
    onError = MidnightForeground
)

@Composable
fun LearnlyTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    // Nocturne Learning Lounge / Midnight Focus is a signature midnight dark UI
    MaterialTheme(
        colorScheme = MidnightColorScheme,
        typography = Typography,
        content = content
    )
}
