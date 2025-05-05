package com.example.obirinweather

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import kotlin.test.assertEquals
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: WeatherViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = WeatherViewModel()

        // Inject fake API
        val fakeApi = object : WeatherApi {
            override suspend fun getCurrentWeather(zip: String, apiKey: String, units: String): WeatherData {
                return WeatherData("Minneapolis", Main(
                    70.0, 68.0,
                    humidity = 50,
                    pressure = 1012,
                    tempMin = 60.0,
                    tempMax = 75.0
                ), listOf(Weather(
                    "Clear", "clear sky",
                    icon = "01d"
                )))
            }

            override suspend fun getForecast(
                zip: String, apiKey: String, units: String, days: Int
            ): ForecastResponse {
                return ForecastResponse(
                    city = CityInfo(name = "Minneapolis", country = "US"),
                    cnt = 1,
                    list = listOf(
                        DailyForecast(
                            dt = 0,
                            temp = Temp(75.0, 55.0),
                            weather = listOf(WeatherDescription(
                                main = "Rain",
                                description = "scattered clouds",
                                icon = "03d"
                            ))
                        )
                    )
                )
            }

            override suspend fun getWeatherByCoords(lat: Double, lon: Double, apiKey: String, units: String): WeatherData {
                return WeatherData("Chicago", Main(
                    65.0, 64.0,
                    humidity = 60,
                    pressure = 1015,
                    tempMin = 58.0,
                    tempMax = 70.0
                ), listOf(Weather(
                    "Rain", "light rain",
                    icon = "10d"
                )))
            }

            override suspend fun getForecastByCoords(lat: Double, lon: Double, apiKey: String, days: Int, units: String): ForecastResponse {
                return ForecastResponse(
                    city = CityInfo(name = "Chicago", country = "US"),
                    cnt = 1,
                    list = listOf(
                        DailyForecast(
                            dt = 0,
                            temp = Temp(80.0, 60.0),
                            weather = listOf(
                                WeatherDescription(
                                    main = "Rain",   // 👈 Change this to "Rain"
                                    description = "moderate rain",
                                    icon = "10d"
                        )))
                    )
                )
            }
        }

        RetrofitInstance.api = fakeApi
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun fetchCurrentWeather_setsWeatherData() = runTest {
        viewModel.fetchCurrentWeather("55401,us", "dummy_key")
        testDispatcher.scheduler.advanceUntilIdle()

        val result = viewModel.weatherData.getOrAwaitValue()
        assertEquals("Minneapolis", result.name)
        assertEquals(70.0, result.main.temp)
    }

    @Test
    fun fetchForecast_setsForecastData() = runTest {
        viewModel.fetchForecast("10001,us", "test_key")
        testDispatcher.scheduler.advanceUntilIdle()

        val result = viewModel.forecastData.getOrAwaitValue()
        assertEquals("Rain", result?.list?.first()?.weather?.first()?.main)
    }



    @Test
    fun fetchCurrentWeatherByCoords_setsWeatherData() = runTest {
        viewModel.fetchCurrentWeatherByCoords(44.98, -93.27, "dummy_key")
        testDispatcher.scheduler.advanceUntilIdle()

        val result = viewModel.weatherData.getOrAwaitValue()
        assertEquals("Chicago", result.name)
    }

    @Test
    fun fetchForecastByCoords_setsForecastData() = runTest {
        viewModel.fetchForecastByCoords(44.98, -93.27, "dummy_key")
        testDispatcher.scheduler.advanceUntilIdle()

        val result = viewModel.forecastData.getOrAwaitValue()
        assertEquals("Rain", result?.list?.first()?.weather?.first()?.main)
    }

    @Test
    fun clearForecast_setsForecastDataToNull() {
        viewModel.clearForecast()
        assertNull(viewModel.forecastData.value)
    }
}
