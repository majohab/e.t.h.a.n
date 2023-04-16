package com.example.ethan.api.connectors

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class OpenStreetConectorTest{
    private lateinit var openStreetConector: OpenStreetConector

    @Before
    fun setUp(){
        openStreetConector = OpenStreetConector()
    }

    @Test
    fun testString(){
        assertEquals("url",openStreetConector.url)
    }

    @Test
    fun parseData(){


    }
}