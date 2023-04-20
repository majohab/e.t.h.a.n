package com.example.ethan.usecases

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import com.example.ethan.AgentHandler
import com.example.ethan.sharedprefs.SharedPrefs
import org.json.JSONObject
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.doNothing
import org.mockito.MockitoAnnotations
import java.lang.reflect.Field
import java.lang.reflect.Modifier

class UseCaseTestNavigation {

    lateinit var navigationAssistance: NavigationAssistance

    @Mock
    lateinit var activity: Activity

    @Mock
    lateinit var sharedPrefs: SharedPreferences


    @Before
    fun setUp(){
        navigationAssistance = NavigationAssistance { println("finished") }
        // now you can call the execute method to start the use case
        MockitoAnnotations.openMocks(this)


        val mockEditor = Mockito.mock(SharedPreferences.Editor::class.java)
        Mockito.`when`(activity.getPreferences(Context.MODE_PRIVATE)).thenReturn(sharedPrefs)

        // First time initialization
        Mockito.`when`(sharedPrefs.contains("initialized")).thenReturn(false)
        Mockito.`when`(sharedPrefs.edit()).thenReturn(mockEditor)
        Mockito.`when`(
            mockEditor.putString(
                "08:00",
                AgentHandler.goodMorningDialogue.getResTimeID()
            )
        ).thenReturn(mockEditor)
        Mockito.`when`(
            mockEditor.putString(
                "12:00",
                AgentHandler.lunchBreakConsultant.getResTimeID()
            )
        ).thenReturn(mockEditor)
        Mockito.`when`(
            mockEditor.putString(
                "15:00",
                AgentHandler.navigationAssistance.getResTimeID()
            )
        ).thenReturn(mockEditor)
        Mockito.`when`(mockEditor.putString("18:00", AgentHandler.socialAssistance.getResTimeID())).thenReturn(mockEditor)
        Mockito.`when`(mockEditor.putString("steam_id", "76561198198615839")).thenReturn(mockEditor)
        Mockito.`when`(mockEditor.putBoolean("initialized", true)).thenReturn(mockEditor)
        Mockito.`when`(sharedPrefs.getString("steam_id","")).thenReturn("76561198198615839")
        Mockito.`when`(sharedPrefs.getInt("fav_games_genre",-1)).thenReturn(-1)


        doNothing().`when`(mockEditor).apply()

        SharedPrefs.initSharedPrefs(activity)

        setStaticFieldViaReflection(Build.VERSION::class.java.getField("SDK_INT"), 33)
    }

    @Test
    fun navHandleNextEventsTest1() {

        var t = Thread {
            mockingbird1()
        }.start()
        val event = JSONObject()
        event.put("location", "Lerchenstrasse 1 Stuttgart")
        event.put("startHour", "23")
        event.put("startMinute", "00")
        navigationAssistance.handleNextEvents(nextEvent = event, "cycling-regular")

        println("Test")
    }

    @Test
    fun navHandleNExtEventsTest2() {
        var t = Thread {
            mockingbird2()
        }.start()
        val event = JSONObject()
        event.put("location", "Lerchenstrasse 1 Stuttgart")
        event.put("startHour", "3")
        event.put("startMinute", "00")
        navigationAssistance.handleNextEvents(nextEvent = event, "cycling-regular")

        println("Test")
    }

    @Test
    fun navHandleNextEventsTest3() {
        var t = Thread {
            mockingbird3()
        }.start()
        val event = JSONObject()
        event.put("location", "Lerchenstrasse 1 Stuttgart")
        event.put("startHour", "22")
        event.put("startMinute", "59")
        navigationAssistance.handleNextEvents(nextEvent = event, "foot-walking")

        println("Test")
    }

    @Test
    fun navBestTooLateAlternativeTest1() {
        var t = Thread {
            mockingbird4()
        }.start()

        val event = JSONObject()
        event.put("location", "Lerchenstrasse 1 Stuttgart")
        event.put("startHour", "22")
        event.put("startMinute", "59")
        val estimated_times = mutableMapOf<String, Int>(
            "bike" to 1
        )

        val respone = navigationAssistance.bestTooLateAlternative(1,2,"bike",event, "cycling-regular", estimated_times)
        println("Test")
    }

    @Test
    fun navBestTooLateAlternativeTest2() {
        var t = Thread {
            mockingbird41()
        }.start()

        val event = JSONObject()
        event.put("location", "Lerchenstrasse 1 Stuttgart")
        event.put("startHour", "22")
        event.put("startMinute", "59")
        val estimated_times = mutableMapOf<String, Int>(
            "bike" to 1
        )

        val respone = navigationAssistance.bestTooLateAlternative(1,2,"bike",event, "cycling-regular", estimated_times)
        println("Test")
    }

    @Test
    fun estimatedTimesTest1() {
        var t = Thread {
            navigationAssistance.getExecutionTime()
        }.start()

        val event = JSONObject()
        event.put("location", "Lerchenstrasse 1 Stuttgart")
        event.put("startHour", "22")
        event.put("startMinute", "59")
        val estimated_times = mutableMapOf<String, Int>()

        println("Test")
    }


    @Test
    fun estimatedTimesTest() {
        val event = JSONObject()
        event.put("location", "Lerchenstrasse 1 Stuttgart")
        event.put("startHour", "3")
        event.put("startMinute", "59")
        var t = Thread {

            navigationAssistance.getNextTimeToGo(event)
        }.start()

        val estimated_times = mutableMapOf<String, Int>()
        println("Test")
    }

