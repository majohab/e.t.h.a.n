package com.example.ethan.usecases

import android.os.Build
import java.time.LocalDateTime

class NavigationAssistance(onFinishedCallback: () -> Unit) : AbstractUseCase(onFinishedCallback)  {
    override fun getExecutionTime(): LocalDateTime {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.now()
        } else {
            TODO("VERSION.SDK_INT < O")
        }
    }

    override fun initUseCase() {
        TODO("Not yet implemented")
    }

    override fun executeUseCase() {
        TODO("Not yet implemented")
    }
}