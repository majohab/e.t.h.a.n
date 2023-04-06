package com.example.ethan.usecases

import com.example.ethan.BuildConfig
import com.example.ethan.api.connectors.CalendarConnector
import com.example.ethan.api.connectors.OpenStreetConector
import com.example.ethan.api.connectors.RouteConnector
class NavigationAssistance(onFinishedCallback: () -> Unit) : AbstractUseCase(onFinishedCallback) {
    private val route = RouteConnector()
    private val calendarConnector = CalendarConnector()
    private val openStreet = OpenStreetConector()


    val movmentTypes = listOf("driving-car", "cycling-regular", "foot-walking")


    override fun run() {

        println("NavigationAssistance Thread has been started!")




        //val targetLocation = "8.687872,49.420318" //get from opnestreetApi + Location from
        //val targetLocation = openStreet.getDynamic(target) /target from calendar
        val target = "Lerchenstraße 1 Stuttgart"
        val curent = "Hermann Hesse Straße 20 Nufringen"

        val openstreetURL = "https://nominatim.openstreetmap.org/search/"
        val openstreetEnding = "?format=json&addressdetails=1&limit=1&polygon_svg=1"

        val targetLocations = openStreet.getDynamic(openstreetURL+ target + openstreetEnding)
        val targetLocation = targetLocations.getString("lon") + "," +targetLocations.getString("lat")

        val curentLocations = openStreet.getDynamic(openstreetURL+ curent + openstreetEnding)
        val currentLocation = curentLocations.getString("lon") + "," +curentLocations.getString("lat")

        val locations = "&start=$currentLocation&end=$targetLocation"


        movmentTypes.forEach {
            val url = "https://api.openrouteservice.org/v2/directions/" + it + "?api_key=" +  BuildConfig.API_KEY_Routes + locations


            val response = route.getDynamic(url)

            val features = response.getJSONArray("features")
            val feature = features.getJSONObject(0)
            val properties = feature.getJSONObject("properties")
            val segments = properties.getJSONArray("segments")
            val segment = segments.getJSONObject(0)
            val duration = segment.getDouble("duration")

            println(duration)


        }


    }
}