package com.example.ethan.usecases

import com.example.ethan.api.connectors.HoroscopeConnector
import com.example.ethan.ui.gui.Message
import com.example.ethan.ui.gui.Messaging
import com.example.ethan.ui.gui.Sender
import java.time.LocalDateTime

class GoodMorningDialogue : AbstractUseCase() {
    private var horoscopeConnector = HoroscopeConnector()

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


}