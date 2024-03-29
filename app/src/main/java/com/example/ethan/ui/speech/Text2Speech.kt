package com.example.ethan.ui.speech

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import java.time.Instant
import java.util.*

object Text2Speech {

    private var  textToSpeech:TextToSpeech? = null

    lateinit var onFinished: () -> Unit

    var onFinished_initialized = false
        get() = field
        set(value) {
            field = value
        }

    var utteranceProgressListener = object: UtteranceProgressListener() {
        override fun onStart(p0: String?) {}

        override fun onDone(p0: String?) {
            if(onFinished_initialized) onFinished()
        }

        override fun onError(p0: String?) {}

    }

    fun setCallback(onFinished: () -> Unit)
    {
        this.onFinished = onFinished
        this.onFinished_initialized = true
    }
    fun removeCallback()
    {
        this.onFinished = {  }
        this.onFinished_initialized = false
    }

    fun speakText(
        text: String,
        context: Context
    ) {
        textToSpeech = TextToSpeech(
            context
        ) {
            if (it == TextToSpeech.SUCCESS)
            {
                textToSpeech?.let()
                {   t2s ->
                    t2s.language = Locale.US
                    t2s.setSpeechRate(1.0f)
                    t2s.setOnUtteranceProgressListener(utteranceProgressListener)
                    t2s.speak(
                        text,
                        TextToSpeech.QUEUE_ADD,
                        null,
                        Instant.now().toString()
                    )
                }
            }
        }
    }
}