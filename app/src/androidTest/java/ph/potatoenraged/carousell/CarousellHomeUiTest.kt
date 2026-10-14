package ph.potatoenraged.carousell

import androidx.compose.ui.test.assertDoesNotExist
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.onNode
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.Espresso.pressBack
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CarousellHomeUiTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun searchStaysInsideNativeAppAndCanBeEdited() {
        composeRule.onNode(hasSetTextAction()).performTextInput("iPhone 13")
        composeRule.onNodeWithText("Search marketplace").performClick()

        composeRule.onNodeWithText("Native results screen").assertIsDisplayed()
        composeRule.onNodeWithText("iPhone 13").assertIsDisplayed()
        composeRule.onNodeWithText("Edit search").performClick()

        composeRule.onNodeWithText("Search marketplace").assertIsDisplayed()
        composeRule.onNodeWithText("iPhone 13").assertIsDisplayed()
    }

    @Test
    fun systemBackReturnsFromSearchToHome() {
        composeRule.onNode(hasSetTextAction()).performTextInput("system-back-test")
        composeRule.onNodeWithText("Search marketplace").performClick()
        composeRule.onNodeWithText("Native results screen").assertIsDisplayed()

        pressBack()

        composeRule.onNodeWithText("Carousell Companion").assertIsDisplayed()
        composeRule.onNodeWithText("Search marketplace").assertIsDisplayed()
    }

    @Test
    fun categoryShortcutOpensNativeResults() {
        composeRule.onNodeWithText("Electronics").performClick()

        composeRule.onNodeWithText("Native results screen").assertIsDisplayed()
        composeRule.onNodeWithText("Electronics").assertIsDisplayed()
    }

    @Test
    fun submittedSearchAppearsInHistoryAndCanBeRemoved() {
        val term = "history-regression-needle"
        composeRule.onNode(hasSetTextAction()).performTextInput(term)
        composeRule.onNodeWithText("Search marketplace").performClick()
        composeRule.onNodeWithContentDescription("Back").performClick()

        composeRule.onNodeWithText("Recent searches").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Remove $term").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Remove $term").performClick()
        composeRule.onNodeWithContentDescription("Clear search").performClick()
        composeRule.onNodeWithText(term).assertDoesNotExist()
    }

    @Test
    fun emptySearchDoesNotOpenResults() {
        composeRule.onNodeWithText("Search marketplace").performClick()
        composeRule.onNodeWithText("Native results screen").assertDoesNotExist()
        composeRule.onNodeWithText("Carousell Companion").assertIsDisplayed()
    }

    @Test
    fun darkModePreferenceSurvivesActivityRecreation() {
        composeRule.activityRule.scenario.onActivity { activity ->
            activity.getSharedPreferences("carousell_companion", 0)
                .edit()
                .putBoolean("dark_mode", false)
                .commit()
        }
        composeRule.activityRule.scenario.recreate()

        composeRule.onNodeWithContentDescription("Toggle dark mode").performClick()
        composeRule.activityRule.scenario.onActivity { activity ->
            assertTrue(activity.getSharedPreferences("carousell_companion", 0)
                .getBoolean("dark_mode", false))
        }

        composeRule.activityRule.scenario.recreate()
        composeRule.onNodeWithContentDescription("Toggle dark mode").assertIsDisplayed()
        composeRule.activityRule.scenario.onActivity { activity ->
            assertTrue(activity.getSharedPreferences("carousell_companion", 0)
                .getBoolean("dark_mode", false))
        }
    }
}
