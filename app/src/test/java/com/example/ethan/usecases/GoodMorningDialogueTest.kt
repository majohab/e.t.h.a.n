package com.example.ethan.usecases


class GoodMorningDialogueTest : UseCaseTest(){

    override var abstractUseCase = GoodMorningDialogue {
        // this code will be executed when the use case is finished
        println("Good morning dialogue is finished!")
    } as AbstractUseCase

    override fun mockingbird() {
        mock_waitForAPIs(5000)
        mock_onEthanVoiceOutputFinished(10)
        mock_speakAndHearSelectiveInput("car")
        println("a1")
        mock_waitForAPIs(3000)
        println("a2")
        mock_speakAndHearSelectiveInput("yes")
        println("a3")
        mock_waitForAPIs(3000)
        println("a4s")
        mock_onEthanVoiceOutputFinished(1)
    }
}