package com.example.ethan.usecases

import com.example.ethan.api.connectors.HoroscopeConnector
import com.example.ethan.api.connectors.NewsConnector
import java.time.LocalDateTime

class GoodMorningDialogue(onFinishedCallback: () -> Unit) : AbstractUseCase(onFinishedCallback) {
    private var horoscopeConnector = HoroscopeConnector()
    private var newsConnector = NewsConnector()

    override fun run() {
        println("GoodMorningDialogue Thread has been started!")

        // Request API 1
        val horoscopes = horoscopeConnector.get()
        // Request API 2
        val news = newsConnector.get()
        val articles = news.getJSONArray("articles")
        var newsString = ""
        for (i in 1..1) {
            val article = articles.getJSONObject(i)
            val title = article.getString("title")
            val description = article.getString("description")
            newsString += ("Article $i: $title. "
                            + "$description. ")
        }
        println(newsString)
        // Request API 3

        val now = LocalDateTime.now()

        // Greet user with all gather information
        speak(
    "Good Morning. Today is the ${now.dayOfMonth} of ${now.month}. It is ${now.hour} o'clock and ${now.minute} minutes."
        + "You have 5 events for today"
        + "The stocks are..."
        + "This are your daily news: "
        )

        // Ask for his preferred transportation method
        askForVoiceInput("What is your favorite type of transportation for this day?")
        // Say how long it'll take the user to its destination

        println("GoodMorningDialogue Thread is about to end!")
        onFinishedCallback()
    }
}