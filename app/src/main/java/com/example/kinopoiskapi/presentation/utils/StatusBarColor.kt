package com.example.kinopoiskapi.presentation.utils

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView

@Composable
fun SetStatusBarColor(color: Color) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        LaunchedEffect(true) {
            val window = (view.context as Activity).window
            window.statusBarColor = color.toArgb()
//            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = isSystemInDarkTheme()
        }
    }
}