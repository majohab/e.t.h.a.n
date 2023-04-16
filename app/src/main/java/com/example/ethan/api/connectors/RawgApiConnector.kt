package com.example.ethan.api.connectors

import com.example.ethan.BuildConfig
import com.google.gson.Gson
import java.net.URL

class RawgApiConnector() {

    private val baseUrl = "https://api.rawg.io/api"
    private val gson = Gson()

    fun getGenres(): List<Pair<Int, String>> {
        val url = "$baseUrl/genres?key=${BuildConfig.API_KEY_RAWG}"
        val json = URL(url).readText()
        val response = gson.fromJson(json, RawgApiResponse::class.java)
        return response.results.map { genre -> Pair(genre.id, genre.name) }
    }

    fun getTopGamesByCategory(genreId: Int): List<Game> {
        val url = "$baseUrl/games?key=${BuildConfig.API_KEY_RAWG}&genres=$genreId&page_size=3&ordering=-rating"
        val json = URL(url).readText()
        val response = gson.fromJson(json, RawgApiResponse::class.java)
        return response.results.map { game ->
            Game(
                id = game.id,
                name = game.name,
                released = game.released,
                rating = game.rating,
                backgroundImage = game.background_image
            )
        }
    }

    data class RawgApiResponse(
        val results: List<GameResponse>
    )

    data class GameResponse(
        val id: Int,
        val name: String,
        val released: String,
        val rating: Float,
        val background_image: String
    )

    data class Game(
        val id: Int,
        val name: String,
        val released: String,
        val rating: Float,
        val backgroundImage: String
    )

}
