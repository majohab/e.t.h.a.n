package com.example.ethan.usecases

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


class SocialAssistanceTest : UseCaseTest() {

    override var abstractUseCase = SocialAssistance {
        // this code will be executed when the use case is finished
        println("Navigation assistance finished")
    } as AbstractUseCase

    override fun mockingbird1() {
        // yes yes yes action
        mock_waitForAPIs(7000)
        mock_onEthanVoiceOutputFinished(60)
        mock_speakAndHearSelectiveInput("yes")
        println("a1")
        mock_onEthanVoiceOutputFinished(60)
        mock_speakAndHearSelectiveInput("yes")
        println("a2")
        mock_onEthanVoiceOutputFinished(60)
        mock_speakAndHearSelectiveInput("yes")
        println("a3")
        mock_onEthanVoiceOutputFinished(60)
        mock_speakAndHearSelectiveInput("Action")

        mock_waitForAPIs(3000)
        println("a4s")
        mock_onEthanVoiceOutputFinished(60)
        mock_onEthanVoiceOutputFinished(60)

        println("mock finished")
    }

    override fun mockingbird2() {
        // yes yes yes action
        mock_waitForAPIs(10000)
        mock_onEthanVoiceOutputFinished(40)
        mock_speakAndHearSelectiveInput("yes")
        println("a1")
        mock_onEthanVoiceOutputFinished(40)
        mock_speakAndHearSelectiveInput("yes")
        println("a2")
        mock_onEthanVoiceOutputFinished(40)
        mock_speakAndHearSelectiveInput("yes")
        println("a3")
        mock_onEthanVoiceOutputFinished(60)
        mock_speakAndHearSelectiveInput("Action")

        mock_waitForAPIs(3000)

        println("a4s")
        mock_onEthanVoiceOutputFinished(60)
        mock_onEthanVoiceOutputFinished(60)

        println("mock finished")
    }

    override fun mockingbird3() {
        // yes yes yes action
        mock_waitForAPIs(7000)
        mock_onEthanVoiceOutputFinished(60)
        mock_speakAndHearSelectiveInput("yes")
        println("a1")
        mock_onEthanVoiceOutputFinished(60)
        mock_speakAndHearSelectiveInput("yes")
        println("a2")
        mock_onEthanVoiceOutputFinished(60)
        mock_speakAndHearSelectiveInput("yes")
        println("a3")
        mock_onEthanVoiceOutputFinished(60)
        mock_speakAndHearSelectiveInput("Action")

        mock_waitForAPIs(3000)

        println("a4s")
        mock_onEthanVoiceOutputFinished(60)
        mock_onEthanVoiceOutputFinished(60)

        println("mock finished")
    }

    override fun mockingbird4() {
        // yes yes yes action
        mock_waitForAPIs(7000)
        mock_onEthanVoiceOutputFinished(60)
        mock_speakAndHearSelectiveInput("yes")
        println("a1")
        mock_onEthanVoiceOutputFinished(60)
        mock_speakAndHearSelectiveInput("yes")
        println("a2")
        mock_onEthanVoiceOutputFinished(60)
        mock_speakAndHearSelectiveInput("yes")
        println("a3")
        mock_onEthanVoiceOutputFinished(60)
        mock_speakAndHearSelectiveInput("Action")

        mock_waitForAPIs(3000)

        println("a4s")
        mock_onEthanVoiceOutputFinished(60)
        mock_onEthanVoiceOutputFinished(60)

        println("mock finished")
    }

}