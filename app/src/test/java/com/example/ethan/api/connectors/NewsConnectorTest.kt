package com.example.ethan.api.connectors

import com.example.ethan.BuildConfig
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
    fun testStringTest(){
        assertEquals("https://newsapi.org/v2/top-headlines?country=us&apiKey=" + BuildConfig.API_KEY_NEWS ,newsConnector.url)
    }

    @Test
    fun parseDataTest(){
        val eventsFreeBusy_json = newsConnector.get()



    }
}
