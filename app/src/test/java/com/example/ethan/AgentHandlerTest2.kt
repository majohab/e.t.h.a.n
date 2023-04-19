package com.example.ethan

import com.example.ethan.usecases.NavigationAssistance
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test

class AgentHandlerTest2 {

    private lateinit var agenthandler: AgentHandler

    @Before
    fun setUp(){
        agenthandler = AgentHandler
    }

    @Test
    fun getSemaphore() {
        agenthandler.start()
    }


}