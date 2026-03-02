package com.example.inventrytracker

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import com.example.inventrytracker.View.Greeting // Example logic
import org.junit.Rule
import org.junit.Test

/**
 * Instrumented Test Example (UI Testing)
 * This follows your "Greeting" sample style but adds actual UI testing.
 */
class DashboardUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testGreetingDisplay() {
        // This is exactly like your TestActivity but inside a Test Rule
        composeTestRule.setContent {
            // We use your project's theme (InventryTracker Theme)
            com.example.inventrytracker.ui.theme.InventryTrackerTheme {
                Greeting(name = "Inventory User")
            }
        }

        // Verify that the text "Hello Inventory User!" appears on screen
        composeTestRule.onNodeWithText("Hello Inventory User!").assertIsDisplayed()
    }
}
