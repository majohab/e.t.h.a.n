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
    fun testString(){
        rawgApiConnector.getGenres()
    }

    @Test
    fun parseData(){

    }

}