package com.example.obirinweather

import retrofit2.http.GET
import retrofit2.http.Query

// Current weather endpoint
interface WeatherApi {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("q") zip: String ,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "imperial"
    ): WeatherData

// Forecast endpoint
    @GET("forecast/daily")
    suspend fun getForecast(
        @Query("zip") zip: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "imperial",
        @Query("cnt") days: Int = 16
    ): ForecastResponse

    @GET("weather")
    suspend fun getWeatherByCoords(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "imperial"
    ): WeatherData

    @GET("forecast/daily")
    suspend fun getForecastByCoords(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("cnt") days: Int,
        @Query("units") units: String = "imperial"
    ): ForecastResponse

}