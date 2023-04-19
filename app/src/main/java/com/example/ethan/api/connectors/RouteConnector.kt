package com.example.ethan.api.connectors


import com.example.ethan.BuildConfig
import com.example.ethan.LocalLocation
import com.example.ethan.sharedprefs.SharedPrefs
import com.example.ethan.transportation.getAllTransportationKeys
import org.json.JSONObject
import java.time.LocalDateTime

class RouteConnector() : AbstractConnector() {

    private val openStreet = OpenStreetConnector()

    override val url: String
        get() = "url"

    override fun parseData(data: String): JSONObject {
        try {
            val json = JSONObject(data)
            val features = json.getJSONArray("features")
            val feature = features.getJSONObject(0)
            val properties = feature.getJSONObject("properties")
            val segments = properties.getJSONArray("segments")
            val segment = segments.getJSONObject(0)
            val duration = segment.getDouble("duration")

            return JSONObject("{\"Duration\" : $duration}")
        }catch (e: java.lang.Exception) {
            println("Error in retrieving route!")
            return JSONObject("{\"Duration\" : 60.0}")
        }
    }


    public fun getTimeToNextEvent(event: JSONObject): Int{
        val hour = event.getInt("startHour")
        val minute = event.getInt("startMinute")


        val routTime = getDurations(event.getString("location"))[SharedPrefs.getTransportation()]!!


        val diffHour = hour - LocalDateTime.now().hour
        val diffMinute = (minute - LocalDateTime.now().minute) + (diffHour * 60)

        return diffMinute - routTime
    }

    fun getDurations(target : String): Map<String, Int> {
        val durations = mutableMapOf<String, Int>()
        val movementTypes = getAllTransportationKeys()

        val current = currentLocation()

        val locations = getQueryLocationString(target, current)
        movementTypes.forEach {

            val url = "https://api.openrouteservice.org/v2/directions/" + it + "?api_key=" +  BuildConfig.API_KEY_OPENROUTE + locations
            println(url)
            val response = getDynamic(url)

            val duration = (extractDuration(response)/60).toInt()
            durations[it] = duration
        }
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

    private fun extractDuration(response: JSONObject): Double {
        return response.getDouble("Duration")
    }
    private fun currentLocation(): JSONObject {
        return LocalLocation.getCurrentLocation()
    }
}