package com.example.ethan.usecases

import android.content.IntentSender.OnFinished
import com.example.ethan.ui.gui.Message
import com.example.ethan.ui.gui.Messaging
import com.example.ethan.ui.gui.Sender

abstract class AbstractUseCase(val onFinishedCallback: () -> Unit) : Thread(){
    // Volatile disables caching for those variables CPU-internally -> faster execution
    @Volatile
    private var waitingForSpeech: Boolean = false
    @Volatile
    private var lastSpeechInput: String = ""

    fun askForVoiceInput(question: String){
        waitingForSpeech = true
        speak(question)

        while(waitingForSpeech){}
    }

    fun onSpeechReceived(input: String)
    {
        // Displaying of USER is handled in GUI
        lastSpeechInput = input
        waitingForSpeech = false
    }

    fun onSpeachError(error: Int){
        speak("Something wrent wrong. Please try again.")
    }

    fun speak(text: String){
        Messaging.addMessage(
            Message(
            sender = Sender.ETHAN,
            text = text
        ))
    }
}