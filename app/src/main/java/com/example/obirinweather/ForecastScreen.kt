package com.example.obirinweather

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ForecastScreen(viewModel: WeatherViewModel) {
    val forecast by viewModel.forecastData.observeAsState()

    val iconMap = mapOf(
        "Clear" to R.drawable.sun,
        "Clouds" to R.drawable.cloud,
        "Rain" to R.drawable.rain,
        "Thunderstorm" to R.drawable.thunderstorm,
        "Snow" to R.drawable.snow,
        "Mist" to R.drawable.mist,
        "Fog" to R.drawable.mist,
        "Haze" to R.drawable.mist,
        "Smoke" to R.drawable.mist,
        "Drizzle" to R.drawable.rain
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .blur(8.dp)
                    .alpha(0.7f)
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.07f))
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 12.dp, end = 12.dp, top = 40.dp, bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            forecast?.let { forecastResponse ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(forecastResponse.list.take(16)) { day ->
                        val iconId = iconMap[day.weather.firstOrNull()?.main ?: ""] ?: R.drawable.sun
                        ForecastDayItem(day, iconId)
                    }
                }
            } ?: run {
                Text(text = "No forecast data yet", color = Color.White)
            }
        }
    }
}

@Composable
fun ForecastDayItem(daily: DailyForecast, iconId: Int) {
    val dateFormat = SimpleDateFormat("EEE, MMM d", Locale.getDefault())
    val dateText = dateFormat.format(daily.dt * 1000)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.1f), shape = RoundedCornerShape(6.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = dateText, fontSize = 14.sp, color = Color.White)
        Image(
            painter = painterResource(id = iconId),
            contentDescription = "Weather Icon",
            modifier = Modifier.size(32.dp)
        )
        Column(horizontalAlignment = Alignment.End) {
            Text(text = "${daily.temp.min.toInt()}° / ${daily.temp.max.toInt()}°", color = Color.White, fontSize = 13.sp)
            daily.weather.firstOrNull()?.let { w ->
                Text(text = "${w.main} - ${w.description}", color = Color.White, fontSize = 11.sp)
            }
        }
    }
}

