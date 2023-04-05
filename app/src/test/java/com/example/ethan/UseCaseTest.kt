package com.example.ethan

import org.junit.Assert.*

import org.junit.Test

class UseCaseTest {

    @Test
    fun values() {
        var myVar = UseCase.values()
        assertEquals(1,1)
    }

    @Test
    fun valueOf() {
        var myVar = UseCase.valueOf("GoodMorningDialogue")
        assertEquals(1,1)
    }
}