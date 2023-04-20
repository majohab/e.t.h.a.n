package com.example.ethan.ui.gui

import androidx.compose.ui.graphics.Color
import com.example.ethan.usecases.GoodMorningDialogue

import org.junit.Assert.*
import org.junit.Test

class FeatureTest {

    @Test
    fun dataClassFeatureTest() {
        Feature(
            title = "a",
            lightColor = Color.LightGray,
            mediumColor = Color.Blue,
            darkColor = Color.Black,
            onClicked = null,
            useCase = GoodMorningDialogue::class
        )
        assertEquals(1,1)
    }


}