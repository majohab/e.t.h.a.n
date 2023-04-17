package com.example.ethan.usecases

import com.example.ethan.api.connectors.RawgApiConnector
import com.example.ethan.api.connectors.SteamFriendsConnector
import com.example.ethan.sharedprefs.SharedPrefs
import kotlinx.coroutines.runBlocking

class SocialAssistance(onFinishedCallback: () -> Unit) : AbstractUseCase(onFinishedCallback)  {

    override var shortForm: String = "SA"

    private var steamFriendsConnector = SteamFriendsConnector()
    private var rawgApiConnector = RawgApiConnector()

    override fun executeUseCase() {


        // Steam API
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

        speakAndHearSelectiveInput(
            question = "Good evening. Do you want to know what your Steam-friends are up to?", options = listOf(
            UserInputOption(
                tokens = positiveTokens,
                response = steamfriends_string
            ),
            UserInputOption(
                tokens = negativeTokens,
                response = "Okay. They are probably having fun without you nerd."
            )
        ))

        speakAndHearSelectiveInput(
            question = "Do you need a recommendation on what you could play?", options = listOf(
            UserInputOption(
                tokens = positiveTokens,
                response = "Great then lets see...",
                onSuccess = {
                    println(SharedPrefs.get("fav_games_genre"))
                    speakAndHearSelectiveInput(
                        question = "Do you want to change your favorite genre?", options = listOf(
                            UserInputOption(
                                tokens = positiveTokens,
                                response = "Alright then.",
                                onSuccess = {
                                    SharedPrefs.setInt("fav_games_genre", -1)
                                }
                            )
                        )
                    )
                    if(SharedPrefs.get("fav_games_genre") == -1) {
                        val genres = rawgApiConnector.getGenres()
                        var genrestring = "Which of the following genres is your favorite? "
                        var options = mutableListOf<UserInputOption>()
                        var i = 0
                        for (pair in genres) {
                            val key = pair.first
                            val value = pair.second
                            if(i < genres.size)
                            {
                                genrestring += "$value, "
                            } else
                            {
                                genrestring += "$value."
                            }
                            options.add(
                                UserInputOption(
                                tokens = listOf(value),
                                response = "Ok i added $value as your favorite genre.",
                                onSuccess = {
                                    SharedPrefs.setInt("fav_games_genre", key)
                                }
                            )
                            )
                            i++
                        }
                        speakAndHearSelectiveInput(
                            question = genrestring, options = options
                        )
                    }
                    val games = rawgApiConnector.getTopGamesByCategory(SharedPrefs.get("fav_games_genre"))
                    var gamestring = "Ok i can recommend the following games from your favorite genre. "
                    for (game in games)
                    {
                        gamestring += "${game.name} is rated with ${game.rating.toString()}."
                    }
                    runBlocking { speak(gamestring) }
                    runBlocking { speak("Ok have fun") }
                    }
            ),
            UserInputOption(
                tokens = negativeTokens,
                response = "Okay. Then have fun.",
                    )
        ))
        onFinishedCallback()
    }
}