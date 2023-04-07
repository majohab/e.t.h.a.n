package com.example.ethan.ui.speech

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class Text2SpeechTest{

    private lateinit var text2speech: Text2Speech

    @Before
    fun setUp(){
        text2speech = Text2Speech
    }

    @Test
    fun set_and_get_onFinished_initialized(){
        text2speech.onFinished_initialized = true
        assertEquals(true, text2speech.onFinished_initialized)
    }

    @Test
    fun overritten_utteranceProgressListener(){
        text2speech.utteranceProgressListener.onStart("Start")
        text2speech.utteranceProgressListener.onDone("Done")
        text2speech.utteranceProgressListener.onError("Error")
        //Test if methods can be called
        assertEquals(1,1)
    }

    @Test
    fun removeCallbackTest(){
        text2speech.onFinished_initialized = true
        text2speech.removeCallback()
        assertEquals(false, text2speech.onFinished_initialized)
    }

    @Test
    fun setCallbackTest(){
        text2speech.onFinished_initialized = false
        text2speech.setCallback {  }
        assertEquals(true, text2speech.onFinished_initialized)
    }
}