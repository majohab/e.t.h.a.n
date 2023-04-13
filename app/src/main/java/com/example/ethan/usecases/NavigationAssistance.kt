package com.example.ethan.usecases

import android.os.Build
import java.time.LocalDateTime

class NavigationAssistance(onFinishedCallback: () -> Unit) : AbstractUseCase(onFinishedCallback)  {

    override var resTimeID = "time_NA"

    override fun executeUseCase() {
        TODO("Not yet implemented")
    }
}