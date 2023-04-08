package com.example.ethan.api.connectors

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
    fun testBasicCalendaer(){

        assertEquals(1,1)
    }
}