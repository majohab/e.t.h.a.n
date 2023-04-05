package com.example.ethan.usecases

import com.example.ethan.api.connectors.HoroscopeConnector
import com.example.ethan.api.connectors.NewsConnector
import com.example.ethan.api.connectors.StocksConnector
import java.time.LocalDateTime

class GoodMorningDialogue(onFinishedCallback: () -> Unit) : AbstractUseCase(onFinishedCallback) {
    private var horoscopeConnector = HoroscopeConnector()
    private var newsConnector = NewsConnector()
    private var stocksConnector = StocksConnector()

    override fun run() {
        println("GoodMorningDialogue Thread has been started!")

        // Request API 1
        val horoscopes = horoscopeConnector.get()
        // Request API 2
        val news_json = newsConnector.get()
        val news_articles = news_json.getJSONArray("articles")
        var news_string = ""
        for (i in 0..0) {
            val article = news_articles.getJSONObject(i)
            val title = article.getString("title")
            val description = article.getString("description")
            news_string += ("Article $i: $title. "
                            + "$description. ")
        }
        println(news_string)
        // Request API 3
        val stocknews_json = stocksConnector.get()
        val stocknews_feed = stocknews_json.getJSONArray("feed")
        var stocknews_string = ""
        for (i in 0..0) {
            val article = stocknews_feed.getJSONObject(i)
            val title = article.getString("title")
            stocknews_string += ("Headline $i: $title. ")
        }
        println(stocknews_string)


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