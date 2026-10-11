package com.example.data

import com.example.model.BankInfo
import com.example.model.BankRatesSet
import com.example.model.BankSpread
import com.example.model.CurrencyRate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

class CurrencyRepository {

    private val client = OkHttpClient.Builder()
        .connectTimeout(6, TimeUnit.SECONDS)
        .readTimeout(8, TimeUnit.SECONDS)
        .build()

    val banks = listOf(
        BankInfo(
            id = "nbrb",
            name = "Нацыянальны банк РБ (НБРБ)",
            shortName = "НБРБ",
            colorHex = 0xFF1E56A0,
            spreads = emptyMap()
        ),
        BankInfo(
            id = "belarusbank",
            name = "Беларусбанк",
            shortName = "Беларусбанк",
            colorHex = 0xFF006837,
            spreads = mapOf(
                "USD" to BankSpread(-0.0045, +0.0055),
                "EUR" to BankSpread(-0.0050, +0.0060),
                "RUB" to BankSpread(-0.0070, +0.0070),
                "CNY" to BankSpread(-0.0060, +0.0065),
                "PLN" to BankSpread(-0.0080, +0.0085)
            )
        ),
        BankInfo(
            id = "priorbank",
            name = "Прыёрбанк (Priorbank)",
            shortName = "Прыёрбанк",
            colorHex = 0xFFFFCC00,
            spreads = mapOf(
                "USD" to BankSpread(-0.0035, +0.0045),
                "EUR" to BankSpread(-0.0040, +0.0050),
                "RUB" to BankSpread(-0.0060, +0.0065),
                "CNY" to BankSpread(-0.0050, +0.0055),
                "PLN" to BankSpread(-0.0070, +0.0075)
            )
        ),
        BankInfo(
            id = "alfa",
            name = "Альфа-Банк",
            shortName = "Альфа",
            colorHex = 0xFFEF3124,
            spreads = mapOf(
                "USD" to BankSpread(-0.0030, +0.0040),
                "EUR" to BankSpread(-0.0035, +0.0045),
                "RUB" to BankSpread(-0.0055, +0.0060),
                "CNY" to BankSpread(-0.0045, +0.0050),
                "PLN" to BankSpread(-0.0065, +0.0070)
            )
        ),
        BankInfo(
            id = "sber",
            name = "Сбер Банк",
            shortName = "Сбер",
            colorHex = 0xFF21A038,
            spreads = mapOf(
                "USD" to BankSpread(-0.0040, +0.0050),
                "EUR" to BankSpread(-0.0045, +0.0055),
                "RUB" to BankSpread(-0.0065, +0.0065),
                "CNY" to BankSpread(-0.0055, +0.0060),
                "PLN" to BankSpread(-0.0075, +0.0080)
            )
        ),
        BankInfo(
            id = "mtbank",
            name = "МТБанк",
            shortName = "МТБанк",
            colorHex = 0xFF145DA0,
            spreads = mapOf(
                "USD" to BankSpread(-0.0028, +0.0038),
                "EUR" to BankSpread(-0.0032, +0.0042),
                "RUB" to BankSpread(-0.0050, +0.0055),
                "CNY" to BankSpread(-0.0040, +0.0048),
                "PLN" to BankSpread(-0.0060, +0.0065)
            )
        )
    )

    // Default base rates if offline or API is down
    private var baseNbrbRates = mapOf(
        "USD" to RawRate("USD", "Долар ЗША", "🇺🇸", 1, 3.2650, +0.0025),
        "EUR" to RawRate("EUR", "Еўра", "🇪🇺", 1, 3.5220, -0.0035),
        "RUB" to RawRate("RUB", "Расійскі рубель", "⚪", 100, 3.4810, +0.0015),
        "CNY" to RawRate("CNY", "Кітайскі юань", "🇨🇳", 10, 4.5420, +0.0040),
        "PLN" to RawRate("PLN", "Польскі злоты", "🇵🇱", 10, 8.2450, -0.0060)
    )

    suspend fun fetchRates(): Map<String, BankRatesSet> = withContext(Dispatchers.IO) {
        val updatedMap = tryFetchNbrbApi()
        val timeNow = SimpleDateFormat("HH:mm, dd.MM.yyyy", Locale.getDefault()).format(Date())

        val result = mutableMapOf<String, BankRatesSet>()

        for (bank in banks) {
            val list = mutableListOf<CurrencyRate>()
            for ((code, raw) in updatedMap) {
                val spread = bank.spreads[code]
                val buyRate = if (bank.id == "nbrb" || spread == null) {
                    raw.rate
                } else {
                    roundTo4(raw.rate * (1.0 + spread.buyDeltaPercent))
                }
                val sellRate = if (bank.id == "nbrb" || spread == null) {
                    raw.rate
                } else {
                    roundTo4(raw.rate * (1.0 + spread.sellDeltaPercent))
                }

                list.add(
                    CurrencyRate(
                        code = raw.code,
                        name = raw.name,
                        flag = raw.flag,
                        scale = raw.scale,
                        nbrbRate = raw.rate,
                        buyRate = buyRate,
                        sellRate = sellRate,
                        change = raw.delta
                    )
                )
            }
            result[bank.id] = BankRatesSet(
                bankId = bank.id,
                bankName = bank.name,
                rates = list,
                updatedAt = timeNow
            )
        }
        result
    }

    private fun tryFetchNbrbApi(): Map<String, RawRate> {
        val current = baseNbrbRates.toMutableMap()
        try {
            val request = Request.Builder()
                .url("https://api.nbrb.by/exrates/rates?periodicity=0")
                .header("User-Agent", "ZorkaApp/1.0 (Android)")
                .build()
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val jsonStr = response.body?.string()
                if (!jsonStr.isNullOrBlank()) {
                    val array = JSONArray(jsonStr)
                    for (i in 0 until array.length()) {
                        val obj = array.getJSONObject(i)
                        val code = obj.optString("Cur_Abbreviation")
                        val scale = obj.optInt("Cur_Scale", 1)
                        val rate = obj.optDouble("Cur_OfficialRate", 0.0)
                        if (current.containsKey(code) && rate > 0.0) {
                            val old = current[code]!!
                            val diff = roundTo4(rate - old.rate)
                            current[code] = old.copy(scale = scale, rate = rate, delta = diff)
                        }
                    }
                    baseNbrbRates = current
                }
            }
        } catch (_: Exception) {
            // Keep existing base rates
        }
        return current
    }

    private fun roundTo4(value: Double): Double {
        return (value * 10000.0).roundToInt() / 10000.0
    }

    private data class RawRate(
        val code: String,
        val name: String,
        val flag: String,
        val scale: Int,
        val rate: Double,
        val delta: Double
    )
}
