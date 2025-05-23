package com.highonswift.demotask

import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WeatherActivity : AppCompatActivity() {

    private val apiKey = "322bfcb74cedcbd6946222dbb428b794"
    private val city = "Chennai"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_weather)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Allow network on main thread (not recommended for production)
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())

        fetchWeatherData()
    }

    private fun fetchWeatherData() {
        try {
            val urlString =
                "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$apiKey&units=metric"
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection

            val stream = connection.inputStream.bufferedReader().use { it.readText() }

            // Log the full raw JSON response
            Log.d("WeatherResponse", stream)

            val json = JSONObject(stream)

            val temp = json.getJSONObject("main").getDouble("temp")
            val humidity = json.getJSONObject("main").getInt("humidity")
            val windSpeed = json.getJSONObject("wind").getDouble("speed")
            val description = json.getJSONArray("weather").getJSONObject(0).getString("description")
            val cityName = json.getString("name")

            runOnUiThread {
                findViewById<TextView>(R.id.tvCity).text = cityName
                findViewById<TextView>(R.id.tvTemperature).text = "$temp°C"
                findViewById<TextView>(R.id.tvWeatherDescription).text =
                    description.replaceFirstChar { it.uppercaseChar() }
                findViewById<TextView>(R.id.tvHumidity).text = "Humidity: $humidity%"
                findViewById<TextView>(R.id.tvWind).text = "Wind: $windSpeed km/h"

                val dateFormat = SimpleDateFormat("EEEE, hh:mm a", Locale.getDefault())
                findViewById<TextView>(R.id.tvDateTime).text = dateFormat.format(Date())
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to load weather data", Toast.LENGTH_SHORT).show()
        }
    }
}
