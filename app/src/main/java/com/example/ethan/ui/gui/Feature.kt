package com.example.ethan.ui.gui

import androidx.compose.ui.graphics.Color

data class Feature(
    val title: String,
    val lightColor: Color,
    val mediumColor: Color,
    val darkColor: Color,
    val onClicked: (() -> Unit)?
)
