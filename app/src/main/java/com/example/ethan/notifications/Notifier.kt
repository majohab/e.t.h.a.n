package com.example.ethan.notifications

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.ethan.MainActivity
import com.example.ethan.R
import com.example.ethan.ui.gui.GUI.getSystemService
import java.util.*

object Notifier : BroadcastReceiver() {

    var builder: NotificationCompat.Builder? = null
    val notificationManager: NotificationManagerCompat? = null

    fun setupNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "default",
                "Use-Case Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun setupNotificationAlarm(context: Context) {
        val notificationIntent = Intent(context, Notifier::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, 0)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 11)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis < System.currentTimeMillis()) {
                // If it's already past 11AM, schedule for the next day
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


    fun askForPermission(permission: String, requestCode: Int, onPermissionGranted: () -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

        }
    }

    override fun onReceive(context: Context, intent: Intent) {

        val requestCode = 0

        val notificationIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, requestCode, notificationIntent, 0)

        builder = NotificationCompat.Builder(context, "default")
            //.setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("My App Notification")
            .setContentText("This is a simple notification from My App")
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = NotificationManagerCompat.from(context)


        val onPermissionGranted: () -> Unit = { builder?.let { Notifier.notificationManager?.notify(1, it.build()) } }

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (context is Activity) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    ActivityCompat.requestPermissions(
                        context,
                        arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                        requestCode
                    )
                }
                MainActivity.permissionCallbacks[requestCode] = onPermissionGranted
            }
            return
        }

        onPermissionGranted()
    }
}