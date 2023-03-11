package com.example.ethan

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ethan.ui.theme.ETHANTheme
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ETHANTheme {
                // A surface container using the 'background' color from the theme
                //Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                //    Greeting("Android")
                //}
                MainScreen()
        }
    }
}



}
@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val materialBlue700= Color(0xFF414649)
    Scaffold(
        topBar = { TopAppBar(title = {Text("E.T.H.A.N", color = Color.White, textAlign = TextAlign.Center)},backgroundColor = materialBlue700)},
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        floatingActionButton = { FloatingActionButton(onClick = {}){
            Icon(
                imageVector = Icons.Filled.Mic,
                contentDescription = "Start Talking",
                tint = Color.White,
            )
        } },
        content = { Text("BodyContent") },
        bottomBar = { BottomAppBar(backgroundColor = materialBlue700) {} }
    )
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ETHANTheme {
        MainScreen()
    }
}