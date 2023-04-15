package com.example.ethan.ui.gui


import org.junit.Assert.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import kotlin.math.abs


import org.junit.Test

class PathUtilKtTest {

    @Test
    fun `standardQuadFromTo should add a quadratic bezier curve to the path`() {
        // arrange
        val path = Path()
        val from = Offset(x = 0f, y = 0f)
        val to = Offset(x = 100f, y = 100f)

        // act
        path.standardQuadFromTo(from, to)
        //path.quadraticBezierTo(2.0F,2.0F, 4.0F, 4.0F)
        //anpassungen für mocking nötig

        // assert
        assertEquals(10,9 )

    }
}