package com.example.ethan.usecases

import com.example.ethan.api.connectors.HoroscopeConnector

class NavigationAssistance : Thread() {
    // private val horoscopeConnector = HoroscopeConnector()

    public override fun run(){
        print("Navigation Assistance started")
        while (true){
            execute()
        }
    }

    public fun execute(){
        
    }
}