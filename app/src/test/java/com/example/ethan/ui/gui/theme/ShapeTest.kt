package com.example.ethan.ui.gui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.ui.unit.dp

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

import com.example.ethan.ui.gui.theme.Shapes as MyOwnShapes

class ShapeTest {

    private lateinit var shape: Shapes

    @Before
    fun setUp() {
        shape = Shapes(
            small = RoundedCornerShape(4.dp),
            medium = RoundedCornerShape(4.dp),
            large = RoundedCornerShape(0.dp)
        )
    }

    @Test
    fun OwnShapeTest() {
        assertEquals(shape, MyOwnShapes)
    }

}