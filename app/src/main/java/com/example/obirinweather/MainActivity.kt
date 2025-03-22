package com.example.obirinweather

import coil.compose.AsyncImage
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.obirinweather.ui.theme.ObirinWeatherTheme

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<WeatherViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // fetch data
        viewModel.fetchWeather("lagos", "acc199caf12c164dc5c1616b7bf123a9")

        setContent {
            ObirinWeatherTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Observe LiveData using compose runtime
                    val weather by viewModel.weatherData.observeAsState()

                    // Only show UI if weather isn't null
                    weather?.let {
                        WeatherScreen(it, modifier = Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherScreen(weather: WeatherData, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top bar
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            color = Color(0xFF935FA7)
        ) {
            Text(
                text = stringResource(R.string.app_name),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            )
        }

        // City Name
        Text(
            text = weather.name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )

        // Temperature + Feels Like + Icon in a row
        Row(
            modifier = Modifier.padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Column for Temp and Feels Like
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.temp_format, weather.main.temp),
                    fontSize = 48.sp
                )
                Text(
                    text = stringResource(R.string.feels_like_format, weather.main.feelsLike),
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Icon next to it
            Image(
                painter = painterResource(R.drawable.sun),
                contentDescription = "Weather Icon",
                modifier = Modifier
                    .size(120.dp)
                    .padding(start = 24.dp)
            )
        }

        // Additional Info
        Column(modifier = Modifier.padding(top = 16.dp)) {
            Text(stringResource(R.string.low_format, weather.main.tempMin), fontSize = 16.sp)
            Text(stringResource(R.string.high_format, weather.main.tempMax), fontSize = 16.sp)
            Text(stringResource(R.string.humidity_format, weather.main.humidity), fontSize = 16.sp)
            Text(stringResource(R.string.pressure_format, weather.main.pressure), fontSize = 16.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherScreenPreview() {
    ObirinWeatherTheme {
        // Just show a placeholder for preview
        // For a full preview, create a mock WeatherData object
        // or ignore for now.
    }
}
