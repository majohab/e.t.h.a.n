package com.example.ethan.api.connectors

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class OpenRouteConnectorTest{

    private lateinit var openRouteConnector: OpenRouteConnector

    @Before
    fun setUp(){
        openRouteConnector = OpenRouteConnector()
    }

    @Test
    fun routeResponseTest(){
        //check if objects can be instanciated
        OpenRouteConnector.RouteResponse()
        OpenRouteConnector.Route()
        OpenRouteConnector.Summary()
        OpenRouteConnector.Segment()
        OpenRouteConnector.Step()
        OpenRouteConnector.GeocodeResponse()
        OpenRouteConnector.Geocoding()
        OpenRouteConnector.Query()
        OpenRouteConnector.Lang()
        OpenRouteConnector.Feature()
        OpenRouteConnector.Geometry()
        assertEquals(1,1)
    }

    @Test
    fun getRouteDurationTest(){
        val routeDurationMin = openRouteConnector.getRouteDuration("8.681495,49.41461", "8.687872,49.420318", "foot-walking")
    }

    @Test
    fun getCoordinatesTest(){

    }
}