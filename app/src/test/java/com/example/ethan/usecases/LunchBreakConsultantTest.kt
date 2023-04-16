package com.example.ethan.usecases

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class LunchBreakConsultantTest{
    private lateinit var lunchBreakConsultant: LunchBreakConsultant

    @Before
    fun setUp(){
        lunchBreakConsultant = LunchBreakConsultant {
            // this code will be executed when the use case is finished
            println("Good morning dialogue is finished!")
        }

        // now you can call the execute method to start the use case

    }

    @Test
    fun createTest(){
        lunchBreakConsultant.executeUseCase()
    }
}