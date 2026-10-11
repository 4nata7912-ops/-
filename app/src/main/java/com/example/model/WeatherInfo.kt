package com.example.model

data class BelarusianCity(
    val id: String,
    val nameBe: String,
    val nameRu: String,
    val nameEn: String,
    val latitude: Double,
    val longitude: Double,
    val region: String
) {
    fun getName(lang: AppLanguage): String = when (lang) {
        AppLanguage.BE -> nameBe
        AppLanguage.UK -> nameBe
        AppLanguage.RU -> nameRu
        else -> nameEn
    }

    companion object {
        val ALL = listOf(
            BelarusianCity("minsk", "Мінск", "Минск", "Minsk", 53.9006, 27.5590, "Сталічны рэгіён"),
            BelarusianCity("brest", "Брэст", "Брест", "Brest", 52.0976, 23.7341, "Брэсцкая вобласць"),
            BelarusianCity("grodno", "Гродна", "Гродно", "Grodno", 53.6688, 23.8295, "Гродзенская вобласць"),
            BelarusianCity("vitebsk", "Віцебск", "Витебск", "Vitebsk", 55.1904, 30.2049, "Віцебская вобласць"),
            BelarusianCity("gomel", "Гомель", "Гомель", "Gomel", 52.4345, 30.9754, "Гомельская вобласць"),
            BelarusianCity("mogilev", "Магілёў", "Могилев", "Mogilev", 53.9007, 30.3313, "Магілёўская вобласць"),
            BelarusianCity("bobruisk", "Бабруйск", "Бобруйск", "Bobruisk", 53.1384, 29.2214, "Магілёўская вобласць"),
            BelarusianCity("baranovichi", "Баранавічы", "Барановичи", "Baranovichi", 53.1327, 26.0139, "Брэсцкая вобласць"),
            BelarusianCity("polotsk", "Полацк", "Полоцк", "Polotsk", 55.4856, 28.7686, "Віцебская вобласць"),
            BelarusianCity("pinsk", "Пінск", "Пинск", "Pinsk", 52.1153, 26.0957, "Брэсцкая вобласць")
        )
    }
}

data class WeatherData(
    val city: BelarusianCity,
    val tempC: Double,
    val conditionDescription: String,
    val conditionCode: String, // "sunny", "cloudy", "rainy", "thunder", "snowy", "fog"
    val windSpeedMs: Double,
    val windDirection: String,
    val humidityPercent: Int,
    val pressureMmHg: Int,
    val feelsLikeC: Double,
    val forecastDays: List<ForecastDay>
)

data class ForecastDay(
    val dayName: String,
    val tempMax: Double,
    val tempMin: Double,
    val conditionCode: String,
    val condition: String
)
