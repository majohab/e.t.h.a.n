package com.example.ethan.ui.speech

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState

object Speech2Text {

    public fun recordInput(context: Context, onFinished: (input: String) -> Unit) {

        val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)

        val intent = Intent (RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH
        )
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Something")

        // https://stackoverflow.com/questions/70688965/jetpack-compose-speech-recognition
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(bundle: Bundle?) { }
            override fun onBeginningOfSpeech() {
                println("onBeginningOfSpeech")
            }
            override fun onRmsChanged(v: Float) { }
            override fun onBufferReceived(bytes: ByteArray?) { }
            override fun onEndOfSpeech() {
                println("onEndOfSpeech")
                // changing the color of your mic icon to
                // gray to indicate it is not listening or do something you want
            }

            override fun onError(i: Int) {
                println("Error: $i")
            }

            override fun onResults(bundle: Bundle) {
                println("onResults")
                val result = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (result != null) {
                    onFinished(result[0])
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