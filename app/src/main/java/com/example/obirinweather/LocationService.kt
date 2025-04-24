package com.example.obirinweather

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LocationService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val apiKey = "acc199caf12c164dc5c1616b7bf123a9"

    override fun onBind(intent: Intent?): IBinder? = null

    @RequiresApi(Build.VERSION_CODES.O)
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForegroundService()  // this MUST call startForeground() with a real Notification
        requestLocation()         // fetch and notify
        return START_STICKY
    }


    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private fun requestLocation() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 60_000L).apply {
            setWaitForAccurateLocation(true)
            setMinUpdateIntervalMillis(30_000L)
            setMaxUpdateDelayMillis(120_000L)
        }.build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val location = result.lastLocation
                if (location != null) {
                    fetchWeatherAndNotify(location)
                }
                fusedLocationClient.removeLocationUpdates(this)
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            mainLooper
        )
    }

    private fun fetchWeatherAndNotify(location: Location) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val weather = RetrofitInstance.api.getWeatherByCoords(
                    lat = location.latitude,
                    lon = location.longitude,
                    apiKey = apiKey
                )

                val icon = when (weather.weather.firstOrNull()?.main) {
                    "Clear" -> R.drawable.sun
                    "Clouds" -> R.drawable.cloud
                    "Rain", "Drizzle" -> R.drawable.rain
                    "Thunderstorm" -> R.drawable.thunderstorm
                    "Snow" -> R.drawable.snow
                    "Mist", "Fog", "Haze", "Smoke" -> R.drawable.mist
                    else -> R.drawable.cloud
                }

                withContext(Dispatchers.Main) {
                    val notification = buildNotification(
                        title = weather.name,
                        content = "${weather.main.temp.toInt()}°F - ${weather.weather.firstOrNull()?.description ?: ""}",
                        iconRes = icon
                    )

                    val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    manager.notify(1001, notification)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startForegroundService() {
        val channelId = "weather_notification_channel"
        val channel = NotificationChannel(
            channelId,
            "Weather Updates",
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)

        val notification = buildNotification(
            title = "Weather Update",
            content = "Fetching your location...",
            iconRes = R.drawable.mist
        )

        startForeground(1, notification) // ✅ THIS LINE IS MANDATORY
    }



    private fun buildNotification(title: String, content: String, iconRes: Int): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, "weather_notification_channel")
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(iconRes)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .setAutoCancel(false)
            .build()
    }

}

