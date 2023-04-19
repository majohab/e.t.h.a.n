package com.example.ethan.api.connectors

import com.example.ethan.api.interfaces.RestInterface
import org.json.JSONObject

abstract class AbstractConnector() {

    private val restInterface = RestInterface()
    abstract val url: String

    fun get(): JSONObject{
        val response = restInterface.get(url)
        return parseData(response!!)
    }
    fun getDynamic(uri : String): JSONObject{
        val response = restInterface.get(uri)
        return parseData(response!!)
    }
    abstract fun parseData(data: String): JSONObject
}