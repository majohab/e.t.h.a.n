package com.example.ethan.api.connectors


import org.json.JSONObject

class RouteConnector() : AbstractConnector() {
    override val url: String
        get() = "url"

    override fun parseData(data: String): JSONObject {
        var json = JSONObject(data)
        return json
    }
}