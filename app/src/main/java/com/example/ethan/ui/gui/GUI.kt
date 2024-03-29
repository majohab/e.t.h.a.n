package com.example.ethan.ui.gui

import android.Manifest
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Text
import com.example.ethan.ui.gui.theme.ETHANTheme
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ethan.AgentHandler
import com.example.ethan.sharedprefs.SharedPrefs
import com.example.ethan.ui.gui.theme.*
import com.example.ethan.ui.speech.Speech2Text
import com.example.ethan.ui.speech.Text2Speech
import com.example.ethan.usecases.GoodMorningDialogue
import com.example.ethan.usecases.LunchBreakConsultant
import com.example.ethan.usecases.NavigationAssistance
import com.example.ethan.usecases.SocialAssistance
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.util.regex.Pattern


object GUI : ComponentActivity() {

    private var textInput by mutableStateOf ("Tap on the microphone and say something")
    private var micRms by mutableStateOf(0.1f)
    private var username = "John"

    @Composable
    fun MainScreen() {

        // Load username from sharedPrefs
        username = SharedPrefs.getString("username", "John")

        ETHANTheme {
            Box(modifier = Modifier.background(DeepBlue).fillMaxSize()) {
                Column(
                    modifier = Modifier.align(Alignment.TopCenter).padding(horizontal = 15.dp, vertical = 0.dp), // Can't add vertical spacing, because we need the mic animation to go to the bottom
                    verticalArrangement = Arrangement.spacedBy(15.dp) // Space between
                )
                {
                    Spacer(
                        modifier = Modifier.height(15.dp)
                    )

                    WelcomeText() // Top Bar

                    Chat() // Chat with Ethan

                    FeatureSection( // Four Buttons
                        features = listOf(
                            Feature(
                                GoodMorningDialogue::class,
                                title = "Good Morning",
                                BlueViolet1,
                                BlueViolet2,
                                BlueViolet3
                            ) {
                                AgentHandler.startUseCase(GoodMorningDialogue::class)
                            },
                            Feature(
                                NavigationAssistance::class,
                                title = "Navigation Assistance",
                                LightGreen1,
                                LightGreen2,
                                LightGreen3
                            ) {
                                AgentHandler.startUseCase(NavigationAssistance::class)
                            },
                            Feature(
                                LunchBreakConsultant::class,
                                title = "Lunch Break",
                                OrangeYellow1,
                                OrangeYellow2,
                                OrangeYellow3
                            ) {
                                AgentHandler.startUseCase(LunchBreakConsultant::class)
                            },
                            Feature(
                                SocialAssistance::class,
                                title = "Social Assistance",
                                Beige1,
                                Beige2,
                                Beige3
                            ) {
                                AgentHandler.startUseCase(SocialAssistance::class)
                            }
                        )
                    )

                    BottomBar()
                }
            }
        }
    }

    @Composable
    fun WelcomeText() {
        val time = "Morning"
        Column()
        {
            Text(
                text = "Good $time, $username.",
                style = Typography.h1
            )
            Text(
                text = "I wish you have a good day!",
                style = Typography.h2,
                color = DarkerButtonBlue
            )
        }
    }

