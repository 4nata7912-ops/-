package com.example.model

data class CurrencyRate(
    val code: String,          // USD, EUR, RUB, CNY, PLN
    val name: String,
    val symbol: String,        // $, €, ₽, ¥, zł, Br
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
    val brandColorHex: Long,
    val spreads: Map<String, BankSpread>
)

data class BankSpread(
    val buyDeltaPercent: Double,
    val sellDeltaPercent: Double
)

data class BankRatesSet(
    val bankId: String,
    val bankName: String,
    val rates: List<CurrencyRate>,
    val updatedAt: String
)
