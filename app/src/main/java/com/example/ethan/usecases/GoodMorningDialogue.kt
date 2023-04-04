package com.example.ethan.usecases

import com.example.ethan.api.connectors.HoroscopeConnector
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
        println("Good Morning. Today is the ${now.dayOfMonth} of ${now.month}. It is ${now.hour} o'clock and ${now.minute} minutes.")
        println("You have 5 events for today")
        println("The stocks are...")
        println("This are your daily news: ")

        // Ask for his preferred transportation method
        askForVoiceInput("What is your favorite type of transportation for this day?")
        println("Good Morning Input: $lastSpeechInput")
        // Say how long it'll take the user to its destination

        println("GoodMorningDialogue Thread is about to end!")
    }

    private fun askForVoiceInput(question: String){
        waitingForSpeech = true
        println(question)

        while(waitingForSpeech){}
    }

    fun onSpeechReceived(input: String)
    {
        lastSpeechInput = input
        waitingForSpeech = false
    }
}