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
        val stockslist = listOf("IBM", "AAPL")
        var stocknews_string = ""
        for (s in stockslist)
        {
            val stocknews_json = stocksConnector.get(s)
            val stocknews_quote = stocknews_json.getJSONObject("Global Quote")
            val price = stocknews_quote.optString("05. price")
            stocknews_string += "Last price of $s was $price. "
        }
        println(stocknews_string)


        val now = LocalDateTime.now()

        // Greet user with all gather information
        speak("Good Morning. Today is the ${now.dayOfMonth} of ${now.month}. It is ${now.hour} o'clock and ${now.minute} minutes. ")
        speak("You have 5 events for today. ")
        speak("Here is your daily update for your preferred stocks: $stocknews_string. ")
        speak("This are your daily news: $news_string.")

        // Ask for his preferred transportation method
        askForUserVoiceInput("What is your favorite type of transportation for this day?")
        // Say how long it'll take the user to its destination

        println("GoodMorningDialogue Thread is about to end!")
        onFinishedCallback()
    }
}