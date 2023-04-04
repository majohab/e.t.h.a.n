package com.example.ethan

import com.example.ethan.ui.speech.Speech2Text
import com.example.ethan.usecases.GoodMorningDialogue
import java.util.concurrent.Semaphore

object AgentHandler : Thread() {

    var semaphore: Semaphore = Semaphore(1)
    // andere Threads...

    override fun run() {

        println("hallo")


        super.run()
    }

    fun startUseCase(useCase: UseCase)
    {
        semaphore.acquire()
        when (useCase) {
            UseCase.GoodMorningDialogue -> {
                var goodMorningDialogue = GoodMorningDialogue()
                Speech2Text.setCallback()
                { input ->
                    goodMorningDialogue.onSpeechReceived(input)
                }
                goodMorningDialogue.start()
            }
            UseCase.NavigationAssistance -> {
                // TODO
            }
            UseCase.LunchBreakConsultant -> {
                // TODO
            }
            UseCase.SocialAssistance -> {
                // TODO
            }
        }
        semaphore.release()
    }
}

enum class UseCase
{
    GoodMorningDialogue,
    NavigationAssistance,
    LunchBreakConsultant,
    SocialAssistance
}