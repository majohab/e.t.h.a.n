package com.example.ethan.api.connectors

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class NewsConnectorTest{
    private lateinit var newsConnector: NewsConnector

    @Before
    fun setUp(){
        newsConnector = NewsConnector()
    }

    @Test
    fun testString(){
        assertEquals("https://newsapi.org/v2/top-headlines?country=us&apiKey=" + BuildConfig.API_KEY_NEWS ,newsConnector.url)
    }"

    @Test
    fun parseData(){
        val eventsFreeBusy_json = newsConnector.get()



    }
}
