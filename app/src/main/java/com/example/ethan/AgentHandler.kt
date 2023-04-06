package com.example.ethan

import com.example.ethan.ui.speech.Speech2Text
import com.example.ethan.ui.speech.Text2Speech
import com.example.ethan.usecases.GoodMorningDialogue
import com.example.ethan.usecases.NavigationAssistance
import java.util.concurrent.Semaphore

object AgentHandler : Thread() {

    var semaphore: Semaphore = Semaphore(1)
    // andere Threads...

    override fun run() {

        println("hallo")

        super.run()
    }

    fun useCaseFinished(){
        Speech2Text.removeCallback()
        Speech2Text.removeErrorCallback()
        Text2Speech.removeCallback()
        semaphore.release()
    }

    fun startUseCase(useCase: UseCase)
    {
        if(!semaphore.tryAcquire()) return
        when (useCase) {
            UseCase.GoodMorningDialogue -> {
                var goodMorningDialogue = GoodMorningDialogue()
                {
                     -> useCaseFinished()
                }
                Speech2Text.setCallback()
                { input ->
                    goodMorningDialogue.onUserVoiceInputReceived(input)
                }
                Speech2Text.setErrorCallback()
                { error: Int ->
                    goodMorningDialogue.onUserVoiceInputError(error)
                }
                Text2Speech.setCallback()
                {
                    -> goodMorningDialogue.onEthanVoiceOutputFinished()
                }
                goodMorningDialogue.start()
            }
            UseCase.NavigationAssistance -> {
                var navigationAssistance = NavigationAssistance(){
                    -> useCaseFinished()
                }
                Speech2Text.setCallback(){ input ->
                    navigationAssistance.onUserVoiceInputReceived(input)
                }
                Speech2Text.setErrorCallback()
                { error: Int ->
                    navigationAssistance.onUserVoiceInputError(error)
                }
                Text2Speech.setCallback()
                {
                    -> navigationAssistance.onEthanVoiceOutputFinished()
                }
                navigationAssistance.start()
            }
            UseCase.LunchBreakConsultant -> {
                // TODO
            }
            UseCase.SocialAssistance -> {
                // TODO
            }
        }
        // Release
    }
}

enum class UseCase
{
    GoodMorningDialogue,
    NavigationAssistance,
    LunchBreakConsultant,
    SocialAssistance
}