package com.example.obirinweather

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable



@Serializable
class WeatherData (
    val name: String,
    val main: Main,
    val weather: List<Weather>

)

@Serializable
class Main (
    val temp: Double,
    @SerialName("feels_like") val feelsLike: Double,
    val humidity: Int,
    val pressure: Int,
    @SerialName("temp_min") val tempMin: Double,
    @SerialName("temp_max") val tempMax: Double
)


@Serializable
class Weather(
    val main: String,
    val description: String,
    val icon: String
)