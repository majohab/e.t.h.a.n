package com.example.ethan.api.connectors

import org.json.JSONArray
import org.json.JSONObject

class HoroscopeConnector : AbstractConnector() {

    override val url: String
        get() = "https://www.7timer.info/bin/astro.php/?lon=113.2&lat=23.1&ac=0&unit=metric&output=json&tzshift=0"
    // TODO: CHANGE IMMEDIATELY

    override fun parseData(data: String): String {
        return JSONObject(data).toString()
    }
}