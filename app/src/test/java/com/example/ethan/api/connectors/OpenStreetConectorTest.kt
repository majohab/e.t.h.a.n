package com.example.ethan.api.connectors

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class OpenStreetConectorTest{
    private lateinit var openStreetConnector: OpenStreetConnector

    @Before
    fun setUp(){
        openStreetConnector = OpenStreetConnector()
    }

    @Test
    fun testString(){
        assertEquals("url",openStreetConnector.url)
    }

    @Test
    fun parseDataTest(){
        val targetLocations = openStreetConnector.getDynamic("https://nominatim.openstreetmap.org/search/Unter%20den%20Linden%201%20Berlin?format=json&addressdetails=1&limit=1&polygon_svg=1")

    }
}