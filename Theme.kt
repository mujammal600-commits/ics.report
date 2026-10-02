package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CyberColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = Color(0xFF00363F),
    primaryContainer = Color(0xFF0C2B4E),
    onPrimaryContainer = Color(0xFFC3E8FF),
    secondary = CyberAccent,
    onSecondary = Color(0xFF00344D),
    secondaryContainer = Color(0xFF132A42),
    onSecondaryContainer = Color(0xFFCCE8FF),
    tertiary = ElectricBlue,
    onTertiary = Color.White,
    background = CyberNavyDark,
    onBackground = TextPrimary,
    surface = CyberNavySurface,
    onSurface = TextPrimary,
    surfaceVariant = CyberCardBg,
    onSurfaceVariant = TextSecondary,
    outline = CyberCardBorder,
    error = CriticalRed,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CyberColorScheme,
        typography = Typography,
        content = content
    )
}
