package com.example.obirinweather

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun WeatherNavHost(
    navController: NavHostController,
    viewModel: WeatherViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "current",
        modifier = modifier
    ) {
        composable("current") {
            CurrentWeatherScreen(
                viewModel = viewModel,
                onViewForecastClicked = {
                    navController.navigate("forecast")
                }
            )
        }
        composable("forecast") {
            ForecastScreen(viewModel = viewModel)
        }
    }
}
