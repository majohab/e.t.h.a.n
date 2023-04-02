package com.example.ethan.usecases

import com.example.ethan.api.connectors.HoroscopeConnector
import com.example.ethan.ui.speech.Speech2Text

class GoodMorningDialogue : Thread() {
    private val horoscopeConnector = HoroscopeConnector()


    override fun run() {
        println("Executed!")
        // Request API 1
        val horoscopes = horoscopeConnector.get()
        println("Data: " + horoscopes)
        // apiConnector.getHoroscope()
        // Request API 2
        // Request API 3
        //merge information
        // talk
    }

    fun onSpeechReceived(input: String)
    {
        println("onSpeechReceived: $input")
    }
}