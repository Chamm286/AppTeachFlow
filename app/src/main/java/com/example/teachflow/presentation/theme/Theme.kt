package com.example.teachflow.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Màu sắc chủ đạo - Xanh dương chuyên nghiệp
val PrimaryBlue = Color(0xFF1A73E8)
val PrimaryDark = Color(0xFF0D47A1)
val PrimaryLight = Color(0xFF4285F4)
val AccentGreen = Color(0xFF34A853)
val AccentYellow = Color(0xFFFBBC05)
val AccentRed = Color(0xFFEA4335)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryLight,
    secondary = AccentGreen,
    tertiary = AccentYellow,
    background = Color(0xFF202124),
    surface = Color(0xFF303134),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    secondary = AccentGreen,
    tertiary = AccentYellow,
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF202124),
    onSurface = Color(0xFF202124)
)

@Composable
fun TeachFlowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}
