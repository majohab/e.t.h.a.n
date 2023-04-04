package com.example.ethan

import com.example.ethan.ui.gui.theme.MyClassFoo
import org.junit.Assert.*

import org.junit.Test

class MyClassFooTest {

    @Test
    fun foo() {
        var obj = MyClassFoo()
        var res = obj.foo(1)
        assertEquals(res, 43)
    }
}