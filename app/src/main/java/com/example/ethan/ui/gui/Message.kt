package com.example.ethan.ui.gui

data class Message(
    val sender: Sender,
    val text: String
)

enum class Sender {
    ETHAN,
    USER
}
