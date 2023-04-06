package com.example.ethan.api.connectors

import org.json.JSONArray
import org.json.JSONObject

class OpenStreetConector() : AbstractConnector() {
    override val url: String
        get() = "url"

    override fun parseData(data: String): JSONObject {

        var data1 = JSONArray(data)
        var json = data1.getJSONObject(0)
        return json
    }
}