    @Test
    fun createTestFootwalking() {
        val event = JSONObject()
        event.put("location", "Lerchenstrasse 1 Stuttgart")
        event.put("startHour", "22")
        event.put("startMinute", "59")
        val estimated_times = mutableMapOf<String, Int>(
            "bike" to 1, "foot-walking" to 2, "driving-car" to 3,
            "wheelchair" to 4, "cycling-regular" to 5, "cycling-road" to 6
        )
        var t = Thread {
            mockingbirdFootwalking()
        }.start()

        navigationAssistance.footWalkingAlternative(event ,estimatedTimes = estimated_times, 2)
        println("Test")
    }

    @Test
    fun createTestFootwalking1() {
        val event = JSONObject()
        event.put("location", "Lerchenstrasse 1 Stuttgart")
        event.put("startHour", "22")
        event.put("startMinute", "59")
        val estimated_times = mutableMapOf<String, Int>(
            "bike" to 1, "foot-walking" to 2, "driving-car" to 3,
            "wheelchair" to 4, "cycling-regular" to 5, "cycling-road" to 6
        )
        var t = Thread {
            mockingbirdFootwalking1()
        }.start()

        navigationAssistance.footWalkingAlternative(event ,estimatedTimes = estimated_times, 2)
        println("Test")
    }

    @Test
    fun createTestFootwalking2() {
        val event = JSONObject()
        event.put("location", "Lerchenstrasse 1 Stuttgart")
        event.put("startHour", "22")
        event.put("startMinute", "59")
        val estimated_times = mutableMapOf<String, Int>(
            "bike" to 1, "foot-walking" to 2, "driving-car" to 3,
            "wheelchair" to 4, "cycling-regular" to 5, "cycling-road" to 6
        )
        var t = Thread {
            mockingbirdFootwalking2()
        }.start()

        navigationAssistance.footWalkingAlternative(event ,estimatedTimes = estimated_times, 2)
        println("Test")
    }

    fun mockingbirdFootwalking(){
        mock_waitForAPIs(5000)
        mock_onEthanVoiceOutputFinished(20)
        mock_speakAndHearSelectiveInput("no")
        println("i said no")
        mock_onEthanVoiceOutputFinished(20)
    }

    fun mockingbirdFootwalking1(){
        mock_waitForAPIs(5000)
        mock_onEthanVoiceOutputFinished(20)
        mock_speakAndHearSelectiveInput("bike")
        println("i said bike")
        mock_onEthanVoiceOutputFinished(20)
    }

    fun mockingbirdFootwalking2(){
        mock_waitForAPIs(5000)
        mock_onEthanVoiceOutputFinished(20)
        mock_speakAndHearSelectiveInput("wheelchair")
        println("i said wheelchair")
        mock_onEthanVoiceOutputFinished(20)
    }


    fun mockingbird1(){
        mock_waitForAPIs(2000)
        mock_onEthanVoiceOutputFinished(10)
        mock_speakAndHearSelectiveInput("yes")
        mock_onEthanVoiceOutputFinished(10)
        mock_speakAndHearSelectiveInput("yes")
        mock_onEthanVoiceOutputFinished(10)

        mock_speakAndHearSelectiveInput("yes")
        mock_onEthanVoiceOutputFinished(10)
        mock_speakAndHearSelectiveInput("yes")
        mock_onEthanVoiceOutputFinished(10)

        println("mocking bird done")

    }

    fun mockingbird2(){
        mock_waitForAPIs(7000)
        mock_onEthanVoiceOutputFinished(10)
        mock_speakAndHearSelectiveInput("yes")
        mock_onEthanVoiceOutputFinished(10)
        mock_speakAndHearSelectiveInput("yes")
        mock_onEthanVoiceOutputFinished(10)

        mock_speakAndHearSelectiveInput("yes")
        mock_onEthanVoiceOutputFinished(10)
        mock_speakAndHearSelectiveInput("yes")
        mock_onEthanVoiceOutputFinished(10)

        println("mocking bird done")

    }

    fun mockingbird3(){
        mock_waitForAPIs(7000)
        mock_onEthanVoiceOutputFinished(10)
        mock_speakAndHearSelectiveInput("yes")
        mock_onEthanVoiceOutputFinished(10)
        mock_speakAndHearSelectiveInput("yes")
        mock_onEthanVoiceOutputFinished(10)

        mock_speakAndHearSelectiveInput("yes")
        mock_onEthanVoiceOutputFinished(10)
        mock_speakAndHearSelectiveInput("yes")
        mock_onEthanVoiceOutputFinished(10)

        println("mocking bird done")

    }


    fun mockingbird4(){
        mock_waitForAPIs(5000)
        mock_speakAndHearSelectiveInput("yes")
        println("i said yes")
        mock_onEthanVoiceOutputFinished(20)

        println("mocking bird done")

    }

    fun mockingbird41(){
        mock_waitForAPIs(5000)
        mock_speakAndHearSelectiveInput("no")
        println("i said no")
        mock_onEthanVoiceOutputFinished(20)

        println("mocking bird done")

    }

    fun mock_waitForAPIs(duration: Long) {
        Thread.sleep(duration)
    }

    fun mock_speakAndHearSelectiveInput(userInput: String) {
        mock_onEthanVoiceOutputFinished(1)
        navigationAssistance.onUserVoiceInputReceived(userInput)
        mock_onEthanVoiceOutputFinished(2)
    }

    fun mock_onEthanVoiceOutputFinished(times: Int) {
        for (i in 0 until times) {
            navigationAssistance.onEthanVoiceOutputFinished()
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