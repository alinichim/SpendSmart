package com.example.spendsmart.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = Violet600,
    onPrimary = NeutralSurface,
    primaryContainer = Violet100,
    onPrimaryContainer = Violet600,
    secondary = Purple700,
    onSecondary = NeutralSurface,
    background = NeutralBackground,
    onBackground = NeutralOnSurface,
    surface = NeutralSurface,
    onSurface = NeutralOnSurface,
    surfaceVariant = NeutralSurfaceVariant,
    onSurfaceVariant = NeutralOnSurfaceMuted,
    outline = NeutralBorder,
    error = Danger,
    onError = NeutralSurface
)

private val DarkColors = darkColorScheme(
    primary = Violet500,
    onPrimary = NeutralSurface,
    primaryContainer = Violet600,
    onPrimaryContainer = NeutralSurface,
    secondary = Purple700,
    onSecondary = NeutralSurface,
    background = DarkBackground,
    onBackground = DarkOnSurface,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceMuted,
    outline = DarkBorder,
    error = Danger,
    onError = NeutralSurface
)

@Composable
fun SpendSmartTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        shapes = SpendSmartShapes,
        content = content
    )
}
