package com.example.ethan.ui.gui

import androidx.compose.ui.graphics.Color
import com.example.ethan.UseCase

import org.junit.Assert.*
import org.junit.Test

class FeatureTest {

    @Test
    fun dataClassFeatureTest() {
        var feature = Feature(
            title = "a",
            lightColor = Color.LightGray,
            mediumColor = Color.Blue,
            darkColor = Color.Black,
            onClicked = null,
            useCase = UseCase.GoodMorningDialogue
        )
        assertEquals(1,1)
    }


}