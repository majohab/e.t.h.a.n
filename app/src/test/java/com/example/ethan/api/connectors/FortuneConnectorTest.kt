package com.example.ethan.api.connectors

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class FortuneConnectorTest{
    private lateinit var fortuneConnector: FortuneConnector

    @Before
    fun setUp(){
        fortuneConnector = FortuneConnector()
    }

    @Test
    fun getUrl(){
        assertEquals("http://yerkee.com/api/fortune",fortuneConnector.url)
    }

    @Test
    fun parseData(){
        val eventsFreeBusy_json = fortuneConnector.get()



    }

}