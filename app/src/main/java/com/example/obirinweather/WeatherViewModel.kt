package com.example.obirinweather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    // For current conditions
    private val _weatherData = MutableLiveData<WeatherData>()
    val weatherData: LiveData<WeatherData> = _weatherData

    // For forecast
    private val _forecastData = MutableLiveData<ForecastResponse>()
    val forecastData: LiveData<ForecastResponse> = _forecastData

    // For error states (optional)
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun fetchCurrentWeather(zip: String, apiKey: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getCurrentWeather(zip, apiKey)
                _weatherData.value = response
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load current weather."
            }
        }
    }

    fun fetchForecast(zip: String, apiKey: String, days: Int = 7) {
        viewModelScope.launch {
            try {
                val forecastResp = RetrofitInstance.api.getForecast(zip, apiKey, days = days)
                _forecastData.value = forecastResp
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load forecast."
            }
        }
    }

    fun clearForecast() {
        _forecastData.value = null
    }
}

