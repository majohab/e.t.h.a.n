package com.example.ethan.ui.speech

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import androidx.compose.material.Text
import java.util.*

object Text2Speech {

    private var  textToSpeech:TextToSpeech? = null

    fun speakText(
        text: String,
        context: Context
    ) {
        println("speakText")
        textToSpeech = TextToSpeech(
            context
        ) {
            println("speakText 2")
            if (it == TextToSpeech.SUCCESS)
            {
                println("speakText 3")
                textToSpeech?.let()
                {   t2s ->
                    t2s.language = Locale.US
                    t2s.setSpeechRate(1.0f)
                    t2s.speak(
                        text,
                        TextToSpeech.QUEUE_ADD,
                        null,
                        null
                    )
                }
            }
        }
    }
}