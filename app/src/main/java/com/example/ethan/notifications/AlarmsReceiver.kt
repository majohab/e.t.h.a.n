package com.example.ethan.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.ethan.MainActivity

class AlarmsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        println("onReceive")
//
        //val requestCode = 0
//
        //val notificationIntent = Intent(context, MainActivity::class.java)
        //val pendingIntent = PendingIntent.getActivity(context, requestCode, notificationIntent, 0)
//
        //val builder = NotificationCompat.Builder(context, "default")
        //    //.setSmallIcon(R.drawable.notification_icon)
        //    .setContentTitle("My App Notification")
        //    .setContentText("This is a simple notification from My App")
        //    .setContentIntent(pendingIntent)
        //    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//
        //val notificationManager = NotificationManagerCompat.from(context)
//
        //val onPermissionGranted: () -> Unit = { builder.let { notificationManager.notify(1, it.build()) } }
//
        //if (ActivityCompat.checkSelfPermission(
        //        context,
        //        Manifest.permission.POST_NOTIFICATIONS
        //    ) != PackageManager.PERMISSION_GRANTED
        //) {
        //    if (context is Activity) {
        //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        //            ActivityCompat.requestPermissions(
        //                context,
        //                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
        //                requestCode
        //            )
        //        }
        //        MainActivity.permissionCallbacks[requestCode] = onPermissionGranted
        //    }
        //    return
        //}
//
        //onPermissionGranted()

        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        notificationManager.sendReminderNotification(
            applicationContext = context,
            channelId = "default"
        )
    }
}

fun NotificationManager.sendReminderNotification(
    applicationContext: Context,
    channelId: String,
) {
    val contentIntent = Intent(applicationContext, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(
        applicationContext,
        1,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    val builder = NotificationCompat.Builder(applicationContext, channelId)
        .setContentTitle("Notification")
        .setContentText("Notification")
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    notify(1, builder.build())
}