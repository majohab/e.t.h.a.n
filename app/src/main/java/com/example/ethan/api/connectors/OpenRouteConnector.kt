package com.example.ethan.api.connectors

import com.example.ethan.BuildConfig
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.json.JSONArray
import org.json.JSONObject

class OpenRouteConnector() {

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class RouteResponse(
        val routes: List<Route> = emptyList()
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Route(
        val summary: Summary = Summary(),
        val segments: List<Segment> = emptyList()
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Summary(
        val duration: Double = 0.0,
        val distance: Double = 0.0
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Segment(
        val duration: Double = 0.0,
        val distance: Double = 0.0,
        val steps: List<Step> = emptyList()
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Step(
        val duration: Double = 0.0,
        val distance: Double = 0.0,
        val name: String? = "",
        val instruction: String? = "",
        val mode: String? = ""
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class GeocodeResponse(
        val features: List<Feature>,
        val geocoding: Geocoding // add this field
    ) {
        constructor() : this(emptyList(), Geocoding())
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Geocoding(
        val version: String = "",
        val attribution: String = "",
        val query: Query = Query()
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Query(
        val text: String = "",
        val size: Int = 0,
        val layers: List<String> = emptyList(),
        val private: Boolean = false,
        val lang: Lang = Lang(),
        var querySize: Int? = null
    )

    data class Lang(
        val name: String = "",
        val iso6391: String = "",
        val iso6393: String = "",
        val via: String = "",
        val defaulted: Boolean = false
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Feature(
        val geometry: Geometry
    ){
        constructor() : this(Geometry())
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Geometry(
        val coordinates: List<Double>
    ){
        constructor() : this(emptyList())
    }

    fun getRouteDuration(origin: String, destination: String, mode: String): Double {
        return getRouteDuration(getRoute(origin, destination, mode)!!)
    }

    fun getRouteDuration(properties: JSONObject) : Double {
        val summary = properties.getJSONObject("summary")
        return summary.getDouble("duration") / 60
    }

    fun getRouteInstructions(origin: String, destination: String, mode: String): List<Pair<String, Double>> {
        return getRouteInstructions(getRoute(origin, destination, mode)!!)
    }

    fun getRouteInstructions(properties: JSONObject) : List<Pair<String, Double>> {
        val steps: JSONArray = properties.getJSONArray("segments").getJSONObject(0).getJSONArray("steps")

        val instructionsList = mutableListOf<Pair<String, Double>>()
        for (i in 0 until steps.length()) {
            instructionsList.add(
                Pair(steps.getJSONObject(i).getString("instruction"), steps.getJSONObject(i).getDouble("duration"))
            )
        }

        return instructionsList
    }

    fun getRoute(origin: String, destination: String, mode: String): JSONObject? {
        val destinationCoordinate = getCoordinates(destination)
        val url = "https://api.openrouteservice.org/v2/directions/$mode?".toHttpUrlOrNull()!!.newBuilder()
            .addEncodedQueryParameter("api_key", BuildConfig.API_KEY_OPENROUTE)
            .addEncodedQueryParameter("start", origin)
            .addEncodedQueryParameter("end", destination)//"${destinationCoordinate[1]},${destinationCoordinate[0]}")
            .build()

        val request = Request.Builder()
            .url(url)
            .build()

        val response = OkHttpClient().newCall(request).execute()

        if (!response.isSuccessful)
            return null

        val responseBody = response.body?.string()
        val responseBody_JSON = JSONObject(responseBody!!)

        val properties = responseBody_JSON.getJSONArray("features").getJSONObject(0).getJSONObject("properties")
        return properties
    }

    fun getCoordinates(query: String): List<Double> {
        val url = "https://api.openrouteservice.org/geocode/search?".toHttpUrlOrNull()!!.newBuilder()
            .addEncodedQueryParameter("api_key", BuildConfig.API_KEY_OPENROUTE)
            .addEncodedQueryParameter("text", query)
            .build()

        val request = Request.Builder()
            .url(url)
            .build()

        val response = OkHttpClient().newCall(request).execute()
        val responseBody = response.body?.string()

        val mapper = ObjectMapper()
        val geocodeResponse: GeocodeResponse = mapper.readValue(responseBody ?: "")

        return geocodeResponse.features.firstOrNull()?.geometry?.coordinates ?: emptyList()
    }
}