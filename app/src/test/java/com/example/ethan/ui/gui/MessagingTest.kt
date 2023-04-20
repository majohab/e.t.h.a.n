package com.example.ethan.ui.gui
import androidx.compose.runtime.mutableStateListOf
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MessagingTest {

    private lateinit var messagingObject: Messaging

    @Before
    fun setUp() {
        messagingObject = Messaging
    }

    @Test
    fun testObjectMessagingSender(){
        assertEquals(messagingObject.messages[0].sender, Sender.ETHAN)
    }

    @Test
    fun testObjectMessagingText(){
        assertEquals(messagingObject.messages[0].text, "Good Day!")
    }

    @Test
    fun testObjectMessagingAddMessage(){
        messagingObject.addMessage(Message(Sender.USER,"Test"))
        assertEquals("Test", messagingObject.messages.last().text)
    }

}