package com.example.ethan.usecases

import com.example.ethan.api.connectors.CalendarConnector
import com.example.ethan.api.connectors.OpenRouteConnector
import com.example.ethan.api.connectors.OpenWeatherApiConnector
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.time.LocalDateTime

class NavigationAssistance(onFinishedCallback: () -> Unit) : AbstractUseCase(onFinishedCallback)  {

    override var resTimeID = "time_NA"

    private var navigationConnector = OpenRouteConnector()
    private var weatherApiConnector = OpenWeatherApiConnector()
    private var calendarConnector = CalendarConnector()

    override fun executeUseCase() {
        //val weatherJSON = weatherApiConnector.getCurrentWeather(48.783821, 9.215519)
        //println(weatherJSON)

        var transportation_mode = "foot-walking"

        val eventsFreeBusy_json = calendarConnector.get()
        var timeToGo = 0
        val nextEventID: Int = eventsFreeBusy_json.getInt("nextEventID")
        if (nextEventID == -1) {
            return
        }
        val event = eventsFreeBusy_json.getJSONObject("events").getJSONObject(nextEventID.toString())

        timeToGo = 11//getTimeToNextEvent(event, "foot-walking")
        var routeDuration = getRouteTimeToNextEvent(event, transportation_mode)

        if(timeToGo > 15){ return }
        runBlocking { speak("Hello. This is your PDA ETHAN. I want to inform you that you need to leave in $timeToGo minutes to catch your next event.")}

        var changeQuestion = ""
        if(transportation_mode != "foot-walking"){
            routeDuration = getTimeToNextEvent(event, "foot-walking")
            if (routeDuration < 10){
                changeQuestion = "Walking to your next event would only take $routeDuration minutes. Do you want to walk instead of your current transportation method?"
            }
        }else {
            if (routeDuration > 60) {
                changeQuestion = "Your current type of transportation is set to walking. This would take more than 60 minutes as of now. Do you want to change your type of transportation for this event?"
            }
        }
        if (changeQuestion != "") {
            speakAndHearSelectiveInput(question = changeQuestion, options = listOf(
                UserInputOption(
                    tokens = positiveTokens,
                    response = "You successfully set bus as your favourite transportation method for today."
                ),
                UserInputOption(
                    tokens = negativeTokens,
                    response = "You successfully set train as your favourite transportation method for today."
                ),
            ))
        }
    }

    private fun getTimeToNextEvent(event: JSONObject, mode: String): Int{
        val hour = event.getInt("startHour")
        val minute = event.getInt("startMinute")

        val routeDurationMin = navigationConnector.getRouteDuration("48.734276, 9.110791", event.getString("location"), mode)

        val diffHour = hour - LocalDateTime.now().hour
        val diffMinute = (minute - LocalDateTime.now().minute) + (diffHour * 60)

        return diffMinute - routeDurationMin.toInt()
    }

    private fun getRouteTimeToNextEvent(event: JSONObject, mode: String): Int{
        return navigationConnector.getRouteDuration("48.734276, 9.110791", event.getString("location"), mode).toInt()
    }
}