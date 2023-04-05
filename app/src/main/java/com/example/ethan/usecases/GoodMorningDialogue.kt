package com.example.ethan.usecases

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.text.toLowerCase
import com.example.ethan.api.connectors.CalendarConnector
import com.example.ethan.api.connectors.HoroscopeConnector
import com.example.ethan.api.connectors.NewsConnector
import com.example.ethan.api.connectors.StocksConnector
import java.time.LocalDateTime

class GoodMorningDialogue(onFinishedCallback: () -> Unit) : AbstractUseCase(onFinishedCallback) {
    private var horoscopeConnector = HoroscopeConnector()
    private var newsConnector = NewsConnector()
    private var stocksConnector = StocksConnector()
    private var calendarConnector = CalendarConnector()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun run() {
        println("GoodMorningDialogue Thread has been started!")

        // Request API 1
        val horoscopes = horoscopeConnector.get()

        // Reqeuest API 0
        val eventsFreeBusy = calendarConnector.get()["answer"]

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
        speak("$eventsFreeBusy")
        speak("Here is your daily update for your preferred stocks: $stocknews_string. ")
        speak("This are your daily news: $news_string.")

        // Ask for his preferred transportation method
        askForUserVoiceInput("What is your favorite type of transportation for this day?")

        if (lastUserVoiceInput.lowercase().contains("bus")) {
            speak("You successfully set bus as your favourite transportation method for today.")
        }else if(lastUserVoiceInput.lowercase().contains("train")){
            speak("You successfully set train as your favourite transportation method for today.")
        }else if(lastUserVoiceInput.lowercase().contains("bike")){
            speak("You successfully set bike as your favourite transportation method for today.")
        }else if(lastUserVoiceInput.lowercase().contains("foot")){
            speak("You successfully set walking as your favourite transportation method for today.")
        }
        speak("Have a great day!")
        // Say how long it'll take the user to its destination

        println("GoodMorningDialogue Thread is about to end!")
        onFinishedCallback()
    }
}