package com.example.ethan.usecases

import com.example.ethan.api.connectors.CalendarConnector

class NavigationAssistance : Thread() {
    private val calendarConnector = CalendarConnector()

    public override fun run(){
        print("Navigation Assistance started")
        while (true){
            execute()
            Thread.sleep(1000 * 10)
        }
    }

    public fun execute(){
        calendarConnector.execute();
    }
}