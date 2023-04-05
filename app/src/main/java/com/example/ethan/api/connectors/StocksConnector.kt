package com.example.ethan.api.connectors

import com.example.ethan.BuildConfig
import com.example.ethan.api.interfaces.RestInterface
import org.json.JSONObject

class StocksConnector {
    private val restInterface = RestInterface()

    public fun get(ticker: String): JSONObject{
        val url = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + ticker + "&apikey=demo" + BuildConfig.API_KEY_NEWS
        val response = restInterface.get(url)
        return JSONObject(response!!)
    }
}