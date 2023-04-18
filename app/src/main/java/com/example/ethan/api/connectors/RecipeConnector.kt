package com.example.ethan.api.connectors

import com.example.ethan.BuildConfig
import com.example.ethan.api.interfaces.RestInterface
import org.json.JSONObject

class RecipeConnector {
    private val restInterface = RestInterface()

    // gets recipe based on recipe id
    public fun get(id: Int): JSONObject{
        val url = "https://api.spoonacular.com/recipes/" + id.toString() + "/information?includeNutrition=false&apiKey=" + BuildConfig.API_KEY_RECIPE
        val response = restInterface.get(url)
        return JSONObject(response!!)
    }

    // search for recipe will return 2
    public fun search(term: String): JSONObject{
        val url = "https://api.spoonacular.com/recipes/complexSearch?query=" + term + "&number=2&apiKey=" + BuildConfig.API_KEY_RECIPE
        val response = restInterface.get(url)
        println(response)
        println(JSONObject(response!!))
        return JSONObject(response!!)
    }
}