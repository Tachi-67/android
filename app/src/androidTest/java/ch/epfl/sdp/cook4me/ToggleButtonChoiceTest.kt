package ch.epfl.sdp.cook4me

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.cook4me.ui.simpleComponent.ToggleButtonChoice
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ToggleButtonChoiceTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun defaultInformationIsDisplayed() {
        composeTestRule.setContent {
            ToggleButtonChoice(
                question = "question",
                possibilities = Pair("option1", "option2"),
                onToggle = {})
        }

        composeTestRule.onNodeWithText("question").assertIsDisplayed()
        composeTestRule.onNodeWithText("option1").assertIsDisplayed()
    }

    @Test
    fun onToggleIsCalledWhenSwitchIsPressed() {
        var toggle = ""
        composeTestRule.setContent {
            ToggleButtonChoice(
                question = "question",
                possibilities = Pair("option1", "option2"),
                onToggle = { toggle = it })
        }

        composeTestRule.onNodeWithTag("switch").performClick()
        assert(toggle == "option2")
        composeTestRule.onNodeWithTag("switch").performClick()
        assert(toggle == "option1")
    }
}