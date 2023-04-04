package com.example.ethan.ui.gui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.platform.LocalContext
import com.example.ethan.ui.speech.Speech2Text
import com.example.ethan.ui.speech.Text2Speech

object Messaging {
    private var messages = mutableStateListOf<Message>(
        Message(
            sender = Sender.ETHAN,
            text = "Good Day!"
        )
    )

    fun addMessage(message: Message)
    {
        messages.add(message)
    }

    fun getMessages(): List<Message> {
        return messages
    }
}

data class Message(
    val sender: Sender,
    val text: String
)

enum class Sender {
    ETHAN,
    USER
}
