package com.example.ethan.api.connectors

import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CalendarConnectorTest{
    private lateinit var calendarConnector: CalendarConnector

    @Before
    fun setUp(){
        calendarConnector = CalendarConnector()
    }

    @Test
    fun testString(){
        assertEquals("http://www.h2991977.stratoserver.net/TINF20B.ics",calendarConnector.url)
    }

    @Test
    fun parseData(){
        val eventsFreeBusy_json = calendarConnector.get()
        val eventsTotal = eventsFreeBusy_json.getInt("total")
        var eventsResponseString = ""


    }
}