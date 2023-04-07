package com.example.ethan.ui.gui

import androidx.compose.ui.graphics.Color

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
            onClicked = null
        )
        assertEquals(1,1)
    }


}