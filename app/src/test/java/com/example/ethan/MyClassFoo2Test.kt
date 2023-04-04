package com.example.ethan

import com.example.ethan.ui.gui.theme.MyClassFoo
import com.example.ethan.ui.gui.theme.MyClassFoo2
import org.junit.Assert.*

import org.junit.Test

class MyClassFoo2Test {

    @Test
    fun foo() {
        var obj = MyClassFoo2()
        var res = obj.foo(1)
        assertEquals(res, 43)
    }
}