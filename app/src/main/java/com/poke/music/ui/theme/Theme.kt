package com.poke.music.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = PokkePrimary,
    onPrimary = PokkeOnPrimary,
    primaryContainer = PokkePrimary.copy(alpha = 0.2f),
    secondary = PokkeSecondary,
    onSecondary = Color.White,
    tertiary = PokkeTertiary,
    background = PokkeBlack,
    surface = PokkeSurface,
    surfaceVariant = PokkeSurfaceVariant,
    onBackground = PokkeOnSurface,
    onSurface = PokkeOnSurface,
    onSurfaceVariant = PokkeOnSurfaceVariant,
    error = PokkeError,
    outline = PokkeGlassBorder
)

private val LightColorScheme = lightColorScheme(
    primary = PokkePrimary,
    onPrimary = PokkeOnPrimary,
    primaryContainer = PokkePrimary.copy(alpha = 0.15f),
    secondary = PokkeSecondary,
    onSecondary = Color.White,
    tertiary = PokkeTertiary,
    background = PokkeLightBackground,
    surface = PokkeLightSurface,
    surfaceVariant = Color(0xFFF0F0F0),
    onBackground = PokkeLightOnSurface,
    onSurface = PokkeLightOnSurface,
    onSurfaceVariant = Color(0xFF555555),
    error = PokkeError
)

@Composable
fun PokkeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }
    MaterialTheme(colorScheme = colorScheme, typography = PokkeTypography, content = content)
}
