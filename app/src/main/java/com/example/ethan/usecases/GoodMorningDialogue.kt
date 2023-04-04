package com.example.ethan.usecases

import com.example.ethan.api.connectors.HoroscopeConnector
import com.example.ethan.ui.gui.Message
import com.example.ethan.ui.gui.Messaging
import com.example.ethan.ui.gui.Sender
import java.time.LocalDateTime

class GoodMorningDialogue : Thread() {
    private var horoscopeConnector = HoroscopeConnector()
    // Volatile disables caching for those variables CPU-internally -> faster execution
    @Volatile
    private var waitingForSpeech: Boolean = false
    @Volatile
    private var lastSpeechInput: String = ""

    override fun run() {
        println("GoodMorningDialogue Thread has been started!")

        // Request API 1
        val horoscopes = horoscopeConnector.get()
        // Request API 2
        // Request API 3

        val now = LocalDateTime.now()

        // Greet user with all gather information
        speak(
    "Good Morning. Today is the ${now.dayOfMonth} of ${now.month}. It is ${now.hour} o'clock and ${now.minute} minutes."
        + "You have 5 events for today"
        + "The stocks are..."
        + "This are your daily news: "
        )

        // Ask for his preferred transportation method
        askForVoiceInput("What is your favorite type of transportation for this day?")
        // Say how long it'll take the user to its destination

        println("GoodMorningDialogue Thread is about to end!")
    }

    private fun speak(text: String){
        Messaging.addMessage(Message(
            sender = Sender.ETHAN,
            text = text
        ))
    }

    private fun askForVoiceInput(question: String){
        waitingForSpeech = true
        speak(question)

        while(waitingForSpeech){}
    }

    fun onSpeechReceived(input: String)
    {
        // Displaying is handled in GUI
        lastSpeechInput = input
        waitingForSpeech = false
    }

    fun onSpeachError(error: Int){
        speak("Something wrent wrong. Please try again.")
    }
}