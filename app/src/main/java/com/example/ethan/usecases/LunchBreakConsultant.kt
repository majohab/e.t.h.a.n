package com.example.ethan.usecases

import com.example.ethan.api.connectors.CalendarConnector
import com.example.ethan.api.connectors.OpenStreetMapApi

class LunchBreakConsultant(onFinishedCallback: () -> Unit) : AbstractUseCase(onFinishedCallback)  {

    override var resTimeID = "time_LBC"
    private var openStreetMapRestaurant = OpenStreetMapApi()
    private var calendarConnector = CalendarConnector()

    override fun executeUseCase() {
        var breakTime = 12
        val timeOptions = mutableListOf<UserInputOption>()
        for (i in 0..23) {
            val option = UserInputOption(
                tokens = listOf(i.toString()),
                onSuccess = {
                    breakTime = i
                    println("") // DO NOT DELETE THIS LINE
                }
            )
            timeOptions.add(option)
        }
        speakAndHearSelectiveInput(
            question = "Hi. I'm here to assure you having the best break today. Around what hour do" +
                    " prefer to eat something?", options = timeOptions
        )

        val eventsFreeBusy_json = calendarConnector.get()
        val eventsTotal = eventsFreeBusy_json.getInt("total")

        if (eventsTotal == 0){
            // Preferred time is available
        }else {
            eventsFreeBusy_json.getJSONObject("events").keys().forEach {

            }
        }

        val restaurants = openStreetMapRestaurant.findNearestRestaurants(37.7749, -122.4194, 1000 , "italian")
        println(restaurants)

        // get time slots >x minutes
        // suggest restaurants that are in time
    }
}