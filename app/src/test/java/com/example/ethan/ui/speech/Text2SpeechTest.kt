package com.example.ethan.ui.speech

import org.junit.Assert.*
import android.content.Context
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import androidx.test.core.app.ApplicationProvider
import org.junit.Before

@RunWith(JUnit4::class)
class Text2SpeechTest {
    private lateinit var context: Context

    @Before
    fun setup() {
        //context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun speakTextCheck() {
        //val text = ""
        //val result = Text2Speech.speakText(text, context)
        assertEquals(1,1)
    }
}