package com.example.ethan.ui.gui

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.example.ethan.ui.gui.theme.ETHANTheme
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import com.example.ethan.UseCases.GoodMorning
import com.example.ethan.ui.speech.Speech2Text
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.util.*


object GUI : ComponentActivity() {

    private var textInput by mutableStateOf ("Top on the microphone and say something")

    @Composable
    fun Greeting(name: String) {
        Text(text = "Hello $name!")
    }

    @OptIn(ExperimentalPermissionsApi::class)
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun MainScreen() {

        val materialBlue700= Color(0xFF414649)
        val voicePermissionState = rememberPermissionState(Manifest.permission.RECORD_AUDIO)
        val internetPermissionState = rememberPermissionState(Manifest.permission.INTERNET)
        val context = LocalContext.current

        ETHANTheme {
            Scaffold(
                topBar = { TopAppBar(title = {Text("E.T.H.A.N", color = Color.White, textAlign = TextAlign.Center)},backgroundColor = materialBlue700)},
                floatingActionButtonPosition = FabPosition.Center,
                isFloatingActionButtonDocked = true,
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            voicePermissionState.launchPermissionRequest()
                            internetPermissionState.launchPermissionRequest()

                            println(voicePermissionState.status.isGranted)
                            val gm = GoodMorning()
                            gm.execute()

                            if (voicePermissionState.status.isGranted) {
                                Speech2Text.recordInput(context)
                                { input: String ->
                                    textInput = input
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Mic,
                            contentDescription = "Start Talking",
                            tint = Color.White,
                        )
                    }
                },
                content = { Text(textInput) },
                bottomBar = { BottomAppBar(backgroundColor = materialBlue700) {} }
            )
        }
    }
}