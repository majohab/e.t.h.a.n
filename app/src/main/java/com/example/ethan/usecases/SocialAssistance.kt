package com.example.ethan.usecases

import android.os.Build
import com.example.ethan.api.connectors.SteamFriendsConnector
import java.time.LocalDateTime

class SocialAssistance(onFinishedCallback: () -> Unit) : AbstractUseCase(onFinishedCallback)  {

    private var steamFriendsConnector = SteamFriendsConnector()

    override fun getExecutionTime(): LocalDateTime {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.of(
                /* year = */ 2023,
                /* month = */ 4,
                /* dayOfMonth = */ 6,
                /* hour = */ 19,
                /* minute = */ 0,
                /* second = */ 0,
                /* nanoOfSecond = */ 0)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
    }

    override fun executeUseCase() {
        val friends = steamFriendsConnector.get("76561198198615839")
        println()
        println()
        println()
        println()
        println()
        println(friends)
    }
}