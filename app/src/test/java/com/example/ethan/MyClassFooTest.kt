package com.example.ethan


import com.example.ethan.ui.gui.theme.MyClassFoo
import org.junit.Assert.*

import org.junit.Test

class MyClassFooTest {

    private val sample = MyClassFoo()

    @Test
    fun foo() {
        var value1: Int = 1
        var result = sample.foo(value1)
        assertEquals(43, result)

    }
}