package com.example.ethan.usecases

import com.example.ethan.api.connectors.CalendarConnector
import com.example.ethan.api.connectors.OpenStreetMapApi
import com.example.ethan.sharedprefs.SharedPrefs
import com.example.ethan.transportation.transportTranslations
import kotlinx.coroutines.runBlocking

class LunchBreakConsultant(onFinishedCallback: () -> Unit) : AbstractUseCase(onFinishedCallback)  {

    override var shortForm: String = "LBC"
    private var openStreetMapRestaurant = OpenStreetMapApi()
    private var calendarConnector = CalendarConnector()

    override fun executeUseCase() {
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

        val restaurants = openStreetMapRestaurant.findNearestRestaurants(37.7749, -122.4194, 1000 , "italian")
        println(restaurants)

        // get time slots >x minutes
        // suggest restaurants that are in time


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
        val options = mutableListOf<UserInputOption>()
        for (i in validCuisines_input.indices) {
            val option = UserInputOption(
                tokens = listOf(validCuisines_input[i]),
                onSuccess = {
                    seletedCuisine = validCuisines[i]
                    println("") // DO NOT DELETE THIS LINE
                }
            )
            options.add(option)
        }
        speakAndHearSelectiveInput(
            question = "What cuisine do you have in mind for today?", options = options
        )

        runBlocking { speak("Got you! I will find a restaurant with a $seletedCuisine cuisine and calculate how you can get there by " + transportTranslations[SharedPrefs.getTransportation()]) }
    }
}