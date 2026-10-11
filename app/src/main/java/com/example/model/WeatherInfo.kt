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
            BelarusianCity("minsk", "Мінск", "Минск", "Minsk", 53.9006, 27.5590, "Сталіца"),
            BelarusianCity("brest", "Брэст", "Брест", "Brest", 52.0976, 23.7341, "Брэсцкая вобл."),
            BelarusianCity("grodno", "Гродна", "Гродно", "Grodno", 53.6688, 23.8295, "Гродзенская вобл."),
            BelarusianCity("vitebsk", "Віцебск", "Витебск", "Vitebsk", 55.1904, 30.2049, "Віцебская вобл."),
            BelarusianCity("gomel", "Гомель", "Гомель", "Gomel", 52.4345, 30.9754, "Гомельская вобл."),
            BelarusianCity("mogilev", "Магілёў", "Могилев", "Mogilev", 53.9007, 30.3313, "Магілёўская вобл."),
            BelarusianCity("bobruisk", "Бабруйск", "Бобруйск", "Bobruisk", 53.1384, 29.2214, "Магілёўская вобл."),
            BelarusianCity("baranovichi", "Баранавічы", "Барановичи", "Baranovichi", 53.1327, 26.0139, "Брэсцкая вобл."),
            BelarusianCity("polotsk", "Полацк", "Полоцк", "Polotsk", 55.4856, 28.7686, "Віцебская вобл."),
            BelarusianCity("pinsk", "Пінск", "Пинск", "Pinsk", 52.1153, 26.0957, "Брэсцкая вобл.")
        )
    }
}

data class WeatherData(
    val city: BelarusianCity,
    val tempC: Double,
    val conditionDescription: String,
    val conditionIcon: String, // emoji / icon
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
    val icon: String,
    val condition: String
)
