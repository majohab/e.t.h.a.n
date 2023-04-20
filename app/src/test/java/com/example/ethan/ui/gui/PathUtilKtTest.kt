package com.example.ethan.ui.gui

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner


import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import kotlin.math.abs


@RunWith(MockitoJUnitRunner::class)
class PathTest {

    @Mock
    private lateinit var path: Path
    @Test
    fun `test standardQuadFromTo`() {
        // Arrange
        val from = Offset(0f, 0f)
        val to = Offset(100f, 100f)
        val expectedX1 = 0f
        val expectedY1 = 0f
        val expectedX2 = 50f
        val expectedY2 = 50f

        // Act
        path.standardQuadFromTo(from, to)

        // Assert
        verify(path).quadraticBezierTo(
            expectedX1,
            expectedY1,
            expectedX2,
            expectedY2
        )
    }
}