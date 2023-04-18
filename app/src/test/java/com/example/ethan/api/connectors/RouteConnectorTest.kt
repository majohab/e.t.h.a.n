package com.example.ethan.api.connectors

import com.example.ethan.BuildConfig
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class RouteConnectorTest{

    private lateinit var routeConnector: RouteConnector

    @Before
    fun setUp(){
        routeConnector = RouteConnector()
    }

    @Test
    fun testString(){
        assertEquals("url",routeConnector.url)
    }

    @Test
    fun parseData(){


    }
}