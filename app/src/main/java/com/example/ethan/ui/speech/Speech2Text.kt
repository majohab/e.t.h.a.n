package com.example.ethan.ui.speech

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import com.example.ethan.ui.gui.Message
import com.example.ethan.ui.gui.Messaging
import com.example.ethan.ui.gui.Sender

object Speech2Text {

    lateinit var onFinished_backend: (input: String) -> Unit
    lateinit var onError_backend: (error: Int) -> Unit

    var onFinished_backend_initialized = false
        get() = field
        set(value) {
            field = value
        }

    var onError_backend_initialized = false
        get() = field
        set(value) {
            field = value
        }

    fun setCallback(onFinished_backend: (input: String) -> Unit)
    {
        // set the onFinshed_backend var to onFinished_backend
        this.onFinished_backend = onFinished_backend
        this.onFinished_backend_initialized = true
    }
    fun removeCallback()
    {
        this.onFinished_backend = {  }
        this.onFinished_backend_initialized = false
    }

    fun setErrorCallback(onError_backend: (error: Int) -> Unit)
    {
        this.onError_backend = onError_backend
        this.onError_backend_initialized = true
    }
    fun removeErrorCallback()
    {
        this.onError_backend = {  }
        this.onError_backend_initialized = false
    }

    fun recordInput(
        context: Context,
        onStart: () -> Unit,
        onRmsChanged: (value: Float) -> Unit,
        onFinished_Frontend: (input: String) -> Unit,
        onError_Frontend: (input: Int) -> Unit
    ) {
        val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)

        val intent = Intent (RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH)
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Something")

        // https://stackoverflow.com/questions/70688965/jetpack-compose-speech-recognition
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(bundle: Bundle?) { onStart() }
            override fun onBeginningOfSpeech() { }
            override fun onRmsChanged(v: Float) { onRmsChanged(v) }
            override fun onBufferReceived(bytes: ByteArray?) { println("onBufferReceived") }
            override fun onEndOfSpeech() { println("onEndOfSpeech")
                // changing the color of your mic icon to
                // gray to indicate it is not listening or do something you want
            }

            override fun onError(i: Int) {
                println("Error: $i")
                if (onError_backend_initialized) onError_backend(i)
                onError_Frontend(i)
            }

            override fun onResults(bundle: Bundle) {
                println("onResults")
                val result = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (result != null) {
                    onFinished_Frontend(result[0])
                    if (onFinished_backend_initialized) {
                        onFinished_backend(result[0])
                    }else {
                        Messaging.addMessage(
                            Message(
                                sender = Sender.ETHAN,
                                text = "Currently no UseCase is active. Please start an UseCase and try again."
                            ))
                    }
                }
            }

            override fun onPartialResults(bundle: Bundle) {
                println("onPartialResults")
            }
            override fun onEvent(i: Int, bundle: Bundle?) {}

        })
        speechRecognizer.startListening(intent)

        //resultLauncher.launch(intent)
        //startActivityForResult(intent, 101)
    }
}