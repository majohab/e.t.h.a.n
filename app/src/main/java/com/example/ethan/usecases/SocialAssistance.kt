package com.example.ethan.usecases

import com.example.ethan.api.connectors.SteamFriendsConnector
import com.example.ethan.sharedprefs.SharedPrefs
import kotlinx.coroutines.runBlocking

class SocialAssistance(onFinishedCallback: () -> Unit) : AbstractUseCase(onFinishedCallback)  {

    override var shortForm: String = "SA"

    private var steamFriendsConnector = SteamFriendsConnector()

    override fun executeUseCase() {
        val steamfriends_list = steamFriendsConnector.get(SharedPrefs.getString("steam_id"))
        var steamfriends_string = ""

        for (pair in steamfriends_list) {
            val key = pair.first
            val value = pair.second

            if (value != 0) {
                steamfriends_string += "$key is " + when (value) {
                    1 -> "online"
                    2 -> "busy"
                    5 -> "looking to trade"
                    6 -> "looking to play"
                    else -> {}
                } + ". "
            }
        }

        runBlocking { speak ("Good evening.") }

        speakAndHearSelectiveInput(
            question = "How was your day?", options = listOf(
            UserInputOption(
                tokens = listOf("great", "superb", "super", "fantastic", "amazing", "stunning", "wonderful", "excellent"),
                response = "Wow! That's great to hear!"
            ),
            UserInputOption(
                tokens = listOf("bad", "not good", "worst", "miserable", "awful", "sad"),
                response = "Kann ich verstehen... So Leude, Forza hat runtergeladen"
            ),
            UserInputOption(
                tokens = listOf("good", "fine", "okay", "decent", "medium", "mediocre", "eher so mittel"),
                response = "I'm glad your day was okay. I think tomorrow will be even better!"
            )
        ))

        speakAndHearSelectiveInput(
            question = "Do you want to know what your Steam-friends are up to?", options = listOf(
            UserInputOption(
                tokens = positiveTokens,
                response = steamfriends_string
            ),
            UserInputOption(
                tokens = negativeTokens,
                response = "Okay. They are probably having fun without you nerd."
            )
        ))
        onFinishedCallback()
    }
}