package com.example.ethan.usecases

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class NavigationAssistanceTest{
    private lateinit var navigationAssistance: NavigationAssistance

    @Before
    fun setUp(){
        navigationAssistance = NavigationAssistance {
            // this code will be executed when the use case is finished
            println("Good morning dialogue is finished!")
        }

        // now you can call the execute method to start the use case

    }

    @Test
    fun createTest(){
        navigationAssistance.executeUseCase()
    }
}