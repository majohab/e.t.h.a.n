package com.example.ethan.api.connectors

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class OpenStreetMapApiTest {
    private lateinit var openStreetMapApi: OpenStreetMapApi

    @Before
    fun setUp(){
        openStreetMapApi = OpenStreetMapApi()
    }

    @Test
    fun findNearest(){
        val restaurants = openStreetMapApi.findNearestRestaurants(37.7749, -122.4194, 500 , "italian")
        val a = listOf<OsmRestaurant>(
            OsmRestaurant("Rich Table",
                37.7748889,
                -122.4227248,
                "https://www.richtablesf.com/"),
            OsmRestaurant("The Italian Homemade Company",37.7743924,-122.4209413,"https://italianhomemade.com/")
        )
        assertEquals(a,restaurants)

    }

    @Test
    fun restOfClass(){

    }
}