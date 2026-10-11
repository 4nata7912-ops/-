package com.example.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.DirectionsTransit
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.ui.graphics.vector.ImageVector

enum class AppModule(
    val id: String,
    val icon: ImageVector,
    val isCore: Boolean = false // settings can always be accessible
) {
    NEWS("news", Icons.Default.Newspaper),
    CURRENCY("currency", Icons.Default.CurrencyExchange),
    UTILITIES("utilities", Icons.Default.Calculate),
    TRANSPORT("transport", Icons.Default.DirectionsTransit),
    EMERGENCY("emergency", Icons.Default.Emergency),
    WEATHER("weather", Icons.Default.WbSunny),
    CALENDAR("calendar", Icons.Default.CalendarMonth),
    SETTINGS("settings", Icons.Default.Settings, isCore = true);

    fun getTitle(strings: StringsHolder): String {
        return when (this) {
            NEWS -> strings.moduleNews
            CURRENCY -> strings.moduleCurrency
            UTILITIES -> strings.moduleUtilities
            TRANSPORT -> strings.moduleTransport
            EMERGENCY -> strings.moduleEmergency
            WEATHER -> strings.moduleWeather
            CALENDAR -> strings.moduleCalendar
            SETTINGS -> strings.moduleSettings
        }
    }

    companion object {
        val DEFAULT_MODULES = listOf(NEWS, CURRENCY, UTILITIES, TRANSPORT, WEATHER, SETTINGS)
        val ALL_OPTIONAL_MODULES = listOf(NEWS, CURRENCY, UTILITIES, TRANSPORT, EMERGENCY, WEATHER, CALENDAR)

        fun fromId(id: String): AppModule? {
            return entries.find { it.id == id }
        }
    }
}
