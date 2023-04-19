package com.example.ethan

import androidx.compose.runtime.mutableStateMapOf
import com.example.ethan.sharedprefs.SharedPrefs
import com.example.ethan.ui.speech.Speech2Text
import com.example.ethan.ui.speech.Text2Speech
import com.example.ethan.usecases.*
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import kotlin.concurrent.thread
import kotlin.reflect.KClass

object AgentHandler : Thread() {

    var useCaseRunning: Boolean = false
    var remainingTimeStrings = mutableStateMapOf<KClass<out AbstractUseCase>, String>( // the object the GUI observes
        GoodMorningDialogue::class to "-1",
        NavigationAssistance::class to "-1",
        LunchBreakConsultant::class to "-1",
        SocialAssistance::class to "-1"
    )

    var goodMorningDialogue = GoodMorningDialogue { useCaseFinished() }
    var navigationAssistance = NavigationAssistance { useCaseFinished() }
    var lunchBreakConsultant = LunchBreakConsultant { useCaseFinished() }
    var socialAssistance = SocialAssistance { useCaseFinished() }

    override fun run() {
        super.run()

        while (SharedPrefs.sharedPrefs == null) // Wait until initialized
            sleep(10)

        var justStarted = true

        // evaluate remaining times and start use case if needed
        while (true) {
            println("while start")
            for (useCase in remainingTimeStrings.keys) {
                val remainingTime = getRemainingMinutes(useCase)
                remainingTimeStrings[useCase] = remainingMinutesToString(useCase, remainingTime)
                println(useCase)
                if (!justStarted) {
                    if (remainingTime < 0 && classToObject(useCase).getDoneToday() && !useCaseRunning) {
                        startUseCase(useCase)
                    }
                }
            }
            justStarted = false
            println("while end")
            sleep(5000)
        }
    }

    private fun useCaseFinished(){
        Speech2Text.removeCallback()
        Speech2Text.removeErrorCallback()
        Text2Speech.removeCallback()
        useCaseRunning = false
    }

    fun startUseCase(useCase: KClass<out AbstractUseCase>)
    {
        println("aaaa")
        if(useCaseRunning) return
        useCaseRunning = true

        val useCaseClass = classToObject(useCase)
        useCaseClass.setDoneToday()

        Speech2Text.setCallback { input ->
            useCaseClass.onUserVoiceInputReceived(input)
        }
        Speech2Text.setErrorCallback { error: Int ->
            useCaseClass.onUserVoiceInputError(error)
        }
        Text2Speech.setCallback { useCaseClass.onEthanVoiceOutputFinished() }

        thread { useCaseClass.executeUseCase() }
    }

    private fun getRemainingMinutes (useCase: KClass<out AbstractUseCase>) : Int {
        val nextExecTime : LocalTime = classToObject(useCase).getExecutionTime()
        return ChronoUnit.MINUTES.between(LocalTime.now(), nextExecTime).toInt()
    }

    private fun remainingMinutesToString (useCase: KClass<out AbstractUseCase>, minutes: Int) : String {
        return if (minutes <= 0)
            if (classToObject(useCase).getDoneToday()) "Done" else "Ready"
        else if (minutes < 60)
            "$minutes min"
        else {
            val hours = minutes / 60
            "$hours hrs"
        }
    }

    private fun classToObject (useCase: KClass<out AbstractUseCase>) : AbstractUseCase {
        return when (useCase) {
            GoodMorningDialogue::class -> goodMorningDialogue
            NavigationAssistance::class -> navigationAssistance
            LunchBreakConsultant::class -> lunchBreakConsultant
            else -> socialAssistance
        }
    }
}