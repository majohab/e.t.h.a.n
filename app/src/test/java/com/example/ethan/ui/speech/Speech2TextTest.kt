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
    fun get_onFinished_backend_initialized() {
        speech2text.onFinished_backend_initialized = false
        assertEquals(false, speech2text.onFinished_backend_initialized)
    }

    @Test
    fun get_onError_backend_initialized() {
        speech2text.onError_backend_initialized = false
        assertEquals(false, speech2text.onFinished_backend_initialized)
    }

    @Test
    fun initialization_onFinsihed_backend_initialized(){
        assertEquals(true, speech2text.onError_backend_initialized)
    }

    @Test
    fun initialization_onError_backend_initialized(){
        assertEquals(false, speech2text.onFinished_backend_initialized )
    }

    @Test
    fun setCallback() {
        speech2text.onFinished_backend_initialized = true
        speech2text.setCallback {  }
        var returnValue = speech2text.onFinished_backend_initialized
        assertEquals(true, returnValue)
    }

    @Test
    fun removeCallback() {
        speech2text.onFinished_backend_initialized = true
        speech2text.removeCallback()
        assertEquals(false,speech2text.onFinished_backend_initialized)
    }

    @Test
    fun setErrorCallback() {
        speech2text.onError_backend_initialized = false
        speech2text.setErrorCallback {  }
        assertEquals(true, speech2text.onError_backend_initialized)
    }

    @Test
    fun removeErrorCallback() {
        speech2text.onError_backend_initialized = true
        speech2text.removeErrorCallback()
        assertEquals(false,speech2text.onError_backend_initialized)
    }

    @Test
    fun recordInput() {
    }
}