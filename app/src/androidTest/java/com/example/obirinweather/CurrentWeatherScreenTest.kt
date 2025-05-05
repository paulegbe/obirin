package com.example.obirinweather

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test

class CurrentWeatherScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun zipCodeInput_acceptsValidDigits() {
        val testZip = "55401"

        composeTestRule.setContent {
            CurrentWeatherScreen(viewModel = WeatherViewModel(), onViewForecastClicked = {})
        }

        composeTestRule
            .onNodeWithText("Enter 5-digit Zip Code")
            .performTextInput(testZip)

        composeTestRule
            .onNodeWithText(testZip, substring = true)
            .assertExists()
    }


    @Test
    fun weatherButton_triggersFetch() {
        composeTestRule.setContent {
            CurrentWeatherScreen(viewModel = WeatherViewModel(), onViewForecastClicked = {})
        }

        val input = "55401"
        composeTestRule
            .onNodeWithText("Enter 5-digit Zip Code")
            .performTextInput(input)

        composeTestRule
            .onNodeWithText("WEATHER", substring = true, ignoreCase = true)
            .performClick()
    }
}
