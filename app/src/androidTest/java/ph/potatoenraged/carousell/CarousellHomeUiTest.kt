package ph.potatoenraged.carousell

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.Espresso.pressBack
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CarousellHomeUiTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun resetAppPreferences() {
        composeRule.activityRule.scenario.onActivity { activity ->
            activity.getSharedPreferences("carousell_companion", 0)
                .edit()
                .remove("recent_searches")
                .commit()
        }
        composeRule.activityRule.scenario.recreate()
    }

    @Test
    fun searchStaysInsideNativeAppAndCanBeEdited() {
        composeRule.onNode(hasSetTextAction()).performTextInput("iPhone 13")
        composeRule.onNodeWithText("Search marketplace").performClick()

        composeRule.onNodeWithText("Native results screen").assertIsDisplayed()
        composeRule.onNodeWithText("iPhone 13").assertIsDisplayed()
        composeRule.onNodeWithText("Edit search").performClick()

        composeRule.onNodeWithText("Search marketplace").assertIsDisplayed()
        composeRule.onNode(hasSetTextAction()).assertTextEquals("iPhone 13")
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
    fun everyCategoryShortcutOpensNativeResults() {
        listOf("Electronics", "Fashion", "Home & Living", "Hobbies", "Vehicles").forEach { category ->
            composeRule.onNodeWithText(category).performClick()

            composeRule.onNodeWithText("Native results screen").assertIsDisplayed()
            composeRule.onNodeWithText(category).assertIsDisplayed()
            composeRule.onNodeWithContentDescription("Back").performClick()
            composeRule.onNodeWithText("Search marketplace").assertIsDisplayed()
        }
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
    fun clearAllRecentSearchesRemovesHistorySection() {
        listOf("clear-history-alpha", "clear-history-beta").forEachIndexed { index, term ->
            if (index > 0) {
                composeRule.onNodeWithContentDescription("Clear search").performClick()
            }
            composeRule.onNode(hasSetTextAction()).performTextInput(term)
            composeRule.onNodeWithText("Search marketplace").performClick()
            composeRule.onNodeWithContentDescription("Back").performClick()
        }

        composeRule.onNodeWithText("Recent searches").assertIsDisplayed()
        composeRule.onNodeWithText("Clear").performClick()

        composeRule.onNodeWithText("Recent searches").assertDoesNotExist()
        composeRule.onNodeWithContentDescription("Remove clear-history-alpha").assertDoesNotExist()
        composeRule.onNodeWithContentDescription("Remove clear-history-beta").assertDoesNotExist()
    }

    @Test
    fun recentSearchPersistsAcrossActivityRecreation() {
        val term = "persisted-history-regression"
        composeRule.onNode(hasSetTextAction()).performTextInput(term)
        composeRule.onNodeWithText("Search marketplace").performClick()
        composeRule.onNodeWithContentDescription("Back").performClick()

        composeRule.activityRule.scenario.recreate()

        composeRule.onNodeWithText("Recent searches").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Remove $term").assertIsDisplayed()
    }

    @Test
    fun emptySearchDoesNotOpenResults() {
        composeRule.onNodeWithText("Search marketplace").performClick()
        composeRule.onNodeWithText("Native results screen").assertDoesNotExist()
        composeRule.onNodeWithText("Carousell Companion").assertIsDisplayed()
    }

    @Test
    fun darkModePreferenceSurvivesActivityRecreation() {
        var initialDarkMode = false
        composeRule.activityRule.scenario.onActivity { activity ->
            initialDarkMode = activity.getSharedPreferences("carousell_companion", 0)
                .getBoolean("dark_mode", false)
        }

        val initialAction = if (initialDarkMode) "Switch to light mode" else "Switch to dark mode"
        val toggledAction = if (initialDarkMode) "Switch to dark mode" else "Switch to light mode"
        composeRule.onNodeWithContentDescription(initialAction).assertIsDisplayed()
        composeRule.onNodeWithContentDescription(initialAction).performClick()
        composeRule.onNodeWithContentDescription(toggledAction).assertIsDisplayed()
        composeRule.activityRule.scenario.onActivity { activity ->
            assertTrue(
                activity.getSharedPreferences("carousell_companion", 0)
                    .getBoolean("dark_mode", false) != initialDarkMode
            )
        }

        composeRule.activityRule.scenario.recreate()
        composeRule.onNodeWithContentDescription(toggledAction).assertIsDisplayed()
        composeRule.activityRule.scenario.onActivity { activity ->
            assertTrue(
                activity.getSharedPreferences("carousell_companion", 0)
                    .getBoolean("dark_mode", false) != initialDarkMode
            )
        }
    }
}
