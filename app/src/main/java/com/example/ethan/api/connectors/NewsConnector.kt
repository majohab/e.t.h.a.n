package com.example.ethan.api.connectors

import com.example.ethan.BuildConfig
import org.json.JSONObject

class NewsConnector : AbstractConnector() {
    override val url: String
        get() = "https://newsapi.org/v2/top-headlines?country=us&apiKey=" + BuildConfig.API_KEY_NEWS

    override fun parseData(data: String): JSONObject {
        return JSONObject(data)
    }
}