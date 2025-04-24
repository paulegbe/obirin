package com.example.obirinweather

import kotlinx.serialization.Serializable

@Serializable
data class ForecastResponse(
    val city: CityInfo?,
    val cnt: Int? = null,
    val list: List<DailyForecast>
)

@Serializable
data class CityInfo(
    val name: String,
    val country: String)

@Serializable
data class DailyForecast(
    val dt: Long,
    val temp: Temp,
    val weather: List<WeatherDescription>)

@Serializable
data class Temp(
    val max: Double,
    val min: Double)

@Serializable
data class WeatherDescription(
    val main: String,
    val icon: String,
    val description: String)


