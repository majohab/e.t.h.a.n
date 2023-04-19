package com.example.ethan

import androidx.compose.ui.test.hasTestTag
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import androidx.compose.ui.*
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.ethan", appContext.packageName)
    }

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testButtonClick() {
        val gmStart = composeTestRule.onNode(hasTestTag("GoodMorningDialogue_invoke"))
        val naStart = composeTestRule.onNode(hasTestTag("NavigationAssistance_invoke"))
        val lbcMorningStart = composeTestRule.onNode(hasTestTag("LunchBreakConsultant_invoke"))
        val saStart = composeTestRule.onNode(hasTestTag("SocialAssistance_invoke"))
        gmStart.assertIsDisplayed()
        naStart.assertIsDisplayed()
        lbcMorningStart.assertIsDisplayed()
        saStart.assertIsDisplayed()
    }
}