package com.example.ethan.usecases

import com.example.ethan.ui.gui.Message
import com.example.ethan.ui.gui.Messaging
import com.example.ethan.ui.gui.Sender
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime

abstract class AbstractUseCase(val onFinishedCallback: () -> Unit) {
    // Volatile disables caching for those variables CPU-internally -> faster execution
    @Volatile
    private var awaitEthanVoiceOutput: Boolean = false
    @Volatile
    private var awaitUserVoiceInput: Boolean = false
    @Volatile
    private var userInputWasWrong: Boolean = false
    @Volatile
    var lastUserVoiceInput: String = ""

    abstract fun getExecutionTime() : LocalDateTime

    abstract fun initUseCase()

    abstract fun executeUseCase()

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

    fun checkIfContainsWord(input: String, vararg tokens: String) : Boolean {
        var input = input.lowercase()
        for (token in tokens) {
            if (input.contains(token.lowercase()))
                return true
        }
        return false
    }

    fun checkIfPositive(input: String) : Boolean {
        return checkIfContainsWord(input, "yes", "yeah", "yep", "yup", "sure")
    }

    fun checkIfNegative(input: String) : Boolean {
        return checkIfContainsWord(input, "no", "nah", "no way", "never", "save nicht")
    }
}