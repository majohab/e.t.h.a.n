package com.example.ethan.usecases

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


class SocialAssistanceTest : UseCaseTest() {

    override var abstractUseCase = SocialAssistance {
        // this code will be executed when the use case is finished
        println("Navigation assistance finished")
    } as AbstractUseCase

    override fun mockingbird() {
        //mock_waitForAPIs(5000)
        println("test")
        mock_onEthanVoiceOutputFinished(10)
        mock_speakAndHearSelectiveInput("yes")
        mock_onEthanVoiceOutputFinished(10)
        println("a1")



    }

    override fun mockingbird2() {
        TODO("Not yet implemented")

    }

    override fun mockingbird3() {
        TODO("Not yet implemented")
    }

    override fun mockingbird4() {
        TODO("Not yet implemented")
    }

}