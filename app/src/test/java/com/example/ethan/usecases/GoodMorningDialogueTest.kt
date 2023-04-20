package com.example.ethan.usecases

import org.mockito.Mockito


class GoodMorningDialogueTest : UseCaseTest(){

    override var abstractUseCase = GoodMorningDialogue {
        // this code will be executed when the use case is finished
        println("Good morning dialogue is finished!")
    } as AbstractUseCase

    override fun mockingbird1() {
        // footwalking, car yes
        Mockito.`when`(sharedPrefs.getString("transportation","foot-walking")).thenReturn("foot-walking")
        mock_waitForAPIs(7000)
        mock_onEthanVoiceOutputFinished(100)
        mock_speakAndHearSelectiveInput("car")
        println("a1")
        mock_waitForAPIs(3000)
        println("a2")
        mock_speakAndHearSelectiveInput("yes")
        println("a3")
        mock_waitForAPIs(3000)
        println("a4s")
        mock_onEthanVoiceOutputFinished(100)
    }

    override fun mockingbird2() {
        // footwalking bike yes
        Mockito.`when`(sharedPrefs.getString("transportation","foot-walking")).thenReturn("foot-walking")
        mock_waitForAPIs(7000)
        mock_onEthanVoiceOutputFinished(100)
        mock_speakAndHearSelectiveInput("bike")
        println("a1")
        mock_waitForAPIs(3000)
        println("a2")
        mock_speakAndHearSelectiveInput("yes")
        println("a3")
        mock_waitForAPIs(3000)
        println("a4s")
        mock_onEthanVoiceOutputFinished(100)
    }


    override fun mockingbird3() {
        // footwalking foot yes
        Mockito.`when`(sharedPrefs.getString("transportation","foot-walking")).thenReturn("foot-walking")
        mock_waitForAPIs(7000)
        mock_onEthanVoiceOutputFinished(100)
        mock_speakAndHearSelectiveInput("foot")
        println("a1")
        mock_waitForAPIs(3000)
        println("a2")
        mock_speakAndHearSelectiveInput("yes")
        println("a3")
        mock_waitForAPIs(3000)
        println("a4s")
        mock_onEthanVoiceOutputFinished(100)
    }

    override fun mockingbird4() {
        // footwalking wheelchair yes
        Mockito.`when`(sharedPrefs.getString("transportation","foot-walking")).thenReturn("foot-walking")
        mock_waitForAPIs(7000)
        mock_onEthanVoiceOutputFinished(100)
        mock_speakAndHearSelectiveInput("wheelchair")
        println("a1")
        mock_onEthanVoiceOutputFinished(100)
        mock_waitForAPIs(3000)
        println("a2")
        mock_speakAndHearSelectiveInput("yes")
        println("a3")
        mock_onEthanVoiceOutputFinished(100)
        mock_waitForAPIs(3000)
        println("a4s")
        mock_onEthanVoiceOutputFinished(100)
    }
}