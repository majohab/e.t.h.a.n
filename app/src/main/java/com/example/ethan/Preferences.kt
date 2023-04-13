package com.example.ethan
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Preferences {

    var initialized = false
    lateinit var stringList: List<String>

    private fun initialize() {

        var file = Preferences::class.java.getResource("/preferences.json")?.readText()
        val listString = object : TypeToken<Map<String, String>>() {}.type
        var a: Map<String, String> = Gson().fromJson(file, listString)
        println(a)

        initialized = true
    }

    fun get(key: String): String {
        if (!initialized)
            initialize()

        return ""
    }
}