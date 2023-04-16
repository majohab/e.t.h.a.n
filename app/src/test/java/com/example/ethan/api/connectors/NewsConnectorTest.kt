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
        assertEquals("https://newsapi.org/v2/top-headlines?country=us&apiKey=2bacda34a75040c2a69ed86d88f45bbc",newsConnector.url)
    }

    @Test
    fun parseData(){
        val eventsFreeBusy_json = newsConnector.get()



    }
}