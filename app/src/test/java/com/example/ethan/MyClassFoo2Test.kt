package com.example.ethan

import com.example.ethan.ui.gui.theme.MyClassFoo
import com.example.ethan.ui.gui.theme.MyClassFoo2
import org.junit.Assert.*

import org.junit.Test

class MyClassFoo2Test {

    private val sample = MyClassFoo2()

    @Test
    fun foo() {
        var value1: Int = 1
        var result = sample.foo(value1)
        assertEquals(43, result)

    }
}