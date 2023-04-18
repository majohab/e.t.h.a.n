package com.example.ethan.api.connectors

import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class RecipeConnectorTest{
    private lateinit var recipeConnector: RecipeConnector

    @Before
    fun setUp(){
        recipeConnector = RecipeConnector()
    }

    @Test
    fun search(){
        val recipe_json = recipeConnector.search("pasta")
        val recipe_one = recipe_json!!.getJSONArray("results").getJSONObject(0)
        val recipe_one_id = recipe_one.getInt("id")
        val recipe_recipe = recipeConnector.get(recipe_one_id)
        val recipe_sourceUrl = recipe_recipe.getString("sourceUrl")

        assertEquals("http://www.foodista.com/recipe/K6QWSKQM/pasta-with-tuna",recipe_sourceUrl)
    }
}