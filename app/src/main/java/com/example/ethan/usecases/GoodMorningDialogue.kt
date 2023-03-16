package com.example.ethan.usecases

import com.example.ethan.api.connectors.HoroscopeConnector

class GoodMorningDialogue : Thread(){
    private val horoscopeConnector = HoroscopeConnector()

    public override fun run(){
        print("Thread started")
        while (true){
            execute()
            Thread.sleep(1000 * 10)
        }
    }

    public fun execute(){
        print("Executed!")
        // Request API 1
        val horoscopes = horoscopeConnector.get()
        print("Data: " + horoscopes)
        // apiConnector.getHoroscope()
        // Request API 2
        // Request API 3
        //merge information
        // talk
    }
}