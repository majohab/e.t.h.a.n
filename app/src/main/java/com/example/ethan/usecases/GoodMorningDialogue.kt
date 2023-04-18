package com.example.ethan.usecases

import android.os.Build
import com.example.ethan.BuildConfig
import com.example.ethan.LocalLocation
import com.example.ethan.api.connectors.*
import com.example.ethan.sharedprefs.SharedPrefs
import com.example.ethan.transportation.getAllTransportationKeys
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.LocalTime

class GoodMorningDialogue(onFinishedCallback: () -> Unit) : AbstractUseCase(onFinishedCallback) {

    override var shortForm: String = "GMD"

    private var fortuneConnector = FortuneConnector()
    private var newsConnector = NewsConnector()
    private var stocksConnector = StocksConnector()
    private var calendarConnector = CalendarConnector()
    private val route = RouteConnector()
    private val openStreet = OpenStreetConnector()

    override fun executeUseCase() {
        println("GoodMorningDialogue Thread has been started!")

        // Request API 1
        val fortune_json = fortuneConnector.get()
        val fortune_string = fortune_json.getString("fortune")

        // Reqeuest API 0
        val eventsFreeBusy_json = calendarConnector.get()
        val eventsTotal = eventsFreeBusy_json.getInt("total")
        var eventsResponseString = ""
        if (eventsTotal == 0){
            eventsResponseString += "Enjoy your free time."
        }else {
            val nextEventID: Int = eventsFreeBusy_json.getInt("nextEventID")
            var timeToGo = 0
            eventsFreeBusy_json.getJSONObject("events").keys().forEach { key ->
                val event: JSONObject = eventsFreeBusy_json.getJSONObject("events").getJSONObject(key)
                eventsResponseString += "Your $key. event is about to start at ${event.get("startHour")}:${event.get("startMinute")}. "
                if(nextEventID.toString() == key) {
                    timeToGo = route.getTimeToNextEvent(event)
                }
            }
            if (timeToGo > 0 && nextEventID != -1){
                eventsResponseString += "According to your favorite type of transportation for this day you need to leave in: ${(timeToGo/60)} hours and ${timeToGo%60} minutes for your next event. "
            }else if (nextEventID != -1){
                eventsResponseString += "According to your favorite type of transportation for this day you need to leave immediately for your next event. "
            }else {
                eventsResponseString += "All your events for today are already completed. Enjoy your end of work. "
            }
        }

        // Request API 2
        val news_json = newsConnector.get()
        println(news_json)
        val news_articles = news_json.getJSONArray("articles")
        var news_string = ""
        for (i in 0..1) {
            val article = news_articles.getJSONObject(i)
            val title = article.getString("title")
            //val description = article.getString("description")
            news_string += ("Article " + (i + 1) + ": $title. ")
                            //+ "$description ")
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


        val now = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.now()
        } else {
            TODO("VERSION.SDK_INT < O")
        }

        // Greet user with all gathered information
        runBlocking { speak("Good morning. Today is the ${now.dayOfMonth} of ${now.month}. It is ${now.hour} o'clock and ${now.minute} minutes. ")}
        runBlocking { speak("You have a total of $eventsTotal events for today. $eventsResponseString")}
        runBlocking { speak("Here is your daily update for your preferred stocks: $stocknews_string")}
        runBlocking { speak("Now your daily news: $news_string")}

        // Ask for his preferred transportation method
        speakAndHearSelectiveInput(
            question = "What is your favorite type of transportation for this day?", options = listOf(
            UserInputOption(
                tokens = listOf("car", "drive"),
                response = "You successfully set bus as your favourite transportation method for today.",
                onSuccess = { SharedPrefs.setString("transportation", "driving-car") }
            ),
            UserInputOption(
                tokens = listOf("bike", "drahtesel", "cycl", "bicycle"),
                response = "You successfully set bike as your favourite transportation method for today.",
                onSuccess = { SharedPrefs.setString("transportation", "cycling-regular") }
            ),
            UserInputOption(
                tokens = listOf("foot", "walk"),
                response = "You successfully set walking as your favourite transportation method for today.",
                onSuccess = { SharedPrefs.setString("transportation", "foot-walking") }
            ),
            UserInputOption(
                tokens = listOf("wheelchair"),
                response = "You successfully set wheelchair as your favourite transportation method for today.",
                onSuccess = { SharedPrefs.setString("transportation", "wheelchair") }
            )
        ))

        speakAndHearSelectiveInput(
            question = "Okay cool. Do you want to hear your fortune for today?", options = listOf(
            UserInputOption(
                tokens = positiveTokens,
                response = fortune_string,
            ),
            UserInputOption(
                tokens = negativeTokens,
                response = "...My personal guess is that you won't have any luck today."
            )
        ))

        runBlocking { speak("Have a great day!") }
        // Say how long it'll take the user to its destination

        println("GoodMorningDialogue Thread is about to end!")
        onFinishedCallback()
    }

    override fun getExecutionTime(): LocalTime {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalTime.parse(SharedPrefs.getString(getResTimeID()))
        } else {
            TODO("VERSION.SDK_INT < O")
        }
    }
}