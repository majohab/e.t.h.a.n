package com.example.ethan.usecases

import android.os.Build
import java.time.LocalDateTime

class LunchBreakConsultant(onFinishedCallback: () -> Unit) : AbstractUseCase(onFinishedCallback)  {
    override fun getExecutionTime(): LocalDateTime {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.of(
                /* year = */ 2023,
                /* month = */ 4,
                /* dayOfMonth = */ 6,
                /* hour = */ 12,
                /* minute = */ 0,
                /* second = */ 0,
                /* nanoOfSecond = */ 0)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
    }

    override fun executeUseCase() {
        TODO("Not yet implemented")
    }
}