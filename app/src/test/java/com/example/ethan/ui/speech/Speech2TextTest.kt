package com.example.ethan.ui.speech

import org.junit.Assert.*
import org.junit.Before

import org.junit.Test


class Speech2TextTest {

    private lateinit var speech2text: Speech2Text

    @Before
    fun setUp(){
        speech2text = Speech2Text
    }

    @Test
    fun getOnFinished_backend() {

    }

    @Test
    fun setOnFinished_backend() {
    }

    @Test
    fun getOnError_backend() {
    }

    @Test
    fun setOnError_backend() {
    }

    @Test
    fun getOnFinished_backend_initialized() {
    }

    @Test
    fun setOnFinished_backend_initialized() {
    }

    @Test
    fun getOnError_backend_initialized() {
    }

    @Test
    fun setOnError_backend_initialized() {
    }

    @Test
    fun setCallback() {
    }

    @Test
    fun removeCallback() {
        speech2text.onFinished_backend_initialized = true
        speech2text.removeCallback()
        var return_value = speech2text.onFinished_backend_initialized
        assertEquals(false,return_value)
    }

    @Test
    fun setErrorCallback() {
    }

    @Test
    fun removeErrorCallback() {
    }

    @Test
    fun recordInput() {
    }
}