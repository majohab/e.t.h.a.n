package com.example.ethan.usecases

import android.os.Build
import java.time.LocalDateTime

class LunchBreakConsultant(onFinishedCallback: () -> Unit) : AbstractUseCase(onFinishedCallback)  {

    override var resTimeID = "time_LBC"

    override fun executeUseCase() {
        TODO("Not yet implemented")
    }
}