package com.example.ethan.ui.gui

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import com.example.ethan.ui.gui.theme.ETHANTheme
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Mic
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ethan.R
import com.example.ethan.ui.gui.theme.*
import com.example.ethan.ui.speech.Speech2Text
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState


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

        val voicePermissionState = rememberPermissionState(Manifest.permission.RECORD_AUDIO)
        val context = LocalContext.current

        ETHANTheme {
            Box(
                modifier = Modifier
                    .background(DeepBlue)
                    .fillMaxSize()
            ) {
                Column {
                    FloatingActionButton(
                        onClick = {
                            voicePermissionState.launchPermissionRequest()
                            println(voicePermissionState.status.isGranted)
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
                    FeatureSection(
                        features = listOf(
                            Feature(
                                title = "Good Morning",
                                BlueViolet1,
                                BlueViolet2,
                                BlueViolet3
                            ),
                            Feature(
                                title = "Navigation Assistance",
                                LightGreen1,
                                LightGreen2,
                                LightGreen3
                            ),
                            Feature(
                                title = "Lunch Break",
                                OrangeYellow1,
                                OrangeYellow2,
                                OrangeYellow3
                            ),
                            Feature(
                                title = "Social Assistance",
                                Beige1,
                                Beige2,
                                Beige3
                            )
                        )
                    )
                }
            }
        }
    }

    @Composable
    fun FeatureSection(features: List<Feature>) {
        Column(modifier = Modifier.fillMaxWidth()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(start = 7.5.dp, end = 7.5.dp),
                modifier = Modifier.fillMaxHeight()
            ) {
                items(features.size) {
                    FeatureItem(feature = features[it])
                }
            }
        }
    }

    @Composable
    fun FeatureItem(
        feature: Feature
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .padding(7.5.dp)
                .aspectRatio(2f)
                .clip(RoundedCornerShape(10.dp))
                .background(feature.darkColor)
        ) {
            val width = constraints.maxWidth
            val height = constraints.maxHeight

            // Medium colored path
            val mediumColoredPoint1 = Offset(width * 0f, height * 0.3f)
            val mediumColoredPoint2 = Offset(width * 0.1f, height * 0.35f)
            val mediumColoredPoint3 = Offset(width * 0.4f, height * 0.05f)
            val mediumColoredPoint4 = Offset(width * 0.75f, height * 0.7f)
            val mediumColoredPoint5 = Offset(width * 1.4f, -height.toFloat())

            val mediumColoredPath = Path().apply {
                moveTo(mediumColoredPoint1.x, mediumColoredPoint1.y)
                standardQuadFromTo(mediumColoredPoint1, mediumColoredPoint2)
                standardQuadFromTo(mediumColoredPoint2, mediumColoredPoint3)
                standardQuadFromTo(mediumColoredPoint3, mediumColoredPoint4)
                standardQuadFromTo(mediumColoredPoint4, mediumColoredPoint5)
                lineTo(width.toFloat() + 100f, height.toFloat() + 100f)
                lineTo(-100f, height.toFloat() + 100f)
                close()
            }

            val lightPoint1 = Offset(0f, height * 0.35f)
            val lightPoint2 = Offset(width * 0.1f, height * 0.4f)
            val lightPoint3 = Offset(width * 0.3f, height * 0.35f)
            val lightPoint4 = Offset(width * 0.65f, height.toFloat())
            val lightPoint5 = Offset(width * 1.4f, -height.toFloat() / 3f)

            val lightColoredPath = Path().apply {
                moveTo(lightPoint1.x, lightPoint1.y)
                standardQuadFromTo(lightPoint1, lightPoint2)
                standardQuadFromTo(lightPoint2, lightPoint3)
                standardQuadFromTo(lightPoint3, lightPoint4)
                standardQuadFromTo(lightPoint4, lightPoint5)
                lineTo(width.toFloat() + 100f, height.toFloat() + 100f)
                lineTo(-100f, height.toFloat() + 100f)
                close()
            }

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                drawPath(
                    path = mediumColoredPath,
                    color = feature.mediumColor
                )
                drawPath(
                    path = lightColoredPath,
                    color = feature.lightColor
                )
            }

            Box(modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
            ) {
                Text(
                    text = feature.title,
                    style = MaterialTheme.typography.h2,
                    lineHeight = 22.sp,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(end = 60.dp)
                )
                Text(
                    text = "45 min",
                    color = TextWhite,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .clip(RoundedCornerShape(10.dp))
                        .background(DarkerButtonBlue)
                        .padding(vertical = 4.dp, horizontal = 8.dp)
                )
                Text(
                    text = "Start",
                    color = TextWhite,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable {
                            // On click...
                        }
                        .align(Alignment.BottomEnd)
                        .clip(RoundedCornerShape(10.dp))
                        .background(ButtonBlue)
                        .padding(vertical = 4.dp, horizontal = 12.dp)
                )
            }
        }
    }
}