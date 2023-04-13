package com.example.ethan.usecases

import android.os.Build
import com.example.ethan.Preferences
import com.example.ethan.ui.gui.Message
import com.example.ethan.ui.gui.Messaging
import com.example.ethan.ui.gui.Sender
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.time.LocalTime

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

    abstract var resTimeID: String

    var positiveTokens = listOf("yes", "yeah", "yep", "yup", "sure")
    var negativeTokens = listOf("no", "nah", "no way", "never", "save nicht")

    fun getExecutionTime() : LocalTime {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalTime.parse(Preferences.get(resTimeID))
        } else {
            TODO("VERSION.SDK_INT < O")
        }
    }

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

    fun speakAndHearSelectiveInput(question: String, options: List<UserInputOption>) {

        runBlocking { askForUserVoiceInput(question) }

        var response: String? = null
        while (response == null) {
            for (option in options) {
                if (checkIfContainsWord(option.tokens)) {
                    response = option.response
                }
            }

            if (response == null)
                runBlocking { speak("I didn't quite catch that, please repeat your response.") }
        }

        runBlocking { speak(response) }
    }

    fun checkIfContainsWord(vararg tokens: String) : Boolean {
        return checkIfContainsWord(tokens.asList())
    }

    private fun checkIfContainsWord(tokens: List<String>) : Boolean {
        var input = lastUserVoiceInput.lowercase()
        for (token in tokens) {
            if (input.contains(token.lowercase()))
                return true
        }
        return false
    }

    fun checkIfPositive() : Boolean {
        return checkIfContainsWord(positiveTokens)
    }

    fun checkIfNegative() : Boolean {
        return checkIfContainsWord(negativeTokens)
    }
}

class UserInputOption(var tokens: List<String>, var response: String)