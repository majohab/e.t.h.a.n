package com.example.ethan

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.ethan.notifications.RemindersManager
import com.example.ethan.notifications.AlarmsReceiver
import com.example.ethan.sharedprefs.SharedPrefs
import com.example.ethan.ui.gui.GUI.MainScreen
import java.util.*

class MainActivity : ComponentActivity() {

    companion object {
        var permissionCallbacks = mutableMapOf<Int, () -> Unit>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AgentHandler.start()

        SharedPrefs.initSharedPrefs(this) {
            setupNotificationChannel(this)
            setupNotificationAlarm(this)
        }

        setupNotificationChannel(this)
        RemindersManager.startReminder(this)

        setContent {
            MainScreen()
        }
    }



    fun setupNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "default",
                "Use-Case Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun setupNotificationAlarm(context: Context) {
        println("h1")
        val notificationIntent = Intent(context, AlarmsReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 14)
            set(Calendar.MINUTE, 20)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis < System.currentTimeMillis()) {
                println("next day")
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        println("onRequestPermissionsResult: $requestCode")
        when (requestCode) {
            0 -> { // Notifications
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, display the notification
                    if (permissionCallbacks.contains(requestCode))
                        permissionCallbacks[requestCode]!!.invoke()
                } else {
                    // Permission denied, show a message or do something else
                }
                return
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MainScreen()
}