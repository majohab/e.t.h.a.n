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
        val routeDurationMin = openRouteConnector.getRouteDuration("48.734276, 9.110791", "abc", "foot-walking")
    }

    @Test
    fun getCoordinatesTest(){

    }
}