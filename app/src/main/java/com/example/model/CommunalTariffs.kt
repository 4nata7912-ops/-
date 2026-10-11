package com.example.model

data class UtilityItem(
    val id: String,
    val nameKey: String,
    val unit: String,
    val defaultTariff: Double, // in BYN per unit
    val iconEmoji: String,
    val description: String
)

object BelarusUtilityTariffs {
    // Standard established tariffs in Belarus (2025/2026 typical subsidised residential rates in BYN)
    val ELECTRICITY = UtilityItem(
        id = "elec",
        nameKey = "electricity",
        unit = "кВт·г",
        defaultTariff = 0.2708,
        iconEmoji = "⚡",
        description = "Аднаставачны субсідаваны тарыф для кватэр з электраплітамі"
    )

    val COLD_WATER = UtilityItem(
        id = "cold_water",
        nameKey = "coldWater",
        unit = "м³",
        defaultTariff = 1.5553,
        iconEmoji = "💧",
        description = "Водазабеспячэнне і водаадвядзенне (каналізацыя)"
    )

    val HOT_WATER = UtilityItem(
        id = "hot_water",
        nameKey = "hotWater",
        unit = "м³",
        defaultTariff = 2.4710,
        iconEmoji = "♨️",
        description = "Падагрэў вады (цеплавая энергія)"
    )

    val GAS = UtilityItem(
        id = "gas",
        nameKey = "gas",
        unit = "м³",
        defaultTariff = 0.2062,
        iconEmoji = "🔥",
        description = "Прыродны газ пры наяўнасці індывідуальных газавых лічыльнікаў"
    )

    val HEATING = UtilityItem(
        id = "heating",
        nameKey = "heating",
        unit = "Гкал",
        defaultTariff = 24.7187,
        iconEmoji = "🏠",
        description = "Цеплазабеспячэнне кватэры ў ацяпляльны перыяд"
    )

    val ALL_SERVICES = listOf(ELECTRICITY, COLD_WATER, HOT_WATER, GAS, HEATING)
}
