package com.example.ethan.ui.gui

import androidx.compose.runtime.mutableStateListOf

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
