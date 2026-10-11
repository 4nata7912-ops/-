package com.example.model

data class UtilityItem(
    val id: String,
    val nameKey: String,
    val unit: String,
    val defaultTariff: Double, // in BYN per unit
    val iconName: String,
    val description: String
)

data class SavedUtilityCalculation(
    val id: String,
    val dateText: String,
    val accountNumber: String,
    val totalByn: Double,
    val details: String
)

object BelarusUtilityTariffs {
    // Standard established residential tariffs in Belarus in BYN
    val ELECTRICITY = UtilityItem(
        id = "elec",
        nameKey = "electricity",
        unit = "кВт·г",
        defaultTariff = 0.2708,
        iconName = "bolt",
        description = "Аднаставачны субсідаваны тарыф для жылых памяшканняў"
    )

    val COLD_WATER = UtilityItem(
        id = "cold_water",
        nameKey = "coldWater",
        unit = "м³",
        defaultTariff = 1.5553,
        iconName = "water_drop",
        description = "Водазабеспячэнне і каналізацыя (Мінскводаканал)"
    )

    val HOT_WATER = UtilityItem(
        id = "hot_water",
        nameKey = "hotWater",
        unit = "м³",
        defaultTariff = 2.4710,
        iconName = "hot_water",
        description = "Падагрэў вады (цеплавая энергія на патрэбы ГВП)"
    )

    val GAS = UtilityItem(
        id = "gas",
        nameKey = "gas",
        unit = "м³",
        defaultTariff = 0.2062,
        iconName = "gas",
        description = "Прыродны газ пры наяўнасці індывідуальных лічыльнікаў"
    )

    val HEATING = UtilityItem(
        id = "heating",
        nameKey = "heating",
        unit = "Гкал",
        defaultTariff = 24.7187,
        iconName = "heating",
        description = "Ацяпленне кватэры ў ацяпляльны перыяд"
    )

    val ALL_SERVICES = listOf(ELECTRICITY, COLD_WATER, HOT_WATER, GAS, HEATING)
}
