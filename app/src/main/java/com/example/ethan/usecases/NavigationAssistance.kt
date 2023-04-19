package com.example.ethan.usecases

import com.example.ethan.LocalLocation
import com.example.ethan.api.connectors.*
import com.example.ethan.sharedprefs.SharedPrefs
import com.example.ethan.transportation.transportTranslations
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.LocalTime

class NavigationAssistance(onFinishedCallback: () -> Unit) : AbstractUseCase(onFinishedCallback) {

    override var shortForm: String = "NA"
    private val route = RouteConnector()
    private val openStreet = OpenStreetConnector()
    private var weatherApiConnector = OpenWeatherApiConnector()
    private var calendarConnector = CalendarConnector()
    private var nextExecutionTime: LocalTime? = null

    override fun executeUseCase() {
        nextExecutionTime = null
        val transportationMode = SharedPrefs.getTransportation()

        val nextEvent = getNextEvent()
        if (nextEvent == null) {
            runBlocking { speak("Congrats! You have no more events for today.") }
            onFinishedCallback()
            println("no new event")
            return
        }
        else{
            handleNextEvents(nextEvent, transportationMode)
        }
    }

    fun handleNextEvents(nextEvent: JSONObject, transportationMode: String){
        println("new event")
        val estimatedTimes = route.getDurations(nextEvent.getString("location"))
        println("got estimated time" + estimatedTimes)
        val timeWithPreferred = estimatedTimes[transportationMode]!!
        var routeDuration = timeWithPreferred
        println("got route")
        var goInXMinutes = getTimeToGo(nextEvent, timeWithPreferred)
        println("goInX" + goInXMinutes)
        if (goInXMinutes < 0) {
            println("in if go in Minutes")
            val overflow = -1 * goInXMinutes
            val bestMethod = getBestTransportMethode(estimatedTimes, transportationMode)
            val bestMethodTime = estimatedTimes[bestMethod]!!
            println("in if")
            if ((timeWithPreferred - bestMethodTime) < overflow) {
                runBlocking {
                    var suffix = ""
                    if (isRainOnRoute(nextEvent) && bestMethod == "foot-walking") {
                        suffix = "Be aware that rain is possible on your way. "
                    }
                    speak(
                        "Hello. This is your PDA ETHAN. I want to inform you that you " +
                                "needed to leave $overflow minutes ago to catch your next event. " +
                                "Your best option would be to travel by " + transportTranslations[bestMethod] + ". " +
                                "However, you will have a delay of ${overflow - (timeWithPreferred - bestMethodTime)} " +
                                "if you go right away. $suffix"
                    )
                }
            } else {
                speakAndHearSelectiveInput(
                    question = "Hello. This is your PDA ETHAN. I want to inform you that you " +
                            "needed to leave $overflow minutes ago to catch your next event. " +
                            "Your best option to be on time is to travel by " + transportTranslations[bestMethod] + ". " +
                            "Do you want to use the suggested method?",
                    options = listOf(
                        UserInputOption(
                            tokens = positiveTokens,
                            onSuccess = {
                                goInXMinutes =
                                    getTimeToGo(nextEvent, estimatedTimes[bestMethod]!!)
                                var suffix = ""
                                if (isRainOnRoute(nextEvent) && bestMethod == "foot-walking") {
                                    suffix = "Be aware that it could rain on your way. "
                                }
                                SharedPrefs.setString("transportation", bestMethod)
                                runBlocking { speak("Okay. Your updated time to leave is in $goInXMinutes minutes. $suffix ") }
                            }
                        ),
                        UserInputOption(
                            tokens = negativeTokens,
                            onSuccess = {
                                var suffix = ""
                                if (isRainOnRoute(nextEvent) && transportationMode == "foot-walking") {
                                    suffix = "Be aware that it could rain on your way. "
                                }
                                runBlocking { speak("I understand. I won't change anything. Please leave as soon as possible to minimize your delay. $suffix ") }
                            },
                        ),
                    )
                )
            }
        } else {
            println("in else in if go in Minutes")
            runBlocking { speak("Hello. This is your PDA ETHAN. I want to inform you that you need to leave in $goInXMinutes minutes to catch your next event.") }
            println("after talk in else")
            if (transportationMode != "foot-walking") {
                println("next if")
                routeDuration = estimatedTimes["foot-walking"]!!
                println("route duration " + routeDuration)
                if (routeDuration < 10) {
                    var suffix = ""
                    if (isRainOnRoute(nextEvent)) {
                        suffix = "Be aware that it could rain on your way. "
                    }
                    println("checked rain")
                    speakAndHearSelectiveInput(
                        question = "Walking to your next event would only take $routeDuration minutes. Do you want to walk instead of your current transportation method? $suffix",
                        options = listOf(
                            UserInputOption(
                                tokens = positiveTokens,
                                onSuccess = {
                                    goInXMinutes =
                                        getTimeToGo(nextEvent, estimatedTimes["foot-walking"]!!)
                                    SharedPrefs.setString("transportation", "foot-walking")
                                    runBlocking { speak("Okay. To catch the upcoming event by foot you now need to leave in $goInXMinutes minutes. ") }
                                }
                            ),
                            UserInputOption(
                                tokens = negativeTokens,
                                response = "I understand. I won't change anything. You still need to leave in $goInXMinutes minutes."
                            ),
                        ))
                }
            } else {
                println("nextElse")
                if (routeDuration > 60) {
                    speakAndHearSelectiveInput(
                        question = "Your current type of transportation is set to walking. This would take more than 60 minutes as of now. Do you want to change your type of transportation for this event? If so, please specify how you want to travel. ",
                        options = listOf(
                            UserInputOption(
                                tokens = listOf("car", "auto", "taxi"),
                                onSuccess = {
                                    goInXMinutes =
                                        getTimeToGo(nextEvent, estimatedTimes["driving-car"]!!)
                                    SharedPrefs.setString("transportation", "driving-car")
                                    runBlocking { speak("Okay. To catch the upcoming event by car you now need to leave in $goInXMinutes minutes. ") }
                                }
                            ),
                            UserInputOption(
                                tokens = listOf("wheelchair", "rollstuhl", "rollie"),
                                onSuccess = {
                                    goInXMinutes =
                                        getTimeToGo(nextEvent, estimatedTimes["wheelchair"]!!)
                                    var suffix = ""
                                    if (isRainOnRoute(nextEvent)) {
                                        suffix = "Be aware that it could rain on your way. "
                                    }
                                    SharedPrefs.setString("transportation", "wheelchair")
                                    runBlocking { speak("Okay. To catch the upcoming event by wheelchair you now need to leave in $goInXMinutes minutes. $suffix") }
                                }
                            ),
                            UserInputOption(
                                tokens = listOf("bike", "drahtesel"),
                                onSuccess = {
                                    goInXMinutes =
                                        getTimeToGo(nextEvent, estimatedTimes["cycling-road"]!!)
                                    var suffix = ""
                                    if (isRainOnRoute(nextEvent)) {
                                        suffix = "Be aware that it could rain on your way. "
                                    }
                                    SharedPrefs.setString("transportation", "cycling-road")
                                    runBlocking { speak("Okay. To catch the upcoming event by bike you now need to leave in $goInXMinutes minutes. $suffix") }
                                }
                            ),
                            UserInputOption(
                                tokens = negativeTokens,
                                onSuccess = {
                                    var suffix = ""
                                    if (isRainOnRoute(nextEvent)) {
                                        suffix = "Be aware that it could rain on your way. "
                                    }
                                    runBlocking { speak("I understand. I won't change anything. You still need to leave withinin $goInXMinutes minutes. $suffix") }
                                },
                            ),
                        )
                    )
                }
            }
        }
        if (goInXMinutes > 15) {
            runBlocking { speak("I will remind you 15 minutes before the upcoming event.") }
        }
        onFinishedCallback()
    }

