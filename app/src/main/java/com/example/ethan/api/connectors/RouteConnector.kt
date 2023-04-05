package com.example.ethan.api.connectors

import okhttp3.FormBody
import okhttp3.Headers
import org.json.JSONObject
import java.net.URL

class RouteConnector(url: String, body: FormBody, headers: Headers) : AbstractConnector() {
    override val body: FormBody
        get() = body
    override val header: Headers
        get() = headers
    override val url: String
        get() = url


    override fun parseData(data: String): JSONObject {
        return JSONObject(data)
    }
}