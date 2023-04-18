package com.example.ethan.usecases

import com.example.ethan.BuildConfig
import com.example.ethan.LocalLocation
import com.example.ethan.api.connectors.*
import com.example.ethan.sharedprefs.SharedPrefs
import com.example.ethan.transportation.getAllTransportationKeys
import com.example.ethan.transportation.transportTranslations
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.time.LocalDateTime

class NavigationAssistance(onFinishedCallback: () -> Unit) : AbstractUseCase(onFinishedCallback) {

    override var shortForm: String = "NA"
    private val route = RouteConnector()
    private val openStreet = OpenStreetConnector()
    private var weatherApiConnector = OpenWeatherApiConnector()
    private var calendarConnector = CalendarConnector()

    override fun executeUseCase() {
        var transportation_mode = SharedPrefs.getTransportation()

        val eventsFreeBusy_json = calendarConnector.get()
        var timeToGo = 0
        val nextEventID: Int = eventsFreeBusy_json.getInt("nextEventID")
        if (nextEventID == -1) {
            return
        }
        val event =
            eventsFreeBusy_json.getJSONObject("events").getJSONObject(nextEventID.toString())

        val estimatedTimes = getDurations(event.getString("location"))

        val timeWithPreffered = estimatedTimes[transportation_mode]!!
        timeToGo = getTimeToGo(event, timeWithPreffered)
        var routeDuration = timeWithPreffered

        val weather_dest = getWeather(event.getString("location"))
        val weather_orig = getWeather(LocalLocation.getCurrentLocation())
        val raining: Boolean = weather_dest == "Rain" || weather_orig == "Rain"

        if (timeToGo > 15) {
            return
        }
        if (timeToGo < 0){
            val overflow = -1*timeToGo
            val bestMethod = getBestTransportMethode(estimatedTimes, transportation_mode)
            var bestMethodTime = estimatedTimes[bestMethod]!!

            if ((timeWithPreffered - bestMethodTime) < overflow){
                runBlocking {
                    var suffix = ""
                    if (raining && bestMethod == "foot-walking"){
                        suffix = "Be aware that rain is possible on your way. "
                    }
                    speak("Hello. This is your PDA ETHAN. I want to inform you that you " +
                        "needed to leave $overflow minutes ago to catch your next event. " +
                        "Your best option would be to travel by " + transportTranslations[bestMethod] + ". " +
                        "However, you still have a delay of ${overflow - (timeWithPreffered - bestMethodTime)} " +
                        "if you go right away. $suffix") }
            }else {
                speakAndHearSelectiveInput(
                    question = "Hello. This is your PDA ETHAN. I want to inform you that you " +
                            "needed to leave $overflow minutes ago to catch your next event. " +
                            "Your best option to be on time is to travel by " + transportTranslations[bestMethod] + ". "     +
                            "Do you want to use the suggested method?",
                    options = listOf(
                        UserInputOption(
                            tokens = positiveTokens,
                            onSuccess = {
                                timeToGo = getTimeToGo(event, estimatedTimes[bestMethod]!!)
                                var suffix = ""
                                if (raining && bestMethod == "foot-walking"){
                                    suffix = "Be aware that it could rain on your way. "
                                }
                                runBlocking { speak("Okay. Your updated time to leave is in $timeToGo minutes. $suffix ") }
                            }
                        ),
                        UserInputOption(
                            tokens = negativeTokens,
                            onSuccess = {
                                var suffix = ""
                                if (raining && transportation_mode == "foot-walking"){
                                    suffix = "Be aware that it could rain on your way. "
                                }
                                runBlocking { speak("I understand. I won't change anything. Please leave as soon as possible to minimize your delay. $suffix ") }
                            },
                        ),
                    ))
            }
        }else {
            runBlocking { speak("Hello. This is your PDA ETHAN. I want to inform you that you need to leave in $timeToGo minutes to catch your next event.") }

            if (transportation_mode != "foot-walking") {
                routeDuration = estimatedTimes["foot-walking"]!!
                if (routeDuration < 10) {
                    var suffix = ""
                    if (raining){
                        suffix = "Be aware that it could rain on your way. "
                    }
                    speakAndHearSelectiveInput(
                        question = "Walking to your next event would only take $routeDuration minutes. Do you want to walk instead of your current transportation method? $suffix",
                        options = listOf(
                            UserInputOption(
                                tokens = positiveTokens,
                                onSuccess = {
                                    timeToGo = getTimeToGo(event, estimatedTimes["foot-walking"]!!)
                                    runBlocking { speak("Okay. To catch the upcoming event by foot you now need to leave in $timeToGo minutes. ") }
                                }
                            ),
                            UserInputOption(
                                tokens = negativeTokens,
                                response = "I understand. I won't change anything. You still need to leave in $timeToGo minutes."
                            ),
                        ))
                }
            } else {
                if (routeDuration > 60) {
                    speakAndHearSelectiveInput(
                        question = "Your current type of transportation is set to walking. This would take more than 60 minutes as of now. Do you want to change your type of transportation for this event? If so, please specify how you want to travel. ",
                        options = listOf(
                            UserInputOption(
                                tokens = listOf("car", "auto", "taxi"),
                                onSuccess = {
                                    timeToGo = getTimeToGo(event, estimatedTimes["driving-car"]!!)
                                    runBlocking { speak("Okay. To catch the upcoming event by car you now need to leave in $timeToGo minutes. ") }
                                }
                            ),
                            UserInputOption(
                                tokens = listOf("wheelchair", "rollstuhl", "rollie"),
                                onSuccess = {
                                    timeToGo = getTimeToGo(event, estimatedTimes["wheelchair"]!!)
                                    var suffix = ""
                                    if (raining){
                                        suffix = "Be aware that it could rain on your way. "
                                    }
                                    runBlocking { speak("Okay. To catch the upcoming event by wheelchair you now need to leave in $timeToGo minutes. $suffix") }
                                }
                            ),
                            UserInputOption(
                                tokens = listOf("bike", "drahtesel"),
                                onSuccess = {
                                    timeToGo = getTimeToGo(event, estimatedTimes["cycling-road"]!!)
                                    var suffix = ""
                                    if (raining){
                                        suffix = "Be aware that it could rain on your way. "
                                    }
                                    runBlocking { speak("Okay. To catch the upcoming event by bike you now need to leave in $timeToGo minutes. $suffix") }
                                }
                            ),
                            UserInputOption(
                                tokens = negativeTokens,
                                onSuccess = {
                                    var suffix = ""
                                    if (raining){
                                        suffix = "Be aware that it could rain on your way. "
                                    }
                                    runBlocking {speak("I understand. I won't change anything. You still need to leave withinin $timeToGo minutes. $suffix")}
                                },
                            ),
                        ))
                }
            }
        }
        onFinishedCallback()
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

    private fun extractDuration(response: JSONObject): Double {
        return response.getDouble("Duration")
    }

    private fun getDurations(target : String): Map<String, Int> {
        val durations = mutableMapOf<String, Int>()
        val movementTypes = getAllTransportationKeys()

        val current = currentLocation()

        val locations = getQueryLocationString(target, current)
        movementTypes.forEach {

            val url = "https://api.openrouteservice.org/v2/directions/" + it + "?api_key=" +  BuildConfig.API_KEY_Routes + locations
            println(url)

            val response = route.getDynamic(url)

            val duration = (extractDuration(response)/60).toInt()
            durations[it] = duration
        }
        println(durations)
        return durations
    }

    private fun getQueryLocationString(target : String, current : JSONObject): String {
        val openstreetURL = "https://nominatim.openstreetmap.org/search/"
        val openstreetEnding = "?format=json&addressdetails=1&limit=1&polygon_svg=1"

        val targetLocations = openStreet.getDynamic(openstreetURL+ target + openstreetEnding)

        val locations = listOf(listOf(current.getString("lon"), current.getString("lat")), listOf(targetLocations.getString("lon"), targetLocations.getString("lat")))
        val query = "&start=" + locations[0][0] + "," + locations[0][1] + "&end=" + locations[1][0] + "," + locations[1][1]
        return query
    }

    private fun currentLocation(): JSONObject {
        return LocalLocation.getCurrentLocation()
    }


    fun getTimeToGo(event: JSONObject, timeInMinutes : Int) : Int {
        val hour = event.getInt("startHour")
        val minute = event.getInt("startMinute")

        val diffHour = hour - LocalDateTime.now().hour
        val diffMinute = (minute - LocalDateTime.now().minute) + (diffHour * 60)

        return diffMinute - timeInMinutes
    }

}