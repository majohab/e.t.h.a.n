package com.example.ethan.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class Movie(val name: String, val imageUrl: String, val desc: String, val category: String)

public abstract interface RESTApi{

    @GET("movielist.json")
    suspend fun getMovies() : List<Movie>

    companion object {
        var apiService: RESTApi? = null
        fun getInstance(baseUrl: String) : RESTApi {
            if (apiService == null) {
                apiService = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(RESTApi::class.java)
            }
            return apiService!!
        }
    }
}