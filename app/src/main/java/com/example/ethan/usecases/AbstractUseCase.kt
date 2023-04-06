package com.example.ethan.usecases

import com.example.ethan.ui.gui.Message
import com.example.ethan.ui.gui.Messaging
import com.example.ethan.ui.gui.Sender
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

abstract class AbstractUseCase(val onFinishedCallback: () -> Unit) : Thread(){
    // Volatile disables caching for those variables CPU-internally -> faster execution
    @Volatile
    private var awaitEthanVoiceOutput: Boolean = false
    @Volatile
    private var awaitUserVoiceInput: Boolean = false
    @Volatile
    private var userInputWasWrong: Boolean = false
    @Volatile
    var lastUserVoiceInput: String = ""

    suspend fun askForUserVoiceInput(question: String){
        userInputWasWrong = false
        awaitUserVoiceInput = true
        runBlocking { speak(question) }
        while (awaitUserVoiceInput) {
            delay(100)
            //println("still waiting for input")
        }
        if (userInputWasWrong)
        {
            askForUserVoiceInput("Something went wrong. Please try again.")
        }
    }

    fun onUserVoiceInputReceived(input: String)
    {
        // Displaying of USER is handled in GUI
        lastUserVoiceInput = input
        awaitUserVoiceInput = false
    }

    fun onUserVoiceInputError(error: Int) {
        userInputWasWrong = true
        awaitUserVoiceInput = false
        //speak("Something went wrong. Please try again.")
    }

    fun onEthanVoiceOutputFinished(){
        awaitEthanVoiceOutput = false
    }

    suspend fun speak(text: String) {
        awaitEthanVoiceOutput = true
        Messaging.addMessage(
            Message(
            sender = Sender.ETHAN,
            text = text
        ))

        while (awaitEthanVoiceOutput) {
            delay(100)
            //println("still waiting for output to finish")
        }
    }
}