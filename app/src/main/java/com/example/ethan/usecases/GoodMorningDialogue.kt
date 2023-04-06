package com.example.ethan.usecases

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.ethan.api.connectors.*
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime

class GoodMorningDialogue(onFinishedCallback: () -> Unit) : AbstractUseCase(onFinishedCallback) {
    private var fortuneConnector = FortuneConnector()
    private var newsConnector = NewsConnector()
    private var stocksConnector = StocksConnector()
    private var calendarConnector = CalendarConnector()
    private var recipeConnector = RecipeConnector()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun run() {
        println("GoodMorningDialogue Thread has been started!")

        // Request Recipe
        val recipe_json = recipeConnector.search("pasta")
        val recipe_one = recipe_json.getJSONArray("results").getJSONObject(0)
        val recipe_one_id = recipe_one.getInt("id")
        val recipe_recipe = recipeConnector.get(recipe_one_id)
        val recipe_sourceUrl = recipe_recipe.getString("sourceUrl")
        println(recipe_sourceUrl)

        // Request API 1
        val fortune_json = fortuneConnector.get()
        val fortune_string = fortune_json.getString("fortune")

        // Reqeuest API 0
        val eventsFreeBusy = calendarConnector.get()["answer"]

        // Request API 2
        val news_json = newsConnector.get()
        println(news_json)
        val news_articles = news_json.getJSONArray("articles")
        var news_string = ""
        for (i in 0..0) {
            val article = news_articles.getJSONObject(i)
            val title = article.getString("title")
            val description = article.getString("description")
            news_string += ("Article " + (i + 1) + ": $title. "
                            + "$description ")
        }
        println(news_string)
        // Request API 3
        val stockslist_tickers = listOf("AAPL", "MSFT", "GOOG")
        val stockslist_names = listOf("Apple", "Microsoft", "Alphabet")
        var stocknews_string = ""
        for (i in stockslist_tickers.indices)
        {
            val stocknews_json = stocksConnector.get(stockslist_tickers[i])
            println(stocknews_json)
            val stocknews_quote = stocknews_json.getJSONObject("Global Quote")
            val price = stocknews_quote.optString("05. price").toFloat().toString()
            stocknews_string += "Last price of " + stockslist_names[i] + " was $price$. "
        }
        //println(stocknews_string)


        val now = LocalDateTime.now()

        // Greet user with all gather information
        runBlocking { speak("Good Morning. Today is the ${now.dayOfMonth} of ${now.month}. It is ${now.hour} o'clock and ${now.minute} minutes. ")}
        runBlocking { speak("$eventsFreeBusy")}
        runBlocking { speak("Here is your daily update for your preferred stocks: $stocknews_string")}
        runBlocking { speak("Now your daily news: $news_string")}

        // Ask for his preferred transportation method
        runBlocking { askForUserVoiceInput("What is your favorite type of transportation for this day?") }

        if (checkIfContainsWord("bus")) {
            runBlocking { speak("You successfully set bus as your favourite transportation method for today.") }
        }else if(checkIfContainsWord("train")){
            runBlocking { speak("You successfully set train as your favourite transportation method for today.") }
        }else if(checkIfContainsWord("bike")){
                runBlocking { speak("You successfully set bike as your favourite transportation method for today.") }
        }else if(checkIfContainsWord("foot")){
                runBlocking { speak("You successfully set walking as your favourite transportation method for today.") }
        }

        var yesOrNo = false
        runBlocking { askForUserVoiceInput("Okay cool. Do you want to hear your horoscope for today?") }
        while (!(checkIfPositive(lastUserVoiceInput) || checkIfNegative(lastUserVoiceInput)))
            runBlocking { askForUserVoiceInput("I didn't understand you. Please repeat. ") }

        if (checkIfPositive(lastUserVoiceInput)) {
            runBlocking { speak(fortune_string) }
        } else if (checkIfNegative(lastUserVoiceInput)) {
            runBlocking { speak("...") }
            runBlocking { speak("My personal guess is that you won't have any luck today.") }
        }

        runBlocking { speak("Have a great day!") }
        // Say how long it'll take the user to its destination

        println("GoodMorningDialogue Thread is about to end!")
        onFinishedCallback()
    }
}