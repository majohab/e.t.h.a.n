package com.example.ethan.usecases

import com.example.ethan.api.connectors.CalendarConnector
import com.example.ethan.api.connectors.OpenStreetMapApi
import com.example.ethan.sharedprefs.SharedPrefs
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Math.abs

class LunchBreakConsultant(onFinishedCallback: () -> Unit) : AbstractUseCase(onFinishedCallback)  {

    override var shortForm: String = "LBC"
    private var openStreetMapRestaurant = OpenStreetMapApi()
    private var calendarConnector = CalendarConnector()

    override fun executeUseCase() {
        SharedPrefs.setString("transportation", "foot-walking")
        var breakTime = 12
        var breakDuration = 1
        var suggested_breakTime = breakTime
        val timeOptions = mutableListOf<UserInputOption>()
        for (i in -23..0) {
            val option = UserInputOption(
                tokens = listOf(abs(i).toString()),
                onSuccess = {
                    breakTime = abs(i)
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
        var event_before: JSONObject? = null
        val breaks = JSONArray()

        if (eventsTotal == 0){
            // Preferred time is available
        }else {
            eventsFreeBusy_json.getJSONObject("events").keys().forEach {
                val event = eventsFreeBusy_json.getJSONObject("events").getJSONObject(it)
                val slot = JSONObject()
                if(event_before == null){
                    slot.put("startHour", 0)
                    slot.put("startMinute", 0)
                    slot.put("endHour", event.getInt("startHour"))
                    slot.put("endMinute", event.getInt("startMinute"))
                    slot.put("duration", event.getInt("startHour")*60+event.getInt("startMinute"))
                }else {
                    slot.put("startHour", event_before!!.getInt("endHour"))
                    slot.put("startMinute", event_before!!.getInt("endMinute"))
                    slot.put("endHour", event.getInt("startHour"))
                    slot.put("endMinute", event.getInt("startMinute"))
                    slot.put("duration", ( (event.getInt("startHour")*60+event.getInt("startMinute")) - (event_before!!.getInt("endHour")*60+event_before!!.getInt("endMinute"))))
                }
                event_before = event
                breaks.put(slot)
            }

            val slot = JSONObject()
            slot.put("startHour", event_before!!.getInt("endHour"))
            slot.put("startMinute", event_before!!.getInt("endMinute"))
            slot.put("endHour", 23)
            slot.put("endMinute", 59)
            slot.put("duration", (23*60+59) - (event_before!!.getInt("endHour")*60+event_before!!.getInt("endMinute")))
            breaks.put(slot)

            var min_distance = 24*60
            var best_break = JSONObject()
            for (x in 0 until breaks.length()){
                val option = breaks.getJSONObject(x)
                val distance_start = abs(breakTime*60 - (option.getInt("startHour")*60+option.getInt("startMinute")))
                val distance_end = abs(breakTime*60 - (option.getInt("endHour")*60+option.getInt("endMinute")))
                if(distance_start < min_distance){
                    min_distance = distance_start
                    best_break = option
                }
                if (distance_end < min_distance){
                    min_distance = distance_end
                    best_break = option
                }
            }

            if((best_break.getInt("startHour")*60+best_break.getInt("startMinute")) < breakTime*60 &&
                (best_break.getInt("endHour")*60+best_break.getInt("endMinute")) > breakTime*60+breakDuration*60 ){
                suggested_breakTime = breakTime
            }else if((best_break.getInt("endHour")*60+best_break.getInt("endMinute")) < breakTime*60+breakDuration*60) {
                // Break ends before preferred break ends
                if(best_break.getInt("duration") >= breakDuration*60){
                    suggested_breakTime = (best_break.getInt("endHour")*60+best_break.getInt("endMinute"))-breakDuration*60
                }else {
                    suggested_breakTime = (best_break.getInt("startHour")*60+best_break.getInt("startMinute"))
                }
            } else if((best_break.getInt("startHour")*60+best_break.getInt("startMinute")) > breakTime*60){
                // Break starts after preferred break starts
                suggested_breakTime = (best_break.getInt("startHour")*60+best_break.getInt("startMinute"))
            }
        }
        println("Start your break at: " + suggested_breakTime)

        val restaurants = openStreetMapRestaurant.findNearestRestaurants(37.7749, -122.4194, 1000 , "italian")
        println(restaurants)
    }
}