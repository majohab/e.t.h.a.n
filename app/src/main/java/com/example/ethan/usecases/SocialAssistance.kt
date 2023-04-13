package com.example.ethan.usecases

import android.os.Build
import com.example.ethan.api.connectors.SteamFriendsConnector
import java.time.LocalDateTime

class SocialAssistance(onFinishedCallback: () -> Unit) : AbstractUseCase(onFinishedCallback)  {

    override var resTimeID = "time_SA"

    private var steamFriendsConnector = SteamFriendsConnector()

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