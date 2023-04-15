package com.example.ethan
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Preferences {

    var initialized = false
    lateinit var map: Map<String, String>

    private fun initialize() {

        var file = Preferences::class.java.getResource("/preferences.json")?.readText()
        val listString = object : TypeToken<Map<String, String>>() {}.type
        map = Gson().fromJson(file, listString)
        println(map)

        initialized = true
    }

    fun get(key: String): String {
        if (!initialized)
            initialize()

        return map[key] ?: return "NO KEY '$key' SPECIFIED"
    }
}