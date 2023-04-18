package com.example.ethan.usecases

import android.content.Context
import android.os.Build
import com.example.ethan.sharedprefs.SharedPrefs
import com.example.ethan.ui.gui.GUI.getPreferences
import com.example.ethan.ui.gui.GUI.getSharedPreferences
import org.junit.Before
import org.junit.Test
import java.lang.reflect.Field
import java.lang.reflect.Modifier


class GoodMorningDialogueTest{

    private lateinit var goodMorningDialogue: GoodMorningDialogue

    @Before
    fun setUp(){
        goodMorningDialogue = GoodMorningDialogue {
            // this code will be executed when the use case is finished
            println("Good morning dialogue is finished!")
        }

        // now you can call the execute method to start the use case

    }

    @Test
    fun createTest() {
        setStaticFieldViaReflection(Build.VERSION::class.java.getField("SDK_INT"), 33)

        var t = Thread {
            mock_waitForAPIs(5000)
            mock_onEthanVoiceOutputFinished(10)
            mock_speakAndHearSelectiveInput("car")
        }.start()

        goodMorningDialogue.executeUseCase()


        println("Test")
    }

    fun mock_waitForAPIs(duration: Long) {
        Thread.sleep(duration)
    }

    fun mock_speakAndHearSelectiveInput(userInput: String) {
        mock_onEthanVoiceOutputFinished(1)
        goodMorningDialogue.onUserVoiceInputReceived(userInput)
        mock_onEthanVoiceOutputFinished(2)
    }

    fun mock_onEthanVoiceOutputFinished(times: Int) {
        for (i in 0 until times) {
            goodMorningDialogue.onEthanVoiceOutputFinished()
            if (i < times -1)Thread.sleep(100)
        }
    }

    private fun setStaticFieldViaReflection(field: Field, value: Any) {
        field.isAccessible = true
        Field::class.java.getDeclaredField("modifiers").apply {
            isAccessible = true
            setInt(field, field.modifiers and Modifier.FINAL.inv())
        }
        field.set(null, value)
    }
}