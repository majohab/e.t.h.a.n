package com.example.ethan.api.connectors

import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.spy
import org.mockito.Mockito.`when`


class CalendarConnectorTest{
    private lateinit var calendarConnector: CalendarConnector
    private lateinit var calendarConnectorParse: CalendarConnector

    @Before
    fun setUp(){
        calendarConnector = spy(CalendarConnector())
        calendarConnectorParse = spy(CalendarConnector())

        val mockEvents = JSONObject()
        val mockEventsSub = JSONObject()
        val mockEventOne = JSONObject()
        val mockEventTwo = JSONObject()

        mockEventOne.put("startHour", 8)
        mockEventOne.put("startMinute", 30)
        mockEventOne.put("endHour", 12)
        mockEventOne.put("endMinute", 0)
        mockEventOne.put("location", "Lerchenstraße 1 Stuttgart")
        mockEventsSub.put("1", mockEventOne)

        mockEventTwo.put("startHour", 13)
        mockEventTwo.put("startMinute", 0)
        mockEventTwo.put("endHour", 15)
        mockEventTwo.put("endMinute", 45)
        mockEventTwo.put("location", "Lerchenstraße 1 Stuttgart")
        mockEventsSub.put("2", mockEventTwo)

        mockEvents.put("events", mockEventsSub)
        mockEvents.put("total", 2)
        mockEvents.put("nextEventID", 1)

        `when`(calendarConnector.get()).thenReturn(mockEvents) // Mock implementation
        `when`(calendarConnectorParse.get()).thenReturn(JSONObject()) // Mock implementation
    }

    @Test
    fun testString(){
        assertEquals("http://www.h2991977.stratoserver.net/TINF20B.ics",calendarConnector.url)
    }

    @Test
    fun parseData(){
        val eventsFreeBusy_json = calendarConnectorParse.get()
    }


    @Test
    fun getFirstPreferenceTest(){
        calendarConnector.getIdealExecutionTime(2, 5, 10)
        calendarConnector.getIdealExecutionTime(12, 30, 60)
        calendarConnector.getIdealExecutionTime(12, 30, 240)
        calendarConnector.getIdealExecutionTime(11, 30, 60)
        calendarConnector.getIdealExecutionTime(11, 30, 800)
    }


    @Test
    fun getFirstBreaksTest(){

        
    }
}