package com.example.ethan.ui.gui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.ethan.R

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

import com.example.ethan.ui.gui.theme.Shapes as MyOwnShapes

class TypeTest {

    @Test
    fun gothicA1Test() {
        assertEquals(listOf(
            Font(R.font.gothica1_regular, FontWeight.Normal),
            Font(R.font.gothica1_medium, FontWeight.Medium),
            Font(R.font.gothica1_semibold, FontWeight.SemiBold),
            Font(R.font.gothica1_bold, FontWeight.Bold),
            Font(R.font.gothica1_black, FontWeight.Black),
        ), gothicA1)
    }

    @Test
    fun typographyTest() {
        assertEquals(Typography,
            Typography(
            body1 = TextStyle(
                color = AquaBlue,
                fontFamily = gothicA1,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            ),
            h1 = TextStyle(
                color = TextWhite,
                fontFamily = gothicA1,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            ),
            h2 = TextStyle(
                color = TextWhite,
                fontFamily = gothicA1,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp // Cursed I know...
            ),
            h3 = TextStyle(
                color = TextWhite,
                fontFamily = gothicA1,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp // Cursed I know...
            ),
            h4 = TextStyle(
                color = TextWhite,
                fontFamily = gothicA1,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp // Cursed I know...
            )
        )
        )
    }

}