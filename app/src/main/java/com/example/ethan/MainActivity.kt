package com.example.ethan

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.example.ethan.notifications.Notifier
import com.example.ethan.sharedprefs.SharedPrefs
import com.example.ethan.ui.gui.GUI.MainScreen
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {

    companion object {
        var permissionCallbacks = mutableMapOf<Int, () -> Unit>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AgentHandler.start()

        SharedPrefs.initSharedPrefs(this) {
            Notifier.setupNotificationChannel()
            Notifier.setupNotificationAlarm(this)
        }

        setContent {
            MainScreen()
        }
    }




    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        println("onRequestPermissionsResult: " + requestCode)
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