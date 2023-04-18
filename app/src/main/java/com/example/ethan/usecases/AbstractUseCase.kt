package com.example.ethan.usecases

import android.os.Build
import com.example.ethan.sharedprefs.SharedPrefs
import com.example.ethan.ui.gui.Message
import com.example.ethan.ui.gui.Messaging
import com.example.ethan.ui.gui.Sender
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
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

    abstract var shortForm: String

    var positiveTokens = listOf("yes", "yeah", "yep", "yup", "sure")
    var negativeTokens = listOf("no", "nah", "no way", "never", "save nicht")

    abstract fun getExecutionTime() : LocalTime

    fun getDoneToday() : Boolean {
        return SharedPrefs.getBoolean(doneTodayString())
    }

    fun setDoneToday(bool: Boolean = true) {
        SharedPrefs.setBoolean(doneTodayString(), bool)
    }

    fun getResTimeID() : String {
        return "time_$shortForm"
    }

    fun doneTodayString() : String {
        val now = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate.now()
        } else {
            TODO("VERSION.SDK_INT < O")
        }

        return shortForm + "_"+ now.dayOfMonth + ":" + now.month + ":" + now.year
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

        var success: Boolean = false
        while (!success) {
            for (option in options) {
                if (checkIfContainsWord(option.tokens)) {
                    success = true
                    if(option.response != null){
                        runBlocking { speak(option.response!!) }
                    }
                    option.onSuccess?.invoke()
                    break
                }
            }

            if (!success)
                runBlocking { askForUserVoiceInput("I didn't quite catch that, please repeat your response.") }
        }
    }

    fun dynamicOptions(tokens: List<String>, onSuccess: ((String) -> Unit)) : List<UserInputOption>{
        val options = mutableListOf<UserInputOption>()
        for (i in tokens.indices) {
            val option = UserInputOption(
                tokens = listOf(tokens[i]),
                onSuccess = {
                    onSuccess(tokens[i])
                }
            )
            options.add(option)
        }
        return options
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

class UserInputOption(var tokens: List<String>, var response: String? = null, var onSuccess: (() -> Unit)? = null)