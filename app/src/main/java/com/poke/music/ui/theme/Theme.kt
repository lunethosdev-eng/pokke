package com.poke.music.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightPastelScheme = lightColorScheme(
    primary = PokkePrimary,
    onPrimary = PokkeOnPrimary,
    primaryContainer = PokkePrimaryContainer,
    onPrimaryContainer = PokkeOnPrimaryContainer,
    secondary = PokkeSecondary,
    background = PokkeBackground,
    surface = PokkeSurface,
    surfaceVariant = PokkeSurfaceVariant,
    onBackground = PokkeOnSurface,
    onSurface = PokkeOnSurface,
    onSurfaceVariant = PokkeOnSurfaceVariant,
    error = PokkeError
)

@Composable
fun PokkeTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = PokkeBackground.toArgb()
            window.navigationBarColor = PokkeSurface.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }
    MaterialTheme(
        colorScheme = LightPastelScheme,
        typography = PokkeTypography,
        content = content
    )
}
