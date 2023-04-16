package com.example.ethan.api.connectors

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class OpenWeatherApiConnectorTest{
    private lateinit var openWeatherApiConnector: OpenWeatherApiConnector

    @Before
    fun setUp(){
        openWeatherApiConnector = OpenWeatherApiConnector()
    }

    @Test
    fun findNearest(){
        val restaurants = openWeatherApiConnector.getCurrentWeather(37.7749, -122.4194)
        val a = null
        assertEquals(a,restaurants)

    }
}