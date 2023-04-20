package com.example.ethan.api.connectors

import com.example.ethan.BuildConfig
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class RawgApiConnectorTest{

    private lateinit var rawgApiConnector: RawgApiConnector

    @Before
    fun setUp(){
        rawgApiConnector = RawgApiConnector()
    }

    @Test
    fun testGetGenres(){
        rawgApiConnector.getGenres()
    }

    @Test
    fun testgetTopGamesByCategory(){
        rawgApiConnector.getTopGamesByCategory(1)
    }

    @Test
    fun dataclassesTest(){
        RawgApiConnector.RawgApiResponse(
            listOf(
                RawgApiConnector.GameResponse(
                    1,
                    "Fortnite",
                    "2023",4.3f,"abv")
            )
        )
        RawgApiConnector.Game( 1,
            "Fortnite",
            "2023",4.3f,"abv"
        )
    }
}