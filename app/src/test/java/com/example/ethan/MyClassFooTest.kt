package com.example.ethan


import com.example.ethan.ui.gui.theme.MyClassFoo
import org.junit.Assert.*

import org.junit.Test

class MyClassFooTest {

    @Test
    fun foo() {
        var value1: Int = 1
        var result = MyClassFoo.foo(value1)
        assertEquals(value1, result)

    }
}