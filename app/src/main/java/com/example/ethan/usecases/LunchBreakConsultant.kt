package com.example.ethan.usecases

import com.example.ethan.LocalLocation
import com.example.ethan.api.connectors.*
import com.example.ethan.sharedprefs.SharedPrefs
import com.example.ethan.transportation.transportTranslations
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.time.LocalTime

class LunchBreakConsultant(onFinishedCallback: () -> Unit) : AbstractUseCase(onFinishedCallback)  {

    override var shortForm: String = "LBC"
    private var openStreetMapRestaurant = OpenStreetMapApi()
    private var calendarConnector = CalendarConnector()
    private val openRouteConnector = OpenRouteConnector()
    private val recipeConnector = RecipeConnector()

    override fun executeUseCase() {

        val origin = LocalLocation.getCurrentLocation()

        var preferredBreakTimeStart = LocalTime.parse("12:00")
        val preferredBreakDuration = 60
        val preferredBreakTimeEnd = preferredBreakTimeStart.plusMinutes(preferredBreakDuration.toLong())

        var suggestedBreaktimeStart = preferredBreakTimeStart
        var suggestedBreaktimeEnd = preferredBreakTimeEnd

        runBlocking { askForUserVoiceInput("Hi. I'm here to assure you having the best break today. Around what hour would you prefer to eat something?") }
        var gottime = false
        while (!gottime) {
            val timestring = formatTime(lastUserVoiceInput)
            if (timestring.equals("00:00")) {
                // No valid input
                gottime = false
                runBlocking { askForUserVoiceInput("I didn't quite catch that, please repeat your response.") }
            }
            else {
                gottime = true
                preferredBreakTimeStart = LocalTime.parse(timestring)
            }
        }

        val bestBreak = calendarConnector.getIdealExecutionTime(preferredBreakTimeStart.hour, preferredBreakTimeStart.minute, preferredBreakDuration)
        suggestedBreaktimeStart = bestBreak.first
        suggestedBreaktimeEnd = bestBreak.second
        runBlocking { speak("You should start your break at: $suggestedBreaktimeStart. It will end at: $suggestedBreaktimeEnd.") }

        fun homeCooking() {
            val orderedFoodTokens = listOf(
                // Foods
                "pizza", "pasta", "hamburger", "hot dog", "taco", "burrito", "sushi", "steak", "chicken", "fish and chips", "fried chicken", "meatloaf", "lasagna", "spaghetti", "mac and cheese",
                "grilled cheese", "quesadilla", "enchiladas", "pad thai", "ramen", "pho", "chow mein", "stir fry", "biryani", "curry", "risotto", "shepherd's pie", "beef stew", "chili", "souvlaki",
                "gyro", "kebab", "falafel", "shakshuka", "omelette", "scrambled eggs", "pancakes", "waffles", "French toast", "bagel", "croissant", "muffin", "toast", "cereal", "oatmeal", "yogurt",
                "salad", "caesar salad", "greek salad", "cobb salad", "spinach salad", "tuna salad", "chicken salad", "pita sandwich", "club sandwich", "BLT sandwich", "grilled chicken sandwich",
                "tuna sandwich", "chicken parmigiana", "schnitzel", "veal parmesan", "fried rice", "lobster", "crab", "shrimp", "clams", "mussels", "crab cakes", "salmon", "tuna", "trout", "cod",
                "haddock", "lobster roll", "chowder", "bisque", "potato soup", "chicken noodle soup", "minestrone", "split pea soup", "clam chowder", "beef noodle soup", "vegetable soup", "corn chowder",
                "gazpacho", "chili con carne", "beef bourguignon", "coq au vin", "beef stroganoff", "meatballs", "shepherd's pie", "corned beef and cabbage", "roast beef", "pork chops",
                "pork tenderloin", "pulled pork", "barbecue ribs", "meatloaf", "stuffed peppers", "chicken parmesan", "chicken marsala", "chicken cordon bleu", "grilled chicken", "roast chicken",
                "chicken fajitas", "chicken enchiladas", "chicken tikka masala", "beef tacos", "fish tacos", "vegetable stir fry", "tofu stir fry", "vegetable curry", "tofu curry", "falafel wrap",
                "vegetable fajitas", "vegetable enchiladas", "mushroom risotto", "spinach and feta stuffed chicken", "broiled salmon", "shrimp scampi", "lobster bisque",

                // Ingredients
                "Rice", "Pasta", "Chicken", "Beef", "Fish", "Potato", "Bread", "Eggs", "Cheese", "Milk", "Butter", "Yogurt", "Apple", "Orange", "Banana",
                "Strawberr", "Blueberr", "Tomato", "Carrot", "Broccoli", "Spinach", "Lettuce", "Cucumber", "Onion", "Garlic", "Pepper", "Mushroom", "Corn", "Bean", "Lentil",
                "Chickpea", "Nut", "Seed", "Olive oil", "Canola oil", "Salt", "Pepper", "Sugar", "Honey", "Chocolate", "Peanut butter", "Jam", "Bacon", "Sausages", "Ham", "Salmon", "Tuna",
                "Shrimp", "Crab", "Lobster", "Scallop", "Clam", "Mussel", "Oyster", "Soy sauce", "Vinegar", "Mustard", "Mayonnaise", "Ketchup", "Hot sauce", "Curry powder", "Cinnamon", "Nutmeg",
                "Clove", "Cumin", "Paprika", "Thyme", "Rosemary", "Basil", "Oregano", "Bay leaves", "Vanilla extract", "Baking powder", "Baking soda", "Flour", "Sugar", "Brown sugar", "Powdered sugar",
                "Cornstarch", "Breadcrumbs", "Cake mix", "Pancake mix", "Waffle mix", "Ramen noodles", "Spaghetti sauce", "Tomato sauce", "Barbecue sauce", "Salsa", "Guacamole", "Tortilla chips",
                "Popcorn", "Cracker", "Cookie", "Cake", "Pie", "Ice cream", "Pudding", "Jello", "Peanut", "Almond", "Cashew", "Walnut", "Pistachio", "Hazelnut", "Macadamia nut", "Brazil nut",
                "Sunflower seed", "Pumpkin seed", "Sesame seed", "Quinoa", "Couscous", "Bulgur", "Polenta", "Grits")

            var selectedFoodToken = ""
            var recipeOptionsJson: JSONObject? = null
            var recipeQuestion = "Alrighty, home cook. What kind of meal would you like to eat?"

            while (recipeOptionsJson == null) {
                speakAndHearSelectiveInput(
                    question = recipeQuestion,
                    options = dynamicOptions(orderedFoodTokens, onSuccess = { token ->
                        selectedFoodToken = token
                    })
                )
                recipeOptionsJson = recipeConnector.search(selectedFoodToken)
                if (recipeOptionsJson == null)
                    recipeQuestion = "I sadly couldn't find any recipes that include $selectedFoodToken. Please try something else."
            }

            // Did not receive a correct response, e.g. because we already called the API 150 times a day
            if (recipeOptionsJson.has("code") && recipeOptionsJson.getInt("code") == 402) {
                runBlocking { speak("I'm sorry, I can't seem to have any recipes for you today. Please check again tomorrow.") }
                onFinishedCallback()
                return
            }


            val recipesOptions = recipeOptionsJson.getJSONArray("results")
            val recipeOptionsCount = minOf(5, recipesOptions.length())
            var recipesOptionsNamesString = ""
            for (i in 0 until recipeOptionsCount) {
                if (i > 0)
                    recipesOptionsNamesString += ", "
                recipesOptionsNamesString += recipesOptions.getJSONObject(i).getString("title")
            }

            runBlocking { speak("I found several recipes for following meals: $recipesOptionsNamesString.") }
            var recipeID = recipesOptions.getJSONObject(0).getInt("id")
            val recipeOptions = mutableListOf<UserInputOption>()
            for (i in 0 until recipeOptionsCount) {
                val recipeName = recipesOptions.getJSONObject(i).getString("title")
                val id = recipesOptions.getJSONObject(i).getInt("id")
                val option = UserInputOption(
                    tokens = when (i) {
                        0 -> listOf("1", "one", "first", recipeName)
                        1 -> listOf("2", "two", "second", recipeName)
                        2 -> listOf("3", "three", "third", recipeName)
                        3 -> listOf("4", "four", "fourth", recipeName)
                        else -> listOf("5", "five", "fifth", "last", recipeName)
                    },
                    onSuccess = { recipeID = id }
                )
                recipeOptions.add(option)
            }
            speakAndHearSelectiveInput(
                question = "Which one would you like to cook?",
                options = recipeOptions
            )
            val recipe = recipeConnector.get(recipeID)
            val recipe_sourceUrl = recipe.getString("sourceUrl")

            runBlocking { speak("Cool, the following URL provides the cooking instructions: $recipe_sourceUrl") }
        }

        fun goingOut() {
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

                runBlocking { speak("Got you! I will find restaurants with a $seletedCuisine cuisine near you.") }
                runBlocking { speak("Beep, Boop, Beep, Boop...") }

                restaurants = openStreetMapRestaurant.findNearestRestaurants(
                    origin.getString("lat").toDouble(),
                    origin.getString("lon").toDouble(),
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

            val restaurantCount = minOf(3, restaurants.count())
            if (restaurantCount == 1) {
                val restaurant = restaurants[0]
                val website = restaurant.website
                runBlocking { speak("What about $restaurant? You can find their website here: $website") }
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
                    val restaurantName = restaurants[i].name // has to be evalutated here
                    val option = UserInputOption(
                        tokens = when (i) {
                            0 -> listOf("1", "one", "first", restaurantName)
                            1 -> listOf("2", "two", "second", restaurantName)
                            else -> listOf("3", "three", "third", "last", restaurantName)
                        },
                        onSuccess = {
                            selectedRestaurant = restaurants[i]
                            println("") // DO NOT DELETE THIS LINE
                        }
                    )
                    restaurantOptions.add(option)
                }
                speakAndHearSelectiveInput(
                    question = "Which one sounds the most appealing to you?", options = restaurantOptions
                )
                if (selectedRestaurant.website.isNotEmpty() && selectedRestaurant.website.isNotBlank())
                    runBlocking { speak("Okay. This is the website of ${selectedRestaurant.name}: ${selectedRestaurant.website}") }

                val originString = "${origin.getString("lon")},${origin.getString("lat")}"
                val destinationString = "${selectedRestaurant.lon},${selectedRestaurant.lat}"
                val properties = openRouteConnector.getRoute(originString, destinationString, SharedPrefs.getTransportation())
                if (properties == null) {
                    runBlocking { speak("I sadly could not find a route to this restaurant.") }
                } else {
                    val duration = openRouteConnector.getRouteDuration(properties).toInt()
                    val instructions = openRouteConnector.getRouteInstructions(properties)

                    runBlocking { speak("By ${transportTranslations[SharedPrefs.getTransportation()]}, you will need to travel $duration minutes. "+
                        "You get there by following these instructions:") }

                    var instructionsString = ""
                    for (i in instructions.indices) {
                        instructionsString += "${i+1}. ${instructions[i].first}. "
                    }

                    runBlocking { speak(instructionsString) }
                }

            }
        }

        speakAndHearSelectiveInput(
            question = "Do you want to eat in a restaurant or cook at home?",
            options = listOf(
                UserInputOption(
                    tokens = listOf("restaurant", "go out", "going out", "somewhere", "someplace", "some place"),
                    onSuccess = { goingOut() }
                ),
                UserInputOption(
                    tokens = listOf("home", "cook", "self"),
                    onSuccess = { homeCooking() }
                )
            )
        )

        onFinishedCallback()
    }

    fun formatTime(timeString: String): String {
        val regex = Regex("[^0-9:]") // Matches any non-digit and non-colon character
        val cleanedString = regex.replace(timeString, "") // Removes any non-digit and non-colon characters
        val parts = cleanedString.split(":") // Splits the cleaned string into hour and minute parts

        // Extracts the hour and minute from the parts list
        val hour: Int
        val minute: Int

        if (parts.size == 2) {
            hour = parts[0].toIntOrNull() ?: 0
            minute = parts[1].toIntOrNull() ?: 0
        } else {
            val hourRegex = Regex("\\d+")
            hour = hourRegex.find(timeString)?.value?.toIntOrNull() ?: 0
            minute = 0
        }

        // Formats the hour and minute into a uniform string of type "HH:mm"
        val formattedHour = hour.toString().padStart(2, '0')
        val formattedMinute = minute.toString().padStart(2, '0')

        return "$formattedHour:$formattedMinute"
    }

    override fun getExecutionTime(): LocalTime {
        return LocalTime.parse(SharedPrefs.getString(getResTimeID()))
    }
}