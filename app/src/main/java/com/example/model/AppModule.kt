package com.example.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.DirectionsTransit
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.ui.graphics.vector.ImageVector

enum class AppModule(
    val id: String,
    val icon: ImageVector,
    val isCore: Boolean = false
) {
    NEWS("news", Icons.Default.Newspaper),
    CURRENCY("currency", Icons.Default.CurrencyExchange),
    RUNA_AI("runa_ai", Icons.Default.AutoAwesome),
    UTILITIES("utilities", Icons.Default.Calculate),
    TRANSPORT("transport", Icons.Default.DirectionsTransit),
    EMERGENCY("emergency", Icons.Default.Emergency),
    WEATHER("weather", Icons.Default.WbSunny),
    HERITAGE("heritage", Icons.Default.Explore),
    CALENDAR("calendar", Icons.Default.CalendarMonth),
    SETTINGS("settings", Icons.Default.Settings, isCore = true);

    fun getTitle(strings: StringsHolder): String {
        return when (this) {
            NEWS -> strings.moduleNews
            CURRENCY -> strings.moduleCurrency
            RUNA_AI -> "Руна (ІІ)"
            UTILITIES -> strings.moduleUtilities
            TRANSPORT -> strings.moduleTransport
            EMERGENCY -> strings.moduleEmergency
            WEATHER -> strings.moduleWeather
            HERITAGE -> "Вандроўкі"
            CALENDAR -> strings.moduleCalendar
            SETTINGS -> strings.moduleSettings
        }
    }

    companion object {
        val DEFAULT_MODULES = listOf(NEWS, CURRENCY, RUNA_AI, UTILITIES, TRANSPORT, WEATHER, SETTINGS)
        val ALL_OPTIONAL_MODULES = listOf(NEWS, CURRENCY, RUNA_AI, UTILITIES, TRANSPORT, EMERGENCY, WEATHER, HERITAGE, CALENDAR)

        fun fromId(id: String): AppModule? {
            return entries.find { it.id == id }
        }
    }
}
