package com.example.ethan

import org.junit.Assert.*

import com.example.ethan.ui.gui.theme.MyClassFoo2


class MyClassTestFoo2 {
    @org.junit.Test
    fun foo2() {
        var obj = MyClassFoo2()
        var res = obj.foo(1)
        assertEquals(res, 43)
    }
}