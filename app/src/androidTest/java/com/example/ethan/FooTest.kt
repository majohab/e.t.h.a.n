package com.example.ethan

import org.junit.Assert.*

import com.example.ethan.ui.gui.theme.MyClassFoo


class MyClassTestFoo {
    @org.junit.Test
    fun foo() {
        var obj = MyClassFoo()
        var res = obj.foo(1)
        assertEquals(res, 43)
    }
}