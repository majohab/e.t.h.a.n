package com.example.ethan.usecases

import com.example.ethan.api.connectors.CalendarConnector
import com.example.ethan.api.connectors.OpenRouteConnector
import com.example.ethan.api.connectors.OpenWeatherApiConnector
import com.example.ethan.sharedprefs.SharedPrefs
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.time.LocalDateTime

class NavigationAssistance(onFinishedCallback: () -> Unit) : AbstractUseCase(onFinishedCallback)  {

    override var resTimeID = "time_NA"

    private var navigationConnector = OpenRouteConnector()
    private var weatherApiConnector = OpenWeatherApiConnector()
    private var calendarConnector = CalendarConnector()

    override fun executeUseCase() {
        val weatherJSON = weatherApiConnector.getCurrentWeather(48.783821, 9.215519)
        println(weatherJSON)

        var transportation_mode = SharedPrefs.getString("transportation", "foot-walking")
        println(transportation_mode)

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

        if(transportation_mode != "foot-walking"){
            routeDuration = getTimeToNextEvent(event, "foot-walking")
            if (routeDuration < 10){
                speakAndHearSelectiveInput(
                    question = "Walking to your next event would only take $routeDuration minutes. Do you want to walk instead of your current transportation method?", options = listOf(
                        UserInputOption(
                            tokens = positiveTokens,
                            onSuccess = {
                                timeToGo = getTimeToNextEvent(event, "foot-walking")
                                runBlocking {speak("Okay. To catch the upcoming event by foot you now need to leave in $timeToGo minutes. ")}
                            }
                        ),
                        UserInputOption(
                            tokens = negativeTokens,
                            response = "I understand. I won't change anything. You still need to leave in $timeToGo minutes."
                        ),
                    ))
            }
        }else {
            if (routeDuration > 60) {
                speakAndHearSelectiveInput(
                    question = "Your current type of transportation is set to walking. This would take more than 60 minutes as of now. Do you want to change your type of transportation for this event? If so, please specify how you want to travel. ", options = listOf(
                        UserInputOption(
                            tokens = listOf("car", "auto", "taxi"),
                            onSuccess = {
                                timeToGo = getTimeToNextEvent(event, "driving-car")
                                runBlocking {speak("Okay. To catch the upcoming event by car you now need to leave in $timeToGo minutes. ")}
                            }
                        ),
                        UserInputOption(
                            tokens = listOf("wheelchair", "rollstuhl", "rollie"),
                            onSuccess = {
                                timeToGo = getTimeToNextEvent(event, "wheelchair")
                                runBlocking {speak("Okay. To catch the upcoming event by wheelchair you now need to leave in $timeToGo minutes. ")}
                            }
                        ),
                        UserInputOption(
                            tokens = listOf("bike", "drahtesel"),
                            onSuccess = {
                                timeToGo = getTimeToNextEvent(event, "cycling-road")
                                runBlocking {speak("Okay. To catch the upcoming event by bike you now need to leave in $timeToGo minutes. ")}
                            }
                        ),
                        UserInputOption(
                            tokens = negativeTokens,
                            response = "I understand. I won't change anything. You still need to leave withinin $timeToGo minutes. "
                        ),
                    ))
            }
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

    private fun checkWeather(){

    }
}