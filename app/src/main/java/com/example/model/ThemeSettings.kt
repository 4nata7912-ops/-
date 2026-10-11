package com.example.model

import androidx.compose.ui.graphics.Color

enum class ThemeMode(val titleKey: String) {
    SYSTEM("system"),
    LIGHT("light"),
    DARK("dark")
}

enum class AccentPalette(
    val id: String,
    val lightPrimary: Color,
    val lightSecondary: Color,
    val lightTertiary: Color,
    val darkPrimary: Color,
    val darkSecondary: Color,
    val darkTertiary: Color
) {
    CORNFLOWER(
        id = "cornflower",
        lightPrimary = Color(0xFF1E56A0),
        lightSecondary = Color(0xFF3B79C9),
        lightTertiary = Color(0xFFE29528),
        darkPrimary = Color(0xFF90CAF9),
        darkSecondary = Color(0xFF64B5F6),
        darkTertiary = Color(0xFFFFB74D)
    ),
    EMERALD(
        id = "emerald",
        lightPrimary = Color(0xFF1B5E20),
        lightSecondary = Color(0xFF388E3C),
        lightTertiary = Color(0xFFE65100),
        darkPrimary = Color(0xFF81C784),
        darkSecondary = Color(0xFFA5D6A7),
        darkTertiary = Color(0xFFFFCC80)
    ),
    CRIMSON(
        id = "crimson",
        lightPrimary = Color(0xFFB71C1C),
        lightSecondary = Color(0xFFC62828),
        lightTertiary = Color(0xFF283593),
        darkPrimary = Color(0xFFEF9A9A),
        darkSecondary = Color(0xFFE57373),
        darkTertiary = Color(0xFF9FA8DA)
    ),
    AMBER(
        id = "amber",
        lightPrimary = Color(0xFFD68100),
        lightSecondary = Color(0xFFFFA000),
        lightTertiary = Color(0xFF1565C0),
        darkPrimary = Color(0xFFFFD54F),
        darkSecondary = Color(0xFFFFCA28),
        darkTertiary = Color(0xFF90CAF9)
    );

    companion object {
        fun fromId(id: String): AccentPalette =
            entries.find { it.id == id } ?: CORNFLOWER
    }
}
