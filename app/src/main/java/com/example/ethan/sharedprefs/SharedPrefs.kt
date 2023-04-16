package com.example.ethan.sharedprefs

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.example.ethan.AgentHandler
import kotlinx.coroutines.delay

object SharedPrefs {

    var sharedPrefs: SharedPreferences? = null

    fun initSharedPrefs(activity: Activity) {

        sharedPrefs = activity.getPreferences(Context.MODE_PRIVATE)

        if (!contains("initialized") || !getBoolean("initialized")) { // Note: This first initialization is what causes the app to crash when first opened
            val editor = sharedPrefs!!.edit()

            editor.putString(AgentHandler.goodMorningDialogue.getResTimeID(), "08:00")
            editor.putString(AgentHandler.lunchBreakConsultant.getResTimeID(), "12:00")
            editor.putString(AgentHandler.navigationAssistance.getResTimeID(), "15:00")
            editor.putString(AgentHandler.socialAssistance.getResTimeID(), "18:00")
            editor.putString("steam_id", "76561198198615839")

            editor.putBoolean("initialized", true)
            editor.apply()
        }
    }

    fun getTransportation() : String {
        return getString("transportation", "foot-walking")
    }

    fun get(key: String, defaultValue: Int = -1) : Int {
        return if (sharedPrefs == null) defaultValue else sharedPrefs!!.getInt(key, defaultValue)
    }

    fun setInt(key: String, value: Int) {
        val editor = sharedPrefs!!.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getFloat(key: String, defaultValue: Float = -1f) : Float {
        return if (sharedPrefs == null) defaultValue else sharedPrefs!!.getFloat(key, defaultValue)
    }

    fun setFloat(key: String, value: Float) {
        val editor = sharedPrefs!!.edit()
        editor.putFloat(key, value)
        editor.apply()
    }

    fun getBoolean(key: String, defaultValue: Boolean = false) : Boolean {
        return if (sharedPrefs == null) defaultValue else sharedPrefs!!.getBoolean(key, defaultValue)
    }

    fun setBoolean(key: String, value: Boolean) {
        val editor = sharedPrefs!!.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getString(key: String, defaultValue: String = "") : String {
        return if (sharedPrefs == null) defaultValue else sharedPrefs!!.getString(key, defaultValue) ?: ""
    }

    fun setString(key: String, value: String) {
        val editor = sharedPrefs!!.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun contains(key: String): Boolean {
        return if (sharedPrefs == null) false else sharedPrefs!!.contains(key)
    }
}