package com.example.ethan.usecases

import com.example.ethan.LocalLocation
import com.example.ethan.api.connectors.*
import com.example.ethan.sharedprefs.SharedPrefs
import com.example.ethan.transportation.transportTranslations
import kotlinx.coroutines.runBlocking

class LunchBreakConsultant(onFinishedCallback: () -> Unit) : AbstractUseCase(onFinishedCallback)  {

    override var shortForm: String = "LBC"
    private var openStreetMapRestaurant = OpenStreetMapApi()
    private var calendarConnector = CalendarConnector()
    private val openRouteConnector = OpenRouteConnector()

    override fun executeUseCase() {

        val a = openRouteConnector.getRouteSteps("8.681495,49.41461", "8.687872,49.420318", "foot-walking")
        println(a)


        var breakTime = 12
        val timeOptions = mutableListOf<UserInputOption>()
        for (i in 0..23) {
            val option = UserInputOption(
                tokens = listOf(i.toString()),
                onSuccess = {
                    breakTime = i
                    println("") // DO NOT DELETE THIS LINE
                }
            )
            timeOptions.add(option)
        }
        speakAndHearSelectiveInput(
            question = "Hi. I'm here to assure you having the best break today. Around what hour do" +
                    " prefer to eat something?", options = timeOptions
        )

        val eventsFreeBusy_json = calendarConnector.get()
        val eventsTotal = eventsFreeBusy_json.getInt("total")

        if (eventsTotal == 0){
            // Preferred time is available
        }else {
            eventsFreeBusy_json.getJSONObject("events").keys().forEach {

            }
        }

        val validCuisines = listOf("afghan", "african", "algerian", "american", "arab", "argentinian", "armenian", "asian", "australian", "austrian", "azerbaijani", "balkan",
            "bangladeshi", "basque", "bbq", "belarusian", "belgian", "brazilian", "breakfast", "british", "bulgarian", "burmese", "cajun", "cambodian", "cameroonian", "canadian",
            "caribbean", "caucasian", "central_asian", "chilean", "chinese", "colombian", "corsican", "cote_d'ivoirean", "croatian", "cuban", "cuisine_of_the_levant", "cypriot", "czech",
            "danish", "delicatessen", "dominican", "donut", "dutch", "east_african", "ecuadorian", "egyptian", "emirati", "english", "eritrean", "estonian", "ethiopian", "european", "filipino",
            "finnish", "fondue", "french", "fusion", "gabonese", "galician", "georgian", "german", "ghanaian", "greek", "grill", "guatemalan", "haitian", "hawaiian", "himalayan_nepalese", "honduran",
            "hong_kong", "hot_dogs", "hungarian", "ice_cream", "indian", "indonesian", "international", "iranian", "iraqi", "irish", "israeli", "italian", "jamaican", "japanese", "jewish_kosher",
            "jordanian", "kazakh", "kenyan", "korean", "kurdish", "kyrgyz", "laotian", "latin_american", "latvian", "lebanese", "libyan", "liechtenstein", "lithuanian", "luxembourgian", "macanese",
            "macedonian", "malagasy", "malaysian", "malian", "maltese", "mauritian", "mexican", "middle_eastern", "moldovan", "mongolian", "moroccan", "mozambican", "multicultural", "native_american",
            "nepalese", "new_zealand", "nicaraguan", "nigerian", "norwegian", "organic", "pakistani", "pan_asian", "paraguayan", "persian", "peruvian", "pizza", "polish", "polynesian", "portuguese",
            "pub_food", "puerto_rican", "romanian", "russian", "salvadoran", "sandwich", "scandinavian", "scottish", "seafood", "senegalese", "serbian", "sicilian", "singaporean", "slovak", "slovenian",
            "soul_food", "south_african", "south_american", "southern", "spanish", "sri_lankan", "steak_house", "sudanese", "swahili", "swedish", "swiss", "syrian", "taiwanese",
            "tanzanian", "tapas", "tex-mex", "thai", "tibetan", "tunisian", "turkish", "ukrainian", "uruguayan", "uzbek", "vegan", "vegetarian", "venezuelan", "vietnamese", "welsh",
            "west_african", "yemeni", "zambian","zimbabwean"
        )

        val validCuisines_input = validCuisines.map {it.replace("_", " ")}

        var seletedCuisine = ""
        val cuisineOptions = mutableListOf<UserInputOption>()
        for (i in validCuisines_input.indices) {
            val option = UserInputOption(
                tokens = listOf(validCuisines_input[i]),

                onSuccess = {
                    seletedCuisine = validCuisines[i]
                    println("") // DO NOT DELETE THIS LINE
                }
            )
            cuisineOptions.add(option)
        }
        speakAndHearSelectiveInput(
            question = "What cuisine do you have in mind for today?", options = cuisineOptions
        )

        var restaurants = listOf<OsmRestaurant>()
        while (restaurants.isEmpty()) {

            runBlocking { speak("Got you! I will find a restaurant with a $seletedCuisine cuisine and calculate how you can get there by " + transportTranslations[SharedPrefs.getTransportation()]) }
            runBlocking { speak("Beep, Boop, Beep, Boop...") }

            restaurants = openStreetMapRestaurant.findNearestRestaurants(
                LocalLocation.getCurrentLocation().getString("lat").toDouble(),
                LocalLocation.getCurrentLocation().getString("lon").toDouble(),
                1000,
                seletedCuisine
            )
            println(restaurants)
            if (restaurants.isEmpty()) {
                runBlocking { speak("Sadly, I didn't find a fitting restaurant in a radius of 1000 meters") }
                speakAndHearSelectiveInput(
                    question = "Please give me a different cuisine to search for.",
                    options = cuisineOptions
                )
            }
        }

        var restaurantCount = minOf(3, restaurants.count())
        if (restaurantCount == 1) {
            val restaurant = restaurants[0]
            val name = restaurant
            val website = restaurant.website
            runBlocking { speak("What about $name? You can find their website here: $website") }
        }
        else {
            var restaurantsNamesString = ""
            for (i in 0 until restaurantCount) {
                if (i > 0)
                    restaurantsNamesString += ", "
                restaurantsNamesString += restaurants[i].name
            }
            runBlocking { speak("I found the following restaurants: $restaurantsNamesString") }

            var selectedRestaurant = restaurants[0]
            val restaurantOptions = mutableListOf<UserInputOption>()
            for (i in 0 until restaurantCount) {
                val option = UserInputOption(
                    tokens = when (i) {
                        0 -> listOf("1", "one", "first", restaurants[i].name)
                        1 -> listOf("2", "two", "second", restaurants[i].name)
                        else -> listOf("3", "three", "third", "last", restaurants[i].name)
                    },
                    onSuccess = {
                        selectedRestaurant = restaurants[i]
                        println("") // DO NOT DELETE THIS LINE
                    }
                )
                restaurantOptions.add(option)
            }
            speakAndHearSelectiveInput(
                question = "Which one sounds the most appealing for you?", options = restaurantOptions
            )
            runBlocking { speak("Okay. This is the website of " + selectedRestaurant.name + ": " + selectedRestaurant.website) }
            runBlocking { speak("I will calculate how you can get there by " + transportTranslations[SharedPrefs.getTransportation()] + ".") }
        }

        onFinishedCallback()
    }
}