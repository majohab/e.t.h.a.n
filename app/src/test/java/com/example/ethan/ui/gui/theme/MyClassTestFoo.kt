package com.example.ethan

import com.example.ethan.ui.gui.theme.MyClassFoo
import org.junit.Test

import org.junit.Assert.*

    class MyClassTestFoo {
        @org.junit.Test
        fun foo() {
            var obj = MyClassFoo()
            var res = obj.foo(1)
            assertEquals(res, 43)
        }
    }
