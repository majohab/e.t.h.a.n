package com.example.ethan.api.connectors


import org.json.JSONObject

class RouteConnector() : AbstractConnector() {
    override val url: String
        get() = "url"

    override fun parseData(data: String): JSONObject {
        val json = JSONObject(data)
        val features = json.getJSONArray("features")
        val feature = features.getJSONObject(0)
        val properties = feature.getJSONObject("properties")
        val segments = properties.getJSONArray("segments")
        val segment = segments.getJSONObject(0)
        val duration = segment.getDouble("duration")

        return JSONObject("{\"Duration\" : $duration}")
    }
}