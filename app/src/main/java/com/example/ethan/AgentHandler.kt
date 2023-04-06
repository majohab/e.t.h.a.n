package com.example.ethan

import android.os.Build
import androidx.compose.runtime.mutableStateMapOf
import com.example.ethan.ui.speech.Speech2Text
import com.example.ethan.ui.speech.Text2Speech
import com.example.ethan.usecases.GoodMorningDialogue
import com.example.ethan.usecases.LunchBreakConsultant
import com.example.ethan.usecases.NavigationAssistance
import com.example.ethan.usecases.SocialAssistance
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.concurrent.thread

object AgentHandler : Thread() {

    var useCaseRunning: Boolean = false
    var remainingTimes = mutableStateMapOf<UseCase, String>(
        UseCase.GoodMorningDialogue to "-1",
        UseCase.NavigationAssistance to "-1",
        UseCase.LunchBreakConsultant to "-1",
        UseCase.SocialAssistance to "-1"
    )

    var goodMorningDialogue = GoodMorningDialogue { useCaseFinished() }
    var navigationAssistance = NavigationAssistance { useCaseFinished() }
    var lunchBreakConsultant = LunchBreakConsultant { useCaseFinished() }
    var socialAssistance = SocialAssistance { useCaseFinished() }

    override fun run() {

        println("hallo")

        super.run()

        while (true) {
            for (entry in remainingTimes) {
                remainingTimes[entry.key] = getRemainingTimeString(entry.key)
            }
            sleep(500)
        }
    }

    private fun useCaseFinished(){
        Speech2Text.removeCallback()
        Speech2Text.removeErrorCallback()
        Text2Speech.removeCallback()
        useCaseRunning = false
    }

    fun startUseCase(useCase: UseCase)
    {
        if(useCaseRunning) return
        when (useCase) {
            UseCase.GoodMorningDialogue -> {
                goodMorningDialogue.initUseCase()
                thread { goodMorningDialogue.executeUseCase() }
            }
            UseCase.NavigationAssistance -> {
                navigationAssistance.initUseCase()
                thread { navigationAssistance.executeUseCase() }
            }
            UseCase.LunchBreakConsultant -> {
                lunchBreakConsultant.initUseCase()
                thread { lunchBreakConsultant.executeUseCase() }
            }
            UseCase.SocialAssistance -> {
                socialAssistance.initUseCase()
                thread { socialAssistance.executeUseCase() }
            }
        }
        useCaseRunning = true
    }


    private fun getRemainingTimeString (useCase: UseCase) : String {

        val nextExecTime = if (useCase == UseCase.GoodMorningDialogue) goodMorningDialogue.getExecutionTime()
                            else if (useCase == UseCase.NavigationAssistance) navigationAssistance.getExecutionTime()
                            else if (useCase == UseCase.LunchBreakConsultant) lunchBreakConsultant.getExecutionTime()
                            else socialAssistance.getExecutionTime()

        val now = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.now()
        } else {
            TODO("VERSION.SDK_INT < O")
        }

        val minutes = ChronoUnit.MINUTES.between(now, nextExecTime).toInt()
        if (minutes < 0)
            return "Done"
        if (minutes < 60)
            return "$minutes min"
        else {
            val hours = ChronoUnit.HOURS.between(now, nextExecTime).toInt()
            return "$hours hrs"
        }
    }
}

enum class UseCase
{
    GoodMorningDialogue,
    NavigationAssistance,
    LunchBreakConsultant,
    SocialAssistance
}