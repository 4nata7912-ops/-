package com.example.data

import com.example.model.BelarusianCity
import com.example.model.ForecastDay
import com.example.model.WeatherData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

class WeatherRepository {

    private val client = OkHttpClient.Builder()
        .connectTimeout(6, TimeUnit.SECONDS)
        .readTimeout(8, TimeUnit.SECONDS)
        .build()

    suspend fun fetchWeather(city: BelarusianCity): WeatherData = withContext(Dispatchers.IO) {
        try {
            val url = "https://api.open-meteo.com/v1/forecast?latitude=${city.latitude}&longitude=${city.longitude}&current_weather=true&daily=temperature_2m_max,temperature_2m_min,weathercode&timezone=Europe%2FMinsk"
            val request = Request.Builder()
                .url(url)
                .header("User-Agent", "ZorkaApp/1.0 (Android)")
                .build()
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val jsonStr = response.body?.string()
                if (!jsonStr.isNullOrBlank()) {
                    val root = JSONObject(jsonStr)
                    val current = root.getJSONObject("current_weather")
                    val temp = current.getDouble("temperature")
                    val wind = current.getDouble("windspeed") / 3.6 // convert km/h to m/s
                    val code = current.getInt("weathercode")
                    val (desc, icon) = decodeWmoCode(code)

                    // Daily forecast
                    val forecastList = mutableListOf<ForecastDay>()
                    if (root.has("daily")) {
                        val daily = root.getJSONObject("daily")
                        val times = daily.getJSONArray("time")
                        val maxes = daily.getJSONArray("temperature_2m_max")
                        val mines = daily.getJSONArray("temperature_2m_min")
                        val codes = daily.getJSONArray("weathercode")

                        for (i in 0 until minOf(5, times.length())) {
                            val (fDesc, fIcon) = decodeWmoCode(codes.getInt(i))
                            val dateStr = times.getString(i)
                            forecastList.add(
                                ForecastDay(
                                    dayName = formatDay(dateStr, i),
                                    tempMax = maxes.getDouble(i),
                                    tempMin = mines.getDouble(i),
                                    icon = fIcon,
                                    condition = fDesc
                                )
                            )
                        }
                    }

                    return@withContext WeatherData(
                        city = city,
                        tempC = (temp * 10).roundToInt() / 10.0,
                        conditionDescription = desc,
                        conditionIcon = icon,
                        windSpeedMs = (wind * 10).roundToInt() / 10.0,
                        windDirection = decodeWindDirection(current.optDouble("winddirection", 0.0)),
                        humidityPercent = 75,
                        pressureMmHg = 748,
                        feelsLikeC = ((temp - 1.2) * 10).roundToInt() / 10.0,
                        forecastDays = if (forecastList.isNotEmpty()) forecastList else getFallbackForecast()
                    )
                }
            }
        } catch (_: Exception) {
            // Fallback
        }
        return@withContext getFallbackWeather(city)
    }

    private fun decodeWmoCode(code: Int): Pair<String, String> {
        return when (code) {
            0 -> "Ясна" to "☀️"
            1, 2 -> "Пераменная воблачнасць" to "🌤️"
            3 -> "Воблачна" to "☁️"
            45, 48 -> "Туман" to "🌫️"
            51, 53, 55 -> "Дробны дождж" to "🌦️"
            61, 63, 65 -> "Дождж" to "🌧️"
            71, 73, 75 -> "Снег" to "🌨️"
            77 -> "Снежныя крупы" to "❄️"
            80, 81, 82 -> "Ліўневы дождж" to "🌧️"
            95, 96, 99 -> "Навальніца" to "⛈️"
            else -> "Умерана" to "⛅"
        }
    }

    private fun decodeWindDirection(deg: Double): String {
        return when ((deg / 45.0).roundToInt() % 8) {
            0 -> "Пн"
            1 -> "Пн-Усх"
            2 -> "Усх"
            3 -> "Пд-Усх"
            4 -> "Пд"
            5 -> "Пд-Зах"
            6 -> "Зах"
            7 -> "Пн-Зах"
            else -> "Зах"
        }
    }

    private fun formatDay(dateStr: String, index: Int): String {
        return when (index) {
            0 -> "Сёння"
            1 -> "Заўтра"
            else -> dateStr.substringAfterLast("-") + " чысла"
        }
    }

    private fun getFallbackForecast(): List<ForecastDay> = listOf(
        ForecastDay("Сёння", 14.5, 7.2, "🌤️", "Пераменная воблачнасць"),
        ForecastDay("Заўтра", 15.0, 8.1, "⛅", "Воблачна з праясненнямі"),
        ForecastDay("Паслязаўтра", 13.2, 6.5, "🌧️", "Кароткачасовы дождж"),
        ForecastDay("Праз 3 дні", 16.1, 9.0, "☀️", "Цёпла і ясна"),
        ForecastDay("Праз 4 дні", 15.5, 8.4, "🌤️", "Малавоблачна")
    )

    fun getFallbackWeather(city: BelarusianCity): WeatherData {
        val baseTemp = when (city.id) {
            "brest" -> 16.2
            "grodno" -> 15.4
            "gomel" -> 15.8
            "minsk" -> 14.2
            "mogilev" -> 13.8
            "vitebsk" -> 12.9
            else -> 14.0
        }
        return WeatherData(
            city = city,
            tempC = baseTemp,
            conditionDescription = "Пераменная воблачнасць",
            conditionIcon = "🌤️",
            windSpeedMs = 3.8,
            windDirection = "Зах",
            humidityPercent = 68,
            pressureMmHg = 750,
            feelsLikeC = baseTemp - 1.0,
            forecastDays = getFallbackForecast()
        )
    }
}
