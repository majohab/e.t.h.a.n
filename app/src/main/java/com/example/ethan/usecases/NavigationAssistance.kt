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
        val transportationMode = SharedPrefs.getTransportation()

        val nextEvent = getNextEvent()
        if (nextEvent == null) {
            println("no new events")
            runBlocking { speak("Congrats! You have no more events for today.") }
            onFinishedCallback()
            println("no new event")
            setDoneToday()
            return
        }
        else {
            handleNextEvents(nextEvent, transportationMode)

            // Evaluate nextExecutionTime
            val eventsFreeBusy_json = calendarConnector.get()
            val events = eventsFreeBusy_json.getJSONObject("events")
            for (i in 1 .. events.length()) {
                var compareEvent = events.getJSONObject(i.toString())
                if (nextEvent.getString("startHour") == compareEvent.getString("startHour") &&
                    nextEvent.getString("startMinute") == compareEvent.getString("startMinute")) {
                    if (i == events.length()) {
                        setDoneToday()
                    }
                    else {
                        val nextNextEvent = events.getJSONObject((i+1).toString())
                        val executionTime = getReminderTime(nextNextEvent)
                        setExecutionTime(executionTime)
                    }
                }
            }

            return
        }
    }

    fun handleNextEvents(nextEvent: JSONObject, transportationMode: String){
        val estimatedTimes = route.getDurations(nextEvent.getString("location"))
        val timeWithPreferred = estimatedTimes[transportationMode]!!
        var routeDuration = timeWithPreferred
        var goInXMinutes = getTimeToGo(nextEvent, timeWithPreferred)

        if (goInXMinutes < 0) {
            // Need to leave in the past
            val overflow = -1 * goInXMinutes
            val bestMethod = getBestTransportMethode(estimatedTimes, transportationMode)
            val bestMethodTime = estimatedTimes[bestMethod]!!

            if ((timeWithPreferred - bestMethodTime) < overflow) {
                // Still too late to catch next event
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
                goInXMinutes = bestTooLateAlternative(overflow, goInXMinutes, bestMethod, nextEvent, transportationMode, estimatedTimes)

            }
        } else {
            runBlocking { speak("Hello. This is your PDA ETHAN. I want to inform you that you need to leave in $goInXMinutes minutes to catch your next event.") }

            if (transportationMode != "foot-walking") {
                routeDuration = estimatedTimes["foot-walking"]!!
                if (routeDuration < 10) {
                    // Ask user if he wants to walk the short distance
                    var suffix = ""
                    if (isRainOnRoute(nextEvent)) {
                        suffix = "Be aware that it could rain on your way. "
                    }
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
                if (routeDuration > 60) {
                    // User wants to walk, but that takes more than 60 minutes. Ask him for a change
                    goInXMinutes = footWalkingAlternative(nextEvent, estimatedTimes, goInXMinutes)
                }
            }
        }
        //if (goInXMinutes > 15) {
        //    // New going time > 15 minutes? -> Remind him again
        //    runBlocking { speak("I will remind you 15 minutes before the upcoming event.") }
        //}
        onFinishedCallback()
    }

    fun getNextTimeToGo(event: JSONObject): LocalTime{
        val transportationMode = SharedPrefs.getTransportation()

        val estimatedTimes = route.getDurations(event.getString("location"))

        val timeWithPreferred = estimatedTimes[transportationMode]!!
        val timeToGo = getTimeToGo(event, timeWithPreferred)

        return LocalTime.now().plusMinutes(timeToGo.toLong())
    }

    fun getReminderTime(event: JSONObject) : LocalTime {
        return getNextTimeToGo(event).minusMinutes(15)
    }

    fun footWalkingAlternative(nextEvent: JSONObject, estimatedTimes: Map<String, Int>, goInXMinutes: Int): Int {
        var goInXMinutesFunction = goInXMinutes
        speakAndHearSelectiveInput(
            question = "Your current type of transportation is set to walking. This would take more than 60 minutes as of now. Do you want to change your type of transportation for this event? If so, please specify how you want to travel. ",
            options = listOf(
                UserInputOption(
                    tokens = listOf("car", "auto", "taxi"),
                    onSuccess = {
                        goInXMinutesFunction =
                            getTimeToGo(nextEvent, estimatedTimes["driving-car"]!!)
                        SharedPrefs.setString("transportation", "driving-car")
                        runBlocking { speak("Okay. To catch the upcoming event by car you now need to leave in $goInXMinutesFunction minutes. ") }
                    }
                ),
                UserInputOption(
                    tokens = listOf("wheelchair", "rollstuhl", "rollie"),
                    onSuccess = {
                        goInXMinutesFunction =
                            getTimeToGo(nextEvent, estimatedTimes["wheelchair"]!!)
                        var suffix = ""
                        if (isRainOnRoute(nextEvent)) {
                            suffix = "Be aware that it could rain on your way. "
                        }
                        SharedPrefs.setString("transportation", "wheelchair")
                        runBlocking { speak("Okay. To catch the upcoming event by wheelchair you now need to leave in $goInXMinutesFunction minutes. $suffix") }
                    }
                ),
                UserInputOption(
                    tokens = listOf("bike", "drahtesel"),
                    onSuccess = {
                        goInXMinutesFunction =
                            getTimeToGo(nextEvent, estimatedTimes["cycling-road"]!!)
                        var suffix = ""
                        if (isRainOnRoute(nextEvent)) {
                            suffix = "Be aware that it could rain on your way. "
                        }
                        SharedPrefs.setString("transportation", "cycling-road")
                        runBlocking { speak("Okay. To catch the upcoming event by bike you now need to leave in $goInXMinutesFunction minutes. $suffix") }
                    }
                ),
                UserInputOption(
                    tokens = negativeTokens,
                    onSuccess = {
                        var suffix = ""
                        if (isRainOnRoute(nextEvent)) {
                            suffix = "Be aware that it could rain on your way. "
                        }
                        runBlocking { speak("I understand. I won't change anything. You still need to leave withinin $goInXMinutesFunction minutes. $suffix") }
                    },
                ),
            )
        )
        return goInXMinutesFunction
    }


    fun bestTooLateAlternative(overflow: Int, goInXMinutes: Int, bestMethod: String, nextEvent: JSONObject, transportationMode: String, estimatedTimes: Map<String, Int>): Int {
        var goInXMinutesFunction = goInXMinutes
        speakAndHearSelectiveInput(
            question = "Hello. This is your PDA ETHAN. I want to inform you that you " +
                    "needed to leave $overflow minutes ago to catch your next event. " +
                    "Your best option to be on time is to travel by " + transportTranslations[bestMethod] + ". " +
                    "Do you want to use the suggested method?",
            options = listOf(
                UserInputOption(
                    tokens = positiveTokens,
                    onSuccess = {
                        goInXMinutesFunction =
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
        return goInXMinutesFunction
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

    // Overloaded Function
    private fun getWeather(target : String): String {
        val targetLocations = openStreet.getDynamic("https://nominatim.openstreetmap.org/search/" + target + "?format=json&addressdetails=1&limit=1&polygon_svg=1")
        val weatherJSON = weatherApiConnector.getCurrentWeather(
            targetLocations.getString("lat").toDouble(),
            targetLocations.getString("lon").toDouble()
        )
        val weather = weatherJSON!!.getJSONArray("weather").getJSONObject(0)
        return weather.getString("main")
    }

    // Overloaded Function
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
        if (nextEvent == null) {
            setDoneToday()
            if(nextExecutionTime == null) {
                nextExecutionTime = LocalTime.of(23, 59) // Doesn't matter, won't execute when setDone == true
            }
        } else if(nextExecutionTime == null) {
            setExecutionTime(getReminderTime(nextEvent))
        }
        return nextExecutionTime!!
    }

    fun setExecutionTime(time: LocalTime) {
        nextExecutionTime = time
        SharedPrefs.setString(getResTimeID(), time.toString())
        setDoneToday(false)
    }
}