package com.example.ethan.ui.gui.theme

import androidx.compose.ui.graphics.Color
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test


class ColorTest {
    @Test
    fun OwnColorTest() {
        assertEquals(TextWhite, Color(0xffeeeeee))
        assertEquals(DeepBlue,Color(0xff06164c))
        assertEquals(ButtonBlue,Color(0xff505cf3))
        assertEquals(DarkerButtonBlue,Color(0xff566894))
        assertEquals(LightRed,Color(0xfffc879a))
        assertEquals(LightRedHalf,Color(0x88fc879a))
        assertEquals(AquaBlue,Color(0xff9aa5c4))
        assertEquals(OrangeYellow1,Color(0xfff4cf65))
        assertEquals(OrangeYellow2,Color(0xfff1c746))
        assertEquals(OrangeYellow3,Color(0xfff0bd28))
        assertEquals(Beige1,Color(0xfffdbda1))
        assertEquals(Beige2,Color(0xfffcaf90))
        assertEquals(Beige3,Color(0xfff9a27b))
        assertEquals(LightGreen1,Color(0xff54e1b6))
        assertEquals(LightGreen2,Color(0xff36ddab))
        assertEquals(LightGreen3,Color(0xff11d79b))
        assertEquals(BlueViolet1,Color(0xffaeb4fd))
        assertEquals(BlueViolet2,Color(0xff9fa5fe))
        assertEquals(BlueViolet3,Color(0xff8f98fd))
    }

}