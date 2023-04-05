package com.example.ethan.api.connectors

import okhttp3.FormBody
import okhttp3.Headers
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

class HoroscopeConnector() : AbstractConnector() {


    override val url: String
        get() = "https://www.7timer.info/bin/astro.php/?lon=113.2&lat=23.1&ac=0&unit=metric&output=json&tzshift=0"
    override val body: FormBody?
        get() = null
    // TODO: CHANGE IMMEDIATELY
    override val header: Headers
        get() = null

    override fun parseData(data: String): JSONObject {
        return JSONObject(data)
    }
}