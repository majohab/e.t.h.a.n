package com.example.ethan

import android.os.Build
import androidx.compose.runtime.mutableStateMapOf
import com.example.ethan.sharedprefs.SharedPrefs
import com.example.ethan.ui.speech.Speech2Text
import com.example.ethan.ui.speech.Text2Speech
import com.example.ethan.usecases.*
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import kotlin.concurrent.thread

object AgentHandler : Thread() {

    var useCaseRunning: Boolean = false
    var remainingTimeStrings = mutableStateMapOf<UseCase, String>(
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
        super.run()

        while (SharedPrefs.sharedPrefs == null) // Wait until initialized
            sleep(10)

        var justStarted = true

        while (true) {
            for (entry in remainingTimeStrings) {
                val useCase = entry.key
                val remainingTime = getRemainingMinutes(useCase)
                remainingTimeStrings[entry.key] = remainingMinutesToString(useCase, remainingTime)

                if (!justStarted) {
                    if (remainingTime < 0 && !useCaseToClass(useCase).getDoneToday() && !useCaseRunning)
                        startUseCase(useCase)
                }
            }
            justStarted = false
            sleep(5000)
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
        useCaseRunning = true

        val useCaseClass = useCaseToClass(useCase)

        Speech2Text.setCallback { input ->
            useCaseClass.onUserVoiceInputReceived(input)
        }
        Speech2Text.setErrorCallback { error: Int ->
            useCaseClass.onUserVoiceInputError(error)
        }
        Text2Speech.setCallback { useCaseClass.onEthanVoiceOutputFinished() }

        thread { useCaseClass.executeUseCase() }

        useCaseClass.setDoneToday()
    }

    private fun getRemainingMinutes (useCase: UseCase) : Int {
        val nextExecTime : LocalTime = useCaseToClass(useCase).getExecutionTime()

        val now = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalTime.now()
        } else {
            TODO("VERSION.SDK_INT < O")
        }

        return ChronoUnit.MINUTES.between(now, nextExecTime).toInt()
    }

    private fun remainingMinutesToString (useCase: UseCase, minutes: Int) : String {
        return if (minutes <= 0)
            if (useCaseToClass(useCase).getDoneToday()) "Done" else "Ready"
        else if (minutes < 60)
            "$minutes min"
        else {
            val hours = minutes / 60
            "$hours hrs"
        }
    }

    private fun useCaseToClass (useCase: UseCase) : AbstractUseCase {
        return when (useCase) {
            UseCase.GoodMorningDialogue -> goodMorningDialogue
            UseCase.NavigationAssistance -> navigationAssistance
            UseCase.LunchBreakConsultant -> lunchBreakConsultant
            else -> socialAssistance
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