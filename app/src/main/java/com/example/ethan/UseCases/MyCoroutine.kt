package com.example.ethan.UseCases

import com.example.ethan.api.RESTApi
import com.example.ethan.apiconnectors.GoodMorningConnector
import com.example.ethan.patterns.APIPattern

class MyCoroutine : APIPattern(){
    public suspend fun doGet() {
        val apiService = RESTApi.getInstance("https://www.howtodoandroid.com/apis/")
        try {
            val movieList = apiService.getMovies()
            print(movieList)
        }
        catch (e: Exception) {
            val errorMessage = e.message.toString()
            print(e.printStackTrace())
        }
    }
}