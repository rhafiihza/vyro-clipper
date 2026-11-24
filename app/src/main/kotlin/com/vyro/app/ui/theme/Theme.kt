package com.vyro.app.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val VyroColorScheme = darkColorScheme(
    primary = VyroPrimary,
    onPrimary = VyroPhoneFrame,
    primaryContainer = VyroPrimaryGradientStart,
    onPrimaryContainer = VyroPhoneFrame,
    secondary = VyroSecondary,
    onSecondary = VyroPhoneFrame,
    tertiary = VyroAccentPurple,
    onTertiary = VyroTextPrimary,
    background = VyroBackground,
    onBackground = VyroTextPrimary,
    surface = VyroSurface,
    onSurface = VyroTextPrimary,
    surfaceVariant = VyroSurfaceAlt,
    onSurfaceVariant = VyroTextMuted,
    error = VyroDanger,
    onError = VyroTextPrimary,
)

@Composable
fun VyroTheme(content: @Composable () -> Unit) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = VyroBackground.toArgb()
            window.navigationBarColor = VyroBackground.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }
    MaterialTheme(
        colorScheme = VyroColorScheme,
        typography = VyroTypography,
        content = content,
    )
}