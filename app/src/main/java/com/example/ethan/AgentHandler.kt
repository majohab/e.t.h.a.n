package com.example.ethan

import com.example.ethan.ui.gui.theme.MyClassFoo
import com.example.ethan.ui.gui.theme.MyClassFoo2
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

    fun useCaseFinished(){
        Speech2Text.removeCallback()
        Speech2Text.removeErrorCallback()
        semaphore.release()
    }

    fun startUseCase(useCase: UseCase)
    {
        val foo: MyClassFoo = MyClassFoo()
        val number: Int = foo.foo(1)


        if(!semaphore.tryAcquire()) return
        when (useCase) {
            UseCase.GoodMorningDialogue -> {
                var goodMorningDialogue = GoodMorningDialogue(){
                     -> useCaseFinished()
                }
                Speech2Text.setCallback()
                { input ->
                    goodMorningDialogue.onSpeechReceived(input)
                }
                Speech2Text.setErrorCallback()
                { error: Int ->
                    goodMorningDialogue.onSpeachError(error)
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