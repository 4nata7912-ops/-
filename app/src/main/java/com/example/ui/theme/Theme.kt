package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.model.AccentPalette
import com.example.model.ThemeMode

@Composable
fun ZorkaTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    accentPalette: AccentPalette = AccentPalette.CORNFLOWER,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = accentPalette.darkPrimary,
            onPrimary = Color(0xFF001E36),
            primaryContainer = accentPalette.darkPrimary.copy(alpha = 0.2f),
            onPrimaryContainer = accentPalette.darkPrimary,
            secondary = accentPalette.darkSecondary,
            onSecondary = Color(0xFF001E36),
            tertiary = accentPalette.darkTertiary,
            background = Color(0xFF0F141C),
            surface = Color(0xFF161E2A),
            surfaceVariant = Color(0xFF222B38),
            onBackground = Color(0xFFE2E6EE),
            onSurface = Color(0xFFE2E6EE),
            onSurfaceVariant = Color(0xFFC0C7D5)
        )
    } else {
        lightColorScheme(
            primary = accentPalette.lightPrimary,
            onPrimary = Color.White,
            primaryContainer = accentPalette.lightPrimary.copy(alpha = 0.12f),
            onPrimaryContainer = accentPalette.lightPrimary,
            secondary = accentPalette.lightSecondary,
            onSecondary = Color.White,
            tertiary = accentPalette.lightTertiary,
            background = Color(0xFFF7F9FC),
            surface = Color(0xFFFFFFFF),
            surfaceVariant = Color(0xFFEDF2F7),
            onBackground = Color(0xFF191C20),
            onSurface = Color(0xFF191C20),
            onSurfaceVariant = Color(0xFF43474E)
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
