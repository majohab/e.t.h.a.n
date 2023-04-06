package com.example.ethan.ui.gui

import androidx.compose.ui.graphics.Color
import com.example.ethan.UseCase

data class Feature(
    val useCase: UseCase,
    val title: String,
    val lightColor: Color,
    val mediumColor: Color,
    val darkColor: Color,
    val onClicked: (() -> Unit)?
)
