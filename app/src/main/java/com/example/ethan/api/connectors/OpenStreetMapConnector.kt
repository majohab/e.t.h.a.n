package com.example.ethan.api.connectors

import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

data class OsmRestaurant(val name: String, val lat: Double, val lon: Double, val website: String)

class OpenStreetMapApi {
    private val httpClient = OkHttpClient()

    fun findNearestRestaurants(latitude: Double, longitude: Double, radius: Int, cuisine: String? = null): List<OsmRestaurant> {
        val query = """
            [out:json];
            node["amenity"="restaurant"]${if (cuisine != null) """["cuisine"="$cuisine"]""" else ""}
            (around:$radius,$latitude,$longitude);
            out;
        """.trimIndent()

        val url = HttpUrl.Builder()
            .scheme("https")
            .host("overpass-api.de")
            .addPathSegment("api")
            .addPathSegment("interpreter")
            .addQueryParameter("data", query)
            .build()
        val request = Request.Builder()
            .url(url)
            .build()
        val response = httpClient.newCall(request).execute()
        if (!response.isSuccessful) {
            throw RuntimeException("Failed to fetch nearest restaurants: ${response.code} ${response.body?.string()}")
        }
        val jsonObj = JSONObject(response.body?.string() ?: "")
        val elements = jsonObj.getJSONArray("elements")
        val restaurants = mutableListOf<OsmRestaurant>()
        for (i in 0 until elements.length()) {
            val element = elements.getJSONObject(i)
            val name = element.getJSONObject("tags").getString("name")
            val lat = element.getDouble("lat")
            val lon = element.getDouble("lon")
            val website = element.getJSONObject("tags").optString("website", "")
            restaurants.add(OsmRestaurant(name, lat, lon, website))
        }
        return restaurants
    }
}