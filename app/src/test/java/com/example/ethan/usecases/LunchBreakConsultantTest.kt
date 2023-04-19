package com.example.ethan.usecases


class LunchBreakConsultantTest : UseCaseTest() {

    override var abstractUseCase = LunchBreakConsultant {
        // this code will be executed when the use case is finished
        println("Lunchbreakconsultanttest is finished!")
    } as AbstractUseCase

    override fun mockingbird1() {
        mock_waitForAPIs(1000)
        mock_onEthanVoiceOutputFinished(10)
        mock_speakAndHearSelectiveInput("15:00")
        mock_onEthanVoiceOutputFinished(10)
        println("a1")
        mock_speakAndHearSelectiveInput("home")
        mock_onEthanVoiceOutputFinished(10)

        println("a2")
        mock_speakAndHearSelectiveInput("pizza")
        mock_waitForAPIs(5000)
        mock_onEthanVoiceOutputFinished(10)
        println("a3")
        mock_speakAndHearSelectiveInput("1")
        mock_waitForAPIs(3000)

        println("a4s")
        mock_onEthanVoiceOutputFinished(1)
        println("mockingbiund1finished")
    }

    override fun mockingbird2() {
        mock_waitForAPIs(1000)
        mock_onEthanVoiceOutputFinished(10)
        mock_speakAndHearSelectiveInput("15:00")
        mock_onEthanVoiceOutputFinished(10)
        println("a1")
        mock_speakAndHearSelectiveInput("restaurant")
        mock_onEthanVoiceOutputFinished(10)

        println("a2")
        mock_speakAndHearSelectiveInput("indian")
        mock_waitForAPIs(5000)
        mock_onEthanVoiceOutputFinished(10)
        println("a3")
        mock_speakAndHearSelectiveInput("1")
        mock_waitForAPIs(3000)

        println("a4s")
        mock_onEthanVoiceOutputFinished(1)
    }

    override fun mockingbird3() {

        mock_waitForAPIs(1000)
        mock_onEthanVoiceOutputFinished(10)
        mock_speakAndHearSelectiveInput("15:00")
        mock_onEthanVoiceOutputFinished(10)
        println("a1")
        mock_speakAndHearSelectiveInput("home")
        mock_onEthanVoiceOutputFinished(10)

        println("a2")
        mock_speakAndHearSelectiveInput("pasta")
        mock_waitForAPIs(5000)
        mock_onEthanVoiceOutputFinished(10)
        println("a3")
        mock_speakAndHearSelectiveInput("3")
        mock_waitForAPIs(3000)
        mock_onEthanVoiceOutputFinished(10)
        println("a3")
        mock_speakAndHearSelectiveInput("1")
        mock_waitForAPIs(3000)

        println("a4s")
        mock_onEthanVoiceOutputFinished(1)
    }

    override fun mockingbird4() {

        mock_waitForAPIs(1000)
        mock_onEthanVoiceOutputFinished(10)
        mock_speakAndHearSelectiveInput("15:00")
        mock_onEthanVoiceOutputFinished(10)
        println("a1")
        mock_speakAndHearSelectiveInput("restaurant")
        mock_onEthanVoiceOutputFinished(10)

        println("a2")
        mock_speakAndHearSelectiveInput("bbq")
        mock_waitForAPIs(5000)
        mock_onEthanVoiceOutputFinished(10)
        mock_speakAndHearSelectiveInput("indian")
        mock_waitForAPIs(5000)
        mock_onEthanVoiceOutputFinished(10)
        println("a3")
        mock_speakAndHearSelectiveInput("1")
        mock_waitForAPIs(3000)

        println("a4s")
        mock_onEthanVoiceOutputFinished(1)

    }

}