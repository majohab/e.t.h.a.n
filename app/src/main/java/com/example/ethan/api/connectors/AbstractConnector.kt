package com.example.ethan.api.connectors

import com.example.ethan.api.interfaces.RestInterface
import okhttp3.FormBody
import okhttp3.Headers
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

abstract class AbstractConnector() {

    private val restInterface = RestInterface()
    abstract val url: String
    abstract val body: FormBody
    abstract val header: Headers

    public fun get(url): JSONObject{
        val response = restInterface.get(url)
        return parseData(response!!)
    }

    public fun post(): JSONObject{
        val response = restInterface.post(url,header,body)
        return parseData(response!!)
    }
    abstract fun parseData(data: String): JSONObject
}