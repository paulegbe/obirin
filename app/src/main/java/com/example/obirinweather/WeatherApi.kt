package com.example.obirinweather

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("weather")
    suspend fun getCurrentWeather(
    @Query("q") cityName: String,
    @Query("appid") apiKey: String,
    @Query("units") units: String = "imperial"
    ): WeatherData
}