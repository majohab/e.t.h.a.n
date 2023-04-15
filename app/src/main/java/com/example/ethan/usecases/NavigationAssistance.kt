package com.example.ethan.usecases

import androidx.compose.ui.tooling.data.SourceLocation
import com.example.ethan.BuildConfig
import com.example.ethan.api.connectors.CalendarConnector
import com.example.ethan.api.connectors.OpenStreetConector
import com.example.ethan.api.connectors.OpenWeatherApiConnector
import com.example.ethan.api.connectors.RouteConnector
import kotlinx.coroutines.runBlocking
import org.json.JSONObject

class NavigationAssistance(onFinishedCallback: () -> Unit) : AbstractUseCase(onFinishedCallback) {
    private val route = RouteConnector()
    private val calendarConnector = CalendarConnector()
    private val openStreet = OpenStreetConector()

    val favoriteMovementType = 0

    val movementTypes = listOf("driving-car", "cycling-regular", "foot-walking")

    val favoriteMovementTypeName = movementTypes[favoriteMovementType]

    override var resTimeID = "time_NA"
    private var weatherApiConnector = OpenWeatherApiConnector()


    override fun executeUseCase() {

        println("NavigationAssistance has been started!")


        val target ="Lerchenstraße 1 Stuttgart"
        val curent = getLocation()
        val nextMeeting = 60
        var output = ""


        val locations = getQueryLocationString(target, curent)

        val queryString = "&start=" + locations[0][0] + "," + locations[0][1] + "&end=" + locations[1][0] + "," + locations[1][1]

        val durations = getDurations(queryString)

        println(durations)

        val leaveTime = nextMeeting - durations[favoriteMovementType]

        output += "To get to your next meeting at $target in $nextMeeting minutes by $favoriteMovementTypeName you need to leave in ${leaveTime.toInt()} minutes!\n"


        val weather = getWeather(locations[1])

        var sugestion = "You should be fine"

        if (weather == "Rain"){
            sugestion = "You should bring an umbrella"
        }

        output += "it is going to be $weather at your target location. $sugestion"

        runBlocking { speak(output) }

        onFinishedCallback()
    }

    private fun getLocation(): String {
        return "Hermann Hesse Straße 20 Nufringen"
    }

    private fun getDurations(locations : String): ArrayList<Double> {
        val durations = ArrayList<Double>()

        movementTypes.forEach {

            val url = "https://api.openrouteservice.org/v2/directions/" + it + "?api_key=" +  BuildConfig.API_KEY_Routes + locations


            val response = route.getDynamic(url)

            val duration = extractDuration(response)
            durations.add(duration/60)
        }
        return durations
    }

    private fun getQueryLocationString(target : String, curent : String): List<List<String>> {
        val openstreetURL = "https://nominatim.openstreetmap.org/search/"
        val openstreetEnding = "?format=json&addressdetails=1&limit=1&polygon_svg=1"

        val targetLocations = openStreet.getDynamic(openstreetURL+ target + openstreetEnding)

        val curentLocations = openStreet.getDynamic(openstreetURL+ curent + openstreetEnding)

        return listOf(listOf(targetLocations.getString("lon"), targetLocations.getString("lat")), listOf(curentLocations.getString("lon"), curentLocations.getString("lat")))

    }

    private fun getWeather(targetLocation: List<String>) :String {
        val weatherJSON = weatherApiConnector.getCurrentWeather(targetLocation[1].toDouble(), targetLocation[0].toDouble())
        val weather = weatherJSON!!.getJSONArray("weather").getJSONObject(0)
        return weather.getString("main")
    }
    private fun extractDuration(response : JSONObject): Double {
        val features = response.getJSONArray("features")
        val feature = features.getJSONObject(0)
        val properties = feature.getJSONObject("properties")
        val segments = properties.getJSONArray("segments")
        val segment = segments.getJSONObject(0)
        return segment.getDouble("duration")
    }
}