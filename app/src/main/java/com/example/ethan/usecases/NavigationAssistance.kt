package com.example.ethan.usecases

import com.example.ethan.BuildConfig
import com.example.ethan.api.connectors.*
import com.example.ethan.sharedprefs.SharedPrefs
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.time.LocalDateTime

class NavigationAssistance(onFinishedCallback: () -> Unit) : AbstractUseCase(onFinishedCallback) {

    override var resTimeID = "time_NA"
    private val route = RouteConnector()
    private val openStreet = OpenStreetConector()
    private var navigationConnector = OpenRouteConnector()
    private var weatherApiConnector = OpenWeatherApiConnector()
    private var calendarConnector = CalendarConnector()

    override fun executeUseCase() {
        val weatherJSON = weatherApiConnector.getCurrentWeather(48.783821, 9.215519)
        println(weatherJSON)

        var transportation_mode = SharedPrefs.getString("transportation", "foot-walking")

        val eventsFreeBusy_json = calendarConnector.get()
        var timeToGo = 0
        val nextEventID: Int = eventsFreeBusy_json.getInt("nextEventID")
        if (nextEventID == -1) {
            return
        }
        val event =
            eventsFreeBusy_json.getJSONObject("events").getJSONObject(nextEventID.toString())

        val estimatetTimes = getDurations(event.getString("location"))

        val timeWithPreffered = estimatetTimes[transportation_mode]!!
        println(timeWithPreffered)
        timeToGo = getTimeToGo(event, timeWithPreffered)



        var routeDuration = timeWithPreffered

        if (timeToGo > 15) {
            return
        }
        runBlocking { speak("Hello. This is your PDA ETHAN. I want to inform you that you need to leave in $timeToGo minutes to catch your next event.") }

        if (transportation_mode != "foot-walking") {
            routeDuration = getTimeToGo(event, estimatetTimes["foot-walking"]!!)
            if (routeDuration < 10) {
                speakAndHearSelectiveInput(
                    question = "Walking to your next event would only take $routeDuration minutes. Do you want to walk instead of your current transportation method?",
                    options = listOf(
                        UserInputOption(
                            tokens = positiveTokens,
                            onSuccess = {
                                timeToGo = getTimeToGo(event, estimatetTimes["foot-walking"]!!)
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
                                timeToGo = getTimeToGo(event, estimatetTimes["driving-car"]!!)
                                runBlocking { speak("Okay. To catch the upcoming event by car you now need to leave in $timeToGo minutes. ") }
                            }
                        ),
                        UserInputOption(
                            tokens = listOf("wheelchair", "rollstuhl", "rollie"),
                            onSuccess = {
                                timeToGo = getTimeToGo(event, estimatetTimes["wheelchair"]!!)
                                runBlocking { speak("Okay. To catch the upcoming event by wheelchair you now need to leave in $timeToGo minutes. ") }
                            }
                        ),
                        UserInputOption(
                            tokens = listOf("bike", "drahtesel"),
                            onSuccess = {
                                timeToGo = getTimeToGo(event, estimatetTimes["cycling-road"]!!)
                                runBlocking { speak("Okay. To catch the upcoming event by bike you now need to leave in $timeToGo minutes. ") }
                            }
                        ),
                        UserInputOption(
                            tokens = negativeTokens,
                            response = "I understand. I won't change anything. You still need to leave withinin $timeToGo minutes. "
                        ),
                    ))
            }
        }
        val weather = getWeather(event.getString("location"))

        var sugestion = "You should be fine"

        if (weather == "Rain"){
            sugestion = "You should bring an umbrella"
        }

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

    private fun extractDuration(response: JSONObject): Double {
        val features = response.getJSONArray("features")
        val feature = features.getJSONObject(0)
        val properties = feature.getJSONObject("properties")
        val segments = properties.getJSONArray("segments")
        val segment = segments.getJSONObject(0)
        return segment.getDouble("duration")
    }

    private fun getDurations(target : String): Map<String, Int> {
        val durations = mutableMapOf<String, Int>()
        val movementTypes = listOf("driving-car", "cycling-regular", "foot-walking", "wheelchair")

        val current = currentLocation()

        val locations = getQueryLocationString(target, current)
        println(locations)
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

    private fun getQueryLocationString(target : String, curent : String): String {
        val openstreetURL = "https://nominatim.openstreetmap.org/search/"
        val openstreetEnding = "?format=json&addressdetails=1&limit=1&polygon_svg=1"

        val targetLocations = openStreet.getDynamic(openstreetURL+ target + openstreetEnding)

        val curentLocations = openStreet.getDynamic(openstreetURL+ curent + openstreetEnding)

        val locations = listOf(listOf(curentLocations.getString("lon"), curentLocations.getString("lat")), listOf(targetLocations.getString("lon"), targetLocations.getString("lat")))
        val query = "&start=" + locations[0][0] + "," + locations[0][1] + "&end=" + locations[1][0] + "," + locations[1][1]
        return query
    }

    private fun currentLocation(): String {
        //hier auslesen
        return "Hermann Hesse Straße 20 Nufringen"
    }


    fun getTimeToGo(event: JSONObject, timeInMinutes : Int) : Int {
        val hour = event.getInt("startHour")
        val minute = event.getInt("startMinute")

        val diffHour = hour - LocalDateTime.now().hour
        val diffMinute = (minute - LocalDateTime.now().minute) + (diffHour * 60)

        return diffMinute - timeInMinutes
    }

}