package com.example.ethan.api.connectors

import android.os.Build
import androidx.annotation.RequiresApi
import net.fortuna.ical4j.data.CalendarBuilder
import net.fortuna.ical4j.model.DateTime
import net.fortuna.ical4j.model.component.VFreeBusy
import org.json.JSONArray
import org.json.JSONObject
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.abs


class CalendarConnector : AbstractConnector(){
    override val url: String
        get() = "http://www.h2991977.stratoserver.net/TINF20B.ics"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun parseData(data: String): JSONObject {
        val calendar = CalendarBuilder().build(data.byteInputStream())
        val startOfToday = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 14)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val timeZoneOffsetInMillis = startOfToday.timeZone.getOffset(startOfToday.timeInMillis)
        val startOfDayInMillis = startOfToday.timeInMillis
        val start = DateTime(startOfDayInMillis + timeZoneOffsetInMillis)
        val end = DateTime(start.time + 1000 * 60 * 60 * 24 + timeZoneOffsetInMillis)
        val request = VFreeBusy(start, end)
        val response = VFreeBusy(request, calendar.components)

        val startTimes = response.getProperties("FREEBUSY").toString().removePrefix("FREEBUSY:").removeSuffix("\n").split(",")

        var result = JSONObject()
        result.put("nextEventID", -1)
        var events = JSONObject()
        var nextEventSet = false
        startTimes.forEachIndexed { index, element ->
            val formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'").withZone(ZoneId.systemDefault())
            var times = element.removeSuffix("\r").split("/")
            val event = JSONObject()
            val eventStart = Instant.from(formatter.parse(times[0])).atZone(ZoneId.systemDefault()).plusMinutes((timeZoneOffsetInMillis / 1000 / 60).toLong())
            val eventEnd = Instant.from(formatter.parse(times[1])).atZone(ZoneId.systemDefault()).plusMinutes((timeZoneOffsetInMillis / 1000 / 60).toLong())

            if(!Instant.from(formatter.parse((times[0]))).isAfter(Instant.now()) && !nextEventSet){
                nextEventSet = true
                result.put("nextEventID", index+1)
            }

            event.put("startHour", eventStart.hour)
            event.put("startMinute", eventStart.minute)
            event.put("endHour", eventEnd.hour)
            event.put("endMinute", eventEnd.minute)
            event.put("location", "Lerchenstraße 1 Stuttgart")
            events.put("${index+1}", event)
        }
        result.put("events", events)
        result.put("total",startTimes.size)
        return result
    }

    fun getIdealExecutionTime(preferredHour: Int, preferredMinute: Int, preferredDuration: Int): LocalTime{
        var suggBreakStart = LocalTime.parse("$preferredHour:$preferredMinute")

        val events = get()
        val eventsTotal = events.getInt("total")

        if (eventsTotal == 0){
            // Preferred time is available
            return suggBreakStart
        }else {
            val breaks = getBreaks(events)
            val bestBreak = getBestBreak(breaks, preferredHour, preferredMinute)


            if((bestBreak.getInt("startHour")*60 + bestBreak.getInt("startMinute")) < (preferredHour*60 + preferredMinute) &&
                (bestBreak.getInt("endHour")*60 + bestBreak.getInt("endMinute")) > (preferredHour*60 + preferredMinute) + preferredDuration){

                // Break extends whole preferred duration
                suggBreakStart = LocalTime.parse("$preferredHour:$preferredMinute") //preferredHour

            }else if((bestBreak.getInt("endHour")*60 + bestBreak.getInt("endMinute")) < (preferredHour*60 + preferredMinute) + preferredDuration) {

                // Break ends before preferred break ends
                if(bestBreak.getInt("duration") >= preferredDuration){
                    // Duration is long enough
                    val minutes = (bestBreak.getInt("endHour")*60 + bestBreak.getInt("endMinute")) - preferredDuration
                    suggBreakStart = LocalTime.parse("${(minutes/60)}:${minutes%60}")
                }else {
                    val minutes = bestBreak.getInt("startHour")*60 + bestBreak.getInt("startMinute")
                    suggBreakStart = LocalTime.parse("${(minutes/60)}:${minutes%60}")
                }

            } else if((bestBreak.getInt("startHour")*60 + bestBreak.getInt("startMinute")) > (preferredHour*60 + preferredMinute)){

                // Break starts after preferred break starts
                val minutes = bestBreak.getInt("startHour")*60 + bestBreak.getInt("startMinute")
                suggBreakStart = LocalTime.parse("${(minutes/60)}:${minutes%60}")
            }
        }

        return suggBreakStart
    }

    private fun getBreaks(events: JSONObject): JSONArray{
        var lastEvent: JSONObject? = null
        val breaks: JSONArray = JSONArray()

        events.getJSONObject("events").keys().forEach {
            val event = events.getJSONObject("events").getJSONObject(it)
            val breakSlot = JSONObject()
            if(lastEvent == null){
                breakSlot.put("startHour", 0)
                breakSlot.put("startMinute", 0)
                breakSlot.put("endHour", event.getInt("startHour"))
                breakSlot.put("endMinute", event.getInt("startMinute"))
                breakSlot.put("duration", event.getInt("startHour")*60+event.getInt("startMinute"))
            }else {
                breakSlot.put("startHour", lastEvent!!.getInt("endHour"))
                breakSlot.put("startMinute", lastEvent!!.getInt("endMinute"))
                breakSlot.put("endHour", event.getInt("startHour"))
                breakSlot.put("endMinute", event.getInt("startMinute"))
                breakSlot.put("duration", ( (event.getInt("startHour")*60+event.getInt("startMinute")) - (lastEvent!!.getInt("endHour")*60+lastEvent!!.getInt("endMinute"))))
            }
            lastEvent = event
            breaks.put(breakSlot)
        }

        val lastBreakSlot = JSONObject()
        lastBreakSlot.put("startHour", lastEvent!!.getInt("endHour"))
        lastBreakSlot.put("startMinute", lastEvent!!.getInt("endMinute"))
        lastBreakSlot.put("endHour", 23)
        lastBreakSlot.put("endMinute", 59)
        lastBreakSlot.put("duration", (23*60+59) - (lastEvent!!.getInt("endHour")*60+lastEvent!!.getInt("endMinute")))
        breaks.put(lastBreakSlot)

        return breaks
    }

    private fun getBestBreak(breaks: JSONArray, preferredHour: Int, preferredMinute: Int): JSONObject{
        var minDistance = 24*60
        var bestBreak = JSONObject()

        for (x in 0 until breaks.length()){
            val breakSlot = breaks.getJSONObject(x)

            // Distances till start of break slot and end of break slot (determine if ideal time is at start or end of break)
            val distanceStart =
                abs((preferredHour * 60 + preferredMinute) - (breakSlot.getInt("startHour") * 60 + breakSlot.getInt("startMinute")))
            val distanceEnd =
                abs((preferredHour * 60 + preferredMinute) - (breakSlot.getInt("endHour") * 60 + breakSlot.getInt("endMinute")))

            if(distanceStart < minDistance){
                minDistance = distanceStart
                bestBreak = breakSlot
            }
            if (distanceEnd < minDistance){
                minDistance = distanceEnd
                bestBreak = breakSlot
            }
        }

        return bestBreak
    }
}