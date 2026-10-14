package ph.potatoenraged.carousell

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CarousellHomeUiTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun searchStaysInsideNativeAppAndCanBeEdited() {
        composeRule.onNodeWithText("What are you looking for?").performClick()
        composeRule.onNodeWithText("What are you looking for?").performTextInput("iPhone 13")
        composeRule.onNodeWithText("Search marketplace").performClick()

        composeRule.onNodeWithText("Native results screen").assertIsDisplayed()
        composeRule.onNodeWithText("iPhone 13").assertIsDisplayed()
        composeRule.onNodeWithText("Edit search").performClick()

        composeRule.onNodeWithText("Search marketplace").assertIsDisplayed()
        composeRule.onNodeWithText("iPhone 13").assertIsDisplayed()
    }

    @Test
    fun categoryShortcutOpensNativeResults() {
        composeRule.onNodeWithText("Electronics").performClick()

        composeRule.onNodeWithText("Native results screen").assertIsDisplayed()
        composeRule.onNodeWithText("Electronics").assertIsDisplayed()
    }

    @Test
    fun darkModeToggleIsAccessible() {
        composeRule.onNodeWithContentDescription("Toggle dark mode").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Toggle dark mode").performClick()
        composeRule.onNodeWithContentDescription("Toggle dark mode").assertIsDisplayed()
    }
}
