package com.example.androidprojecttirzhanov.ui.screens

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Color.Blue,
    primaryVariant = Color.Cyan,
    secondary = Color.Green
)

@Composable
fun MyFootballAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = DarkColorPalette,
        typography = Typography,
        content = content
    )
}
