package com.example.ethan.ui.gui

import androidx.compose.ui.graphics.Color
import com.example.ethan.usecases.AbstractUseCase
import kotlin.reflect.KClass

data class Feature(
    val useCase: KClass<out AbstractUseCase>,
    val title: String,
    val lightColor: Color,
    val mediumColor: Color,
    val darkColor: Color,
    val onClicked: (() -> Unit)?
)
