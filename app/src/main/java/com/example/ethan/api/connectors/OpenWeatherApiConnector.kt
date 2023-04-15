package com.example.ethan.api.connectors

import com.example.ethan.BuildConfig

import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class OpenWeatherApiConnector() {
    private val client = OkHttpClient()

    fun getCurrentWeather(lat: Double, lon: Double): JSONObject? {
        val url = HttpUrl.Builder()
            .scheme("https")
            .host("api.openweathermap.org")
            .addPathSegments("data/2.5/weather")
            .addQueryParameter("lat", lat.toString())
            .addQueryParameter("lon", lon.toString())
            .addQueryParameter("appid", BuildConfig.API_KEY_WEATHER)
            .addQueryParameter("units", "metric")
            .addQueryParameter("lang", "en")
            .build()

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                println("API Key invalid.")
                return null
            }

            val jsonResponse = response.body?.string()
            return JSONObject(jsonResponse)
        }
    }
}