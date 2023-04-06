package com.example.ethan.api.connectors

import com.example.ethan.api.interfaces.RestInterface
import org.json.JSONObject

class FortuneConnector : AbstractConnector() {

    private val restInterface = RestInterface()
    override val url: String
        get() = "http://yerkee.com/api/fortune"

    override fun parseData(data: String): JSONObject {
        return JSONObject(data)
    }
}