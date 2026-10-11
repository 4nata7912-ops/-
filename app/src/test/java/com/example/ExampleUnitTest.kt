package com.example

import com.example.data.CurrencyRepository
import com.example.data.NewsRepository
import com.example.model.AppLanguage
import com.example.model.AppModule
import com.example.model.AppTranslations
import com.example.model.BelarusUtilityTariffs
import com.example.model.NewsSource
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testAllLanguagesSupported() {
        val languages = AppLanguage.entries
        assertEquals(6, languages.size)

        languages.forEach { lang ->
            val strings = AppTranslations.get(lang)
            assertNotNull(strings)
            assertTrue(strings.appTitle.isNotBlank())
            assertTrue(strings.moduleNews.isNotBlank())
            assertTrue(strings.moduleCurrency.isNotBlank())
            assertTrue(strings.converterTitle.isNotBlank())
        }
    }

    @Test
    fun testNewsSourcesIncludeBelta() {
        val sources = NewsSource.ALL
        val belta = sources.find { it.id == "belta" }
        assertNotNull(belta)
        assertEquals("БелТА", belta?.name)

        val newsRepo = NewsRepository()
        val beltaNews = newsRepo.getFallbackNews(belta!!)
        assertTrue(beltaNews.isNotEmpty())
        assertTrue(beltaNews.first().title.contains("БелТА") || beltaNews.first().sourceName == "БелТА")
    }

    @Test
    fun testCurrencyRatesAndBanks() = runBlocking {
        val currencyRepo = CurrencyRepository()
        assertEquals(6, currencyRepo.banks.size) // NBRB, Belarusbank, Priorbank, Alfa, Sber, MTBank

        val rates = currencyRepo.fetchRates()
        assertTrue(rates.containsKey("nbrb"))
        assertTrue(rates.containsKey("belarusbank"))

        val nbrbRates = rates["nbrb"]!!
        val usd = nbrbRates.rates.find { it.code == "USD" }
        assertNotNull(usd)
        assertTrue(usd!!.nbrbRate > 0.0)
    }

    @Test
    fun testUtilityTariffs() {
        val tariffs = BelarusUtilityTariffs.ALL_SERVICES
        assertTrue(tariffs.isNotEmpty())
        assertTrue(tariffs.any { it.id == "elec" && it.defaultTariff > 0 })
        assertTrue(tariffs.any { it.id == "cold_water" && it.defaultTariff > 0 })
    }

    @Test
    fun testModularSystemDefaultConfig() {
        val defaults = AppModule.DEFAULT_MODULES
        assertTrue(defaults.contains(AppModule.NEWS))
        assertTrue(defaults.contains(AppModule.CURRENCY))
        assertTrue(defaults.contains(AppModule.SETTINGS))
    }
}
