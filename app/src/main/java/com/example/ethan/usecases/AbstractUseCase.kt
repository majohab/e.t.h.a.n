package com.example.ethan.usecases

import com.example.ethan.ui.gui.Message
import com.example.ethan.ui.gui.Messaging
import com.example.ethan.ui.gui.Sender

abstract class AbstractUseCase(val onFinishedCallback: () -> Unit) : Thread(){
    // Volatile disables caching for those variables CPU-internally -> faster execution
    @Volatile
    private var awaitEthanVoiceOutput: Boolean = false
    @Volatile
    private var awaitUserVoiceInput: Boolean = false
    @Volatile
    var lastUserVoiceInput: String = ""

    fun askForUserVoiceInput(question: String){
        awaitUserVoiceInput = true
        speak(question)

        while(awaitUserVoiceInput){}
    }

    fun onUserVoiceInputReceived(input: String)
    {
        // Displaying of USER is handled in GUI
        lastUserVoiceInput = input
        awaitUserVoiceInput = false
    }

    fun onUserVoiceInputError(error: Int){
        speak("Something wrent wrong. Please try again.")
    }

    fun onEthanVoiceOutputFinished(){
        awaitEthanVoiceOutput = false
    }

    fun speak(text: String){
        awaitEthanVoiceOutput = true
        Messaging.addMessage(
            Message(
            sender = Sender.ETHAN,
            text = text
        ))
        while (awaitEthanVoiceOutput){}
    }
}