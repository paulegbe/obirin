package com.example.obirinweather

data class TestForecastResponse(
    val list: List<ForecastDay>,
    val city: CityInfo,
    val cnt: Int? = null,
)

data class ForecastDay(
    val dt: Long,
    val temp: ForecastTemp,
    val weather: List<ForecastWeather>
)

data class ForecastTemp(
    val max: Double,
    val min: Double
)

data class ForecastWeather(
    val main: String,
    val description: String
)
