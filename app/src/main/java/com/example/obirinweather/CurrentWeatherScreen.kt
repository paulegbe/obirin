package com.example.obirinweather

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun CurrentWeatherScreen(
    viewModel: WeatherViewModel,
    onViewForecastClicked: () -> Unit
) {
    val weather by viewModel.weatherData.observeAsState()
    var zipCode by remember { mutableStateOf("") }
    val forecast by viewModel.forecastData.observeAsState()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val iconMap = mapOf(
        "Clear" to R.drawable.sun,
        "Clouds" to R.drawable.cloud,
        "Rain" to R.drawable.rain,
        "Thunderstorm" to R.drawable.thunderstorm,
        "Snow" to R.drawable.snow,
        "Mist" to R.drawable.mist
    )

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.POST_NOTIFICATIONS
                    ),
                    101
                )
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .blur(8.dp)
                    .alpha(0.6f)
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.10f))
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            Text(
                text = "What's Today's Weather?",
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = zipCode,
                    onValueChange = { newVal ->
                        if (newVal.length <= 5 && newVal.all { it.isDigit() }) {
                            zipCode = newVal
                        }
                    },
                    label = { Text("Enter 5-digit Zip Code", color = Color.White) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        cursorColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                    ),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

                        if (ActivityCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                                if (location != null) {
                                    val lat = location.latitude
                                    val lon = location.longitude
                                    viewModel.fetchCurrentWeatherByCoords(lat, lon, "acc199caf12c164dc5c1616b7bf123a9")
                                    viewModel.fetchForecastByCoords(lat, lon, "acc199caf12c164dc5c1616b7bf123a9", days = 16)


                                    val intent = Intent(context, LocationService::class.java)
                                    context.startForegroundService(intent)
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.MyLocation,
                        contentDescription = "My Location",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        if (zipCode.length == 5) {
                            viewModel.clearForecast()
                            val fullZip = "$zipCode,us"
                            viewModel.fetchCurrentWeather(fullZip, "acc199caf12c164dc5c1616b7bf123a9")
                            viewModel.fetchForecast(fullZip, "acc199caf12c164dc5c1616b7bf123a9", days = 16)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.15f)
                    ),
                    border = BorderStroke(1.dp, Color.White)
                ) {
                    Text(
                        "WEATHER",
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 12.sp,
                        lineHeight = 24.sp,
                        letterSpacing = 0.5.sp
                    )
                }

                Button(
                    onClick = {
                        if (zipCode.length == 5) {
                            viewModel.fetchForecast("$zipCode,us", "acc199caf12c164dc5c1616b7bf123a9", days = 16)
                            onViewForecastClicked()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.15f)
                    ),
                    border = BorderStroke(1.dp, Color.White)
                ) {
                    Text(
                        "FORECAST",
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 12.sp,
                        lineHeight = 24.sp,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            weather?.let {
                val iconId = iconMap[it.weather.firstOrNull()?.main ?: ""] ?: R.drawable.sun

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp, start = 16.dp, end = 16.dp)
                        .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "City:   ${it.name}", color = Color.White)
                            Text(text = "Temp:   ${it.main.temp}°F", color = Color.White)
                            Text(text = "Feels like:   ${it.main.feelsLike}°F", color = Color.White)
                        }
                        Image(
                            painter = painterResource(id = iconId),
                            contentDescription = "Weather Icon",
                            modifier = Modifier.size(80.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            forecast?.let { forecastData ->
                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    forecastData.list.take(12).forEachIndexed { index, day ->
                        val iconId = iconMap[day.weather.firstOrNull()?.main ?: ""] ?: R.drawable.sun
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("${index + 1}PM", color = Color.White, fontSize = 12.sp)
                            Image(
                                painter = painterResource(id = iconId),
                                contentDescription = null,
                                modifier = Modifier.size(32.dp)
                            )
                            Text("${day.temp.max.toInt()}°", color = Color.White, fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Column(modifier = Modifier.fillMaxWidth()) {
                    forecastData.list.take(16).forEach { day ->
                        val iconId = iconMap[day.weather.firstOrNull()?.main ?: ""] ?: R.drawable.sun
                        val chance = if (day.weather.firstOrNull()?.main in listOf("Rain", "Snow")) {
                            "50%"
                        } else ""

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val dateFormat = SimpleDateFormat("EEE", Locale.getDefault())
                            val dateText = dateFormat.format(day.dt * 1000)
                            Text(dateText, color = Color.White)
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Image(
                                    painter = painterResource(id = iconId),
                                    contentDescription = null,
                                    modifier = Modifier.size(40.dp)
                                )
                                if (chance.isNotEmpty()) {
                                    Text(text = chance, fontSize = 12.sp, color = Color.White)
                                }
                            }
                            Text(text = "${day.temp.max.toInt()}°", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
