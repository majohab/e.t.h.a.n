package com.example.ethan.usecases

import android.os.Build
import com.example.ethan.api.connectors.CalendarConnector
import com.example.ethan.api.connectors.RawgApiConnector
import com.example.ethan.api.connectors.SteamFriendsConnector
import com.example.ethan.sharedprefs.SharedPrefs
import kotlinx.coroutines.runBlocking
import java.time.LocalTime

class SocialAssistance(onFinishedCallback: () -> Unit) : AbstractUseCase(onFinishedCallback)  {

    override var shortForm: String = "SA"

    private var steamFriendsConnector = SteamFriendsConnector()
    private var rawgApiConnector = RawgApiConnector()
    private var calendarConnector = CalendarConnector()

    override fun executeUseCase() {

        // Steam id
        //if(SharedPrefs.getString("steam_id") == "")
        //{
        //    askForSteamId()
        //}
        //commented out because text to speech to shit

        // Steam API
        println("before")
        var steam_id = SharedPrefs.getString("steam_id")
        val steamfriends_list = steamFriendsConnector.get(steam_id)
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
        if(steamfriends_string == "")
        {
            steamfriends_string = "None of your friends are online."
        }
        println("test2")

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
        println("received answer")
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
                            ),
                            UserInputOption(
                                tokens = negativeTokens,
                                response = "Alright then.",
                            )
                        )
                        )
                    println("got a lot of tokens")
                    if(SharedPrefs.get("fav_games_genre") == -1) {
                        println("ask for fav genre")
                        askForFavGenre()
                        println("after ask for fav gerne")
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
        println("finished")
        onFinishedCallback()
    }

    fun askForFavGenre()
    {
        val genres = rawgApiConnector.getGenres()
        var genrestring = "Which of the following genres is your favorite? "
        var options = mutableListOf<UserInputOption>()
        for (i in 0 until genres.size) {
            val key = genres[i].first
            val value = genres[i].second
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
        }
        println("told fav genre")
        speakAndHearSelectiveInput(
            question = genrestring, options = options
        )
    }

    override fun getExecutionTime() : LocalTime {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val prefered = LocalTime.parse(SharedPrefs.getString(getResTimeID()))
            val duration = LocalTime.parse(SharedPrefs.getString("social_duration"))
            val startend = calendarConnector.getIdealExecutionTime(prefered.hour, prefered.minute, duration.hour*60)
            startend.first
        } else {
            TODO("VERSION.SDK_INT < O")
        }
    }
}