    private fun getNextTimeToGo(event: JSONObject): LocalTime{
        val transportationMode = SharedPrefs.getTransportation()

        val estimatedTimes = route.getDurations(event.getString("location"))

        val timeWithPreferred = estimatedTimes[transportationMode]!!
        val timeToGo = getTimeToGo(event, timeWithPreferred)

        return LocalTime.now().plusMinutes(timeToGo.toLong())
    }

    private fun getNextEvent(): JSONObject?{
        val eventsFreeBusy_json = calendarConnector.get()

        if (eventsFreeBusy_json.getInt("total") == 0){
            return null
        }

        val nextEventID: Int = eventsFreeBusy_json.getInt("nextEventID")
        if (nextEventID == -1) {
            return null
        }

        return eventsFreeBusy_json.getJSONObject("events").getJSONObject(nextEventID.toString())
    }

    private fun isRainOnRoute(event: JSONObject): Boolean{
        val weatherDest = getWeather(event.getString("location"))
        val weatherOrig = getWeather(LocalLocation.getCurrentLocation())
        return weatherDest == "Rain" || weatherOrig == "Rain"
    }

    private fun getBestTransportMethode(estimatedTimes : Map<String, Int>, transportation_mode : String): String {
        var bestMethod = transportation_mode
        var bestMethodTime = estimatedTimes[transportation_mode]!!
        estimatedTimes.keys.forEach{
            if(bestMethodTime > (estimatedTimes[it]!!)){
                bestMethodTime = estimatedTimes[it]!!
                bestMethod = it
            }
        }
        return bestMethod
    }

    private fun getWeather(target : String): String {
        val targetLocations = openStreet.getDynamic("https://nominatim.openstreetmap.org/search/" + target + "?format=json&addressdetails=1&limit=1&polygon_svg=1")
        val weatherJSON = weatherApiConnector.getCurrentWeather(
            targetLocations.getString("lat").toDouble(),
            targetLocations.getString("lon").toDouble()
        )
        val weather = weatherJSON!!.getJSONArray("weather").getJSONObject(0)
        return weather.getString("main")
    }

    private fun getWeather(location: JSONObject): String {
        val weatherJSON = weatherApiConnector.getCurrentWeather(
            location.getString("lat").toDouble(),
            location.getString("lon").toDouble()
        )
        val weather = weatherJSON!!.getJSONArray("weather").getJSONObject(0)
        return weather.getString("main")
    }


    private fun getTimeToGo(event: JSONObject, timeInMinutes : Int) : Int {
        val hour = event.getInt("startHour")
        val minute = event.getInt("startMinute")

        val diffHour = hour - LocalDateTime.now().hour
        val diffMinute = (minute - LocalDateTime.now().minute) + (diffHour * 60)

        return diffMinute - timeInMinutes
    }

    override fun getExecutionTime(): LocalTime {
        val nextEvent = getNextEvent()
        return if (nextEvent == null){
            setDoneToday(true)
            LocalTime.parse("00:01")
        }else {
            if(nextExecutionTime == null){
                nextExecutionTime = getNextTimeToGo(nextEvent).minusMinutes(14)
                SharedPrefs.setString(getResTimeID(), nextExecutionTime.toString())
                setDoneToday(false)
            }
            nextExecutionTime!!
        }
    }

}