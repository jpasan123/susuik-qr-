package com.example.pos.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

enum class WindowSize {
    COMPACT, MEDIUM, EXPANDED
}

@Composable
fun rememberWindowSize(): WindowSize {
    val configuration = LocalConfiguration.current
    return when {
        configuration.screenWidthDp < 600 -> WindowSize.COMPACT
        configuration.screenWidthDp < 840 -> WindowSize.MEDIUM
        else -> WindowSize.EXPANDED
    }
}