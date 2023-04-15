package com.example.ethan.api.connectors

import android.os.Build
import androidx.annotation.RequiresApi
import net.fortuna.ical4j.data.CalendarBuilder
import net.fortuna.ical4j.model.DateTime
import net.fortuna.ical4j.model.component.VFreeBusy
import org.json.JSONArray
import org.json.JSONObject
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor
import java.util.*


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
        startTimes.forEachIndexed { index, element ->
            val formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'").withZone(ZoneId.systemDefault())
            var times = element.split("/")
            val event = JSONObject()
            println(times)
            val eventStart = Instant.from(formatter.parse(times[0])).atZone(ZoneId.systemDefault()).plusMinutes((timeZoneOffsetInMillis / 1000 / 60).toLong())
            //val eventEnd = Instant.from(formatter.parse(times[1])).atZone(ZoneId.systemDefault())

            //if(index == 0){
            //    result += "Your first element for today starts at " + eventStart.hour + ":" + eventStart.minute + ". " //+ "It will end on " + eventEnd.hour + ":" + eventEnd.minute + ". "
            //}else if(index == 1){
            //    result += "Your second element for today starts at " + eventStart.hour + ":" + eventStart.minute + ". "// + "It will end on " + eventEnd.hour + ":" + eventEnd.minute + ". "
            //}else {
                // For future use
            //}
            event.put("startHour", eventStart.hour)
            event.put("startMinute", eventStart.minute)
            event.put("location", "Lerchenstraße 1")
            result.put("${index+1}", event)
        }
        result.put("total",startTimes.size)
        return result
    }
}