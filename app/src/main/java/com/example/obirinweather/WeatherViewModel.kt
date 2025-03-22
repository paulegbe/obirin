package com.example.obirinweather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val _weatherData = MutableLiveData<WeatherData>()
    val weatherData: LiveData<WeatherData> = _weatherData

    fun fetchWeather(cityName: String, apiKey: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getCurrentWeather(cityName, apiKey)
                _weatherData.value = response
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Failed to fetch weather", e)
            }
        }
    }
}