    @Composable
    fun ColumnScope.Chat() {

        val backgroundColor = Color.Transparent
        val messages = Messaging.messages
        val listState = LazyListState(messages.size - 1,
                                        100)


        // Read last message
        if (messages.isNotEmpty()) {
            val lastMsg = messages[messages.size - 1]
            if (lastMsg.sender == Sender.ETHAN) {
                Text2Speech.speakText(lastMsg.text, LocalContext.current)
            }
        }

        Box(
            modifier = Modifier
                .weight(1f) // To fill remaining height of Column
                //.padding(bottom = 330.dp, start = 15.dp, end = 15.dp, top = 25.dp)
                //.clip(RoundedCornerShape(10.dp))
                .background(backgroundColor)
        )
        {
            Divider(
                color = ButtonBlue,
                thickness = 1.dp,
                modifier = Modifier
                    .align(Alignment.TopCenter)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 15.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp),
                state = listState
            )
            {
                items(messages.size) {
                    MessageCard(messages[it])
                }
            }

            /* Text Input Field. Decided against it for now.
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 15.dp),
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = { Text(text = "Type in a message...") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, ),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = DarkerButtonBlue,
                    focusedBorderColor = ButtonBlue
                ),
                textStyle = Typography.h3,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.QuestionAnswer,
                        contentDescription = "Start typing",
                        tint = Color.White
                    )
                }
            )
             */

            Divider(
                color = ButtonBlue,
                thickness = 1.dp,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            )
        }
    }

    @Composable
    fun MessageCard(message: Message) {

        val meColor = BlueViolet3
        val ethanColor = LightRed
        val uriColor = ButtonBlue

        // UriHandler parse and opens URI inside AnnotatedString Item in Browse
        val uriHandler = LocalUriHandler.current

        // annotatedString is necessary to highlight URIs and make them clickable
        val annotatedString = buildAnnotatedString {
            val plainText = message.text

            val uriPattern = Pattern.compile("(?i)\\b((?:https?://|www\\d{0,3}[.]|[a-z0-9.\\-]+[.][a-z]{2,4}/)(?:[^\\s()<>]+|\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\))+(?:\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\)|[^\\s`!()\\[\\]{};:'\".,<>?«»“”‘’]))")
            var start = 0

            val matcher = uriPattern.matcher(plainText)
            while (matcher.find()) {
                val uriStart = matcher.start()
                val uriEnd = matcher.end()
                if (uriStart > start) {
                    append(plainText.substring(start, uriStart))
                }
                val uriString = plainText.substring(uriStart, uriEnd)
                pushStringAnnotation(tag = "URI", annotation = uriString)
                withStyle(style = SpanStyle(color = uriColor)) {
                    append(uriString)
                }
                start = uriEnd
            }
            if (start < plainText.length) {
                append(plainText.substring(start))
            }
        }


        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = when (message.sender) {
                Sender.ETHAN -> Alignment.Start
                else -> Alignment.End
            }
        ) {
            Card(
                modifier = Modifier.widthIn(max = 300.dp),
                shape = CardShapeFor(message),
                backgroundColor = when (message.sender) {
                    Sender.USER -> meColor
                    else -> ethanColor
                },
            ) {
                ClickableText(
                    modifier = Modifier.padding(7.5.dp),
                    text = annotatedString,
                    style = Typography.h3,
                    onClick = {offset ->
                        annotatedString.getStringAnnotations(tag = "URI", start = offset, end = offset).firstOrNull()?.let { stringAnnotation ->
                            uriHandler.openUri(stringAnnotation.item)
                        }
                    }
                )
            }
            Text (
                text = when (message.sender) {
                    Sender.USER -> username
                    else -> "E.T.H.A.N"
                },
                style = Typography.h3
            )
        }
    }

    @Composable
    fun CardShapeFor(message: Message): Shape {
        val roundedCorners = RoundedCornerShape(10.dp)
        return when (message.sender) {
            Sender.USER -> roundedCorners.copy(bottomEnd = CornerSize(0))
            else -> roundedCorners.copy(bottomStart = CornerSize((0)))
        }
    }

    @Composable
    fun FeatureSection(features: List<Feature>) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(15.dp),
                horizontalArrangement = Arrangement.spacedBy(15.dp)
                //contentPadding = PaddingValues(start = 7.5.dp, end = 7.5.dp)
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
                //.padding(7.5.dp)
                .aspectRatio(2f)
                .clip(RoundedCornerShape(10.dp))
                .background(feature.darkColor)
        ) {
            val width = constraints.maxWidth
            val height = constraints.maxHeight

            // BUTTON BACKGROUND

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

            // Light colored Path
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
                    style = Typography.h2,
                    lineHeight = 22.sp,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(end = 60.dp)
                )
                Text(
                    text = AgentHandler.remainingTimeStrings[feature.useCase].toString(),
                    style = Typography.h4,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .clip(RoundedCornerShape(10.dp))
                        .background(DarkerButtonBlue)
                        .padding(vertical = 4.dp, horizontal = 8.dp)
                        .width(38.dp)
                )
                Text(
                    text = "Start",
                    style = Typography.h4,
                    modifier = Modifier
                        .clickable {
                            feature.onClicked?.invoke()
                        }
                        .align(Alignment.BottomEnd)
                        .clip(RoundedCornerShape(10.dp))
                        .background(ButtonBlue)
                        .padding(vertical = 4.dp, horizontal = 12.dp)
                )
            }
        }
    }

    @Composable
    fun BottomBar() {

        val backgroundColor = Color.Transparent
        val soundWaveColor = LightRed
        val soundWaveMultiplier = 50

        Box(
            modifier = Modifier
                //.padding(horizontal = 15.dp)
                .fillMaxWidth()
                //.clip(RoundedCornerShape(10.dp))
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(soundWaveColor, backgroundColor),
                        radius = maxOf(soundWaveMultiplier * micRms, 0.1f),
                    )
                )
        )
        {
            Divider(
                color = ButtonBlue,
                thickness = 1.dp
            )

            MicButton()
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun BoxScope.MicButton() {
        val voicePermissionState = rememberPermissionState(Manifest.permission.RECORD_AUDIO)
        val context = LocalContext.current
        val defaultBackgroundColor = ButtonBlue
        val activeBackgroundColor = LightRed
        var backgroundColor by remember { mutableStateOf(defaultBackgroundColor) }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = 25.dp, top = 15.dp),
            backgroundColor = backgroundColor,
            onClick = {
                voicePermissionState.launchPermissionRequest() // Ask for permission

                if (voicePermissionState.status.isGranted) {
                    Speech2Text.recordInput(
                        context = context,
                        onStart = { backgroundColor = activeBackgroundColor },
                        onRmsChanged = { value: Float -> // Sound level of the audio stream failed
                            micRms = value
                                       },
                        onFinished_Frontend = { input: String ->
                            textInput = input
                            backgroundColor = defaultBackgroundColor
                            Messaging.addMessage(Message(
                                sender = Sender.USER,
                                text = input
                            ))
                        },
                        onError_Frontend = {
                            backgroundColor = defaultBackgroundColor
                        }
                    )
                }
            }
        ) {
            Icon(
                imageVector = Icons.Filled.Mic,
                contentDescription = "Start Talking",
                tint = Color.White,
            )
        }
    }
}