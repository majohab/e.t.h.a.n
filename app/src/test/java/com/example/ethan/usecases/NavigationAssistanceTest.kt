package com.example.ethan.usecases

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class NavigationAssistanceTest : UseCaseTest() {

    override var abstractUseCase = NavigationAssistance {
        // this code will be executed when the use case is finished
        println("Navigation assistance finished")
    } as AbstractUseCase



    override fun mockingbird1() {
        mock_waitForAPIs(1000)
        mock_onEthanVoiceOutputFinished(10)
        mock_speakAndHearSelectiveInput("yes")
        println("a1")


    }

    override fun mockingbird2() {

        mock_waitForAPIs(1000)
        mock_onEthanVoiceOutputFinished(10)
        mock_speakAndHearSelectiveInput("yes")
        println("a1")
    }

    override fun mockingbird3() {
        mock_waitForAPIs(1000)
        mock_onEthanVoiceOutputFinished(10)
        mock_speakAndHearSelectiveInput("yes")
        println("a1")
    }

    override fun mockingbird4() {
        mock_waitForAPIs(1000)
        mock_onEthanVoiceOutputFinished(10)
        mock_speakAndHearSelectiveInput("yes")
        println("a1")
    }

}