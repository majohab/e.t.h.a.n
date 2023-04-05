package com.example.ethan.api.connectors

import com.example.ethan.BuildConfig
import org.json.JSONObject

class StocksConnector : AbstractConnector() {
    override val url: String
        get() = "https://www.alphavantage.co/query?function=NEWS_SENTIMENT&apikey=" + BuildConfig.API_KEY_ALPHAVANTAGE

    override fun parseData(data: String): JSONObject {
        return JSONObject(data)
    }
}