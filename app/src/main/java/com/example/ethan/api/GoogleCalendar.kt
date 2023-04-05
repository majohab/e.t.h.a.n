package com.example.ethan.api

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.DateTime
import com.google.api.services.calendar.CalendarScopes
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.model.Event
import com.google.api.services.calendar.model.Events
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger


class GoogleCalendar : Activity() {
    private val LOGGING_LEVEL: Level = Level.OFF

    private val PREF_ACCOUNT_NAME = "accountName"

    val httpTransport = AndroidHttp.newCompatibleTransport()

    val jsonFactory: JsonFactory = GsonFactory.getDefaultInstance()

    var service: Calendar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enable logging
        Logger.getLogger("com.google.api.client").setLevel(LOGGING_LEVEL)
        // Google Accounts
        val credential = GoogleAccountCredential.usingOAuth2(this, Collections.singleton(CalendarScopes.CALENDAR_READONLY))


        val settings: SharedPreferences = getPreferences(Context.MODE_PRIVATE)
        credential.setSelectedAccountName(settings.getString(PREF_ACCOUNT_NAME, null))
        // Tasks client
        service = Calendar.Builder(httpTransport, jsonFactory, credential).setApplicationName("Google-Calendar/1.0").build()
    }

    fun getEvents() {
        val now = DateTime(System.currentTimeMillis())

        val events = this.service?.events()!!.list("primary").setMaxResults(10).setTimeMin(now)
            .setOrderBy("startTime").setSingleEvents(true).execute();

        val items = events.getItems();

        if (items.isEmpty()) {
            println("No upcoming events found.");
        } else {
            println("Upcoming events");
            items.forEach() {
                val event = it
                var start = event.getStart().getDateTime()
                if (start == null) {
                    start = event.getStart().getDate();
                }
                println(event.getSummary() + "  " + start);
            }
        }
    }

}