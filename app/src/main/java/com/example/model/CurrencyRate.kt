package com.example.model

data class CurrencyRate(
    val code: String,          // USD, EUR, RUB, CNY, PLN
    val name: String,
    val flag: String,
    val scale: Int,            // 1, 10, 100
    val nbrbRate: Double,      // Official NBRB rate in BYN for scale units
    val buyRate: Double,       // Bank buys from client
    val sellRate: Double,      // Bank sells to client
    val change: Double = 0.0   // 24h delta in BYN
)

data class BankInfo(
    val id: String,
    val name: String,
    val shortName: String,
    val colorHex: Long,
    val spreads: Map<String, BankSpread> // code -> spread info
)

data class BankSpread(
    val buyDeltaPercent: Double,  // e.g. -0.005 (-0.5%)
    val sellDeltaPercent: Double  // e.g. +0.005 (+0.5%)
)

data class BankRatesSet(
    val bankId: String,
    val bankName: String,
    val rates: List<CurrencyRate>,
    val updatedAt: String
)
