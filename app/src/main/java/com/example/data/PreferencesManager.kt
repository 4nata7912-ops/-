package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.AccentPalette
import com.example.model.AppLanguage
import com.example.model.AppModule
import com.example.model.SavedUtilityCalculation
import com.example.model.ThemeMode
import org.json.JSONArray
import org.json.JSONObject

class PreferencesManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("zorka_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_MODULES = "key_enabled_modules"
        private const val KEY_LANGUAGE = "key_language"
        private const val KEY_THEME_MODE = "key_theme_mode"
        private const val KEY_ACCENT = "key_accent_palette"
        private const val KEY_NEWS_SOURCE = "key_news_source"
        private const val KEY_CURRENCY_BANK = "key_currency_bank"
        private const val KEY_FIRST_LAUNCH = "key_first_launch"
        private const val KEY_BOOKMARKS = "key_bookmarked_news_ids"
        private const val KEY_SAVED_BILLS = "key_saved_utility_bills"
        private const val KEY_ACCOUNT_NUMBER = "key_user_erip_account"
    }

    var isFirstLaunch: Boolean
        get() = prefs.getBoolean(KEY_FIRST_LAUNCH, true)
        set(value) = prefs.edit().putBoolean(KEY_FIRST_LAUNCH, value).apply()

    fun getEnabledModules(): List<AppModule> {
        val raw = prefs.getString(KEY_MODULES, null)
        if (raw.isNullOrBlank()) {
            return AppModule.DEFAULT_MODULES
        }
        val list = raw.split(",").mapNotNull { AppModule.fromId(it.trim()) }
        return if (!list.contains(AppModule.SETTINGS)) {
            list + AppModule.SETTINGS
        } else {
            list
        }
    }

    fun setEnabledModules(modules: List<AppModule>) {
        val filtered = if (!modules.contains(AppModule.SETTINGS)) {
            modules + AppModule.SETTINGS
        } else {
            modules
        }
        val raw = filtered.joinToString(",") { it.id }
        prefs.edit().putString(KEY_MODULES, raw).apply()
    }

    fun getLanguage(): AppLanguage {
        val code = prefs.getString(KEY_LANGUAGE, AppLanguage.BE.code) ?: AppLanguage.BE.code
        return AppLanguage.fromCode(code)
    }

    fun setLanguage(language: AppLanguage) {
        prefs.edit().putString(KEY_LANGUAGE, language.code).apply()
    }

    fun getThemeMode(): ThemeMode {
        val raw = prefs.getString(KEY_THEME_MODE, ThemeMode.SYSTEM.name) ?: ThemeMode.SYSTEM.name
        return try {
            ThemeMode.valueOf(raw)
        } catch (_: Exception) {
            ThemeMode.SYSTEM
        }
    }

    fun setThemeMode(mode: ThemeMode) {
        prefs.edit().putString(KEY_THEME_MODE, mode.name).apply()
    }

    fun getAccentPalette(): AccentPalette {
        val raw = prefs.getString(KEY_ACCENT, AccentPalette.CORNFLOWER.id) ?: AccentPalette.CORNFLOWER.id
        return AccentPalette.fromId(raw)
    }

    fun setAccentPalette(palette: AccentPalette) {
        prefs.edit().putString(KEY_ACCENT, palette.id).apply()
    }

    fun getSelectedNewsSource(): String {
        return prefs.getString(KEY_NEWS_SOURCE, "belta") ?: "belta"
    }

    fun setSelectedNewsSource(sourceId: String) {
        prefs.edit().putString(KEY_NEWS_SOURCE, sourceId).apply()
    }

    fun getSelectedBank(): String {
        return prefs.getString(KEY_CURRENCY_BANK, "nbrb") ?: "nbrb"
    }

    fun setSelectedBank(bankId: String) {
        prefs.edit().putString(KEY_CURRENCY_BANK, bankId).apply()
    }

    fun getBookmarkedNewsIds(): Set<String> {
        return prefs.getStringSet(KEY_BOOKMARKS, emptySet()) ?: emptySet()
    }

    fun toggleBookmark(newsId: String): Boolean {
        val current = getBookmarkedNewsIds().toMutableSet()
        val isAdded: Boolean
        if (current.contains(newsId)) {
            current.remove(newsId)
            isAdded = false
        } else {
            current.add(newsId)
            isAdded = true
        }
        prefs.edit().putStringSet(KEY_BOOKMARKS, current).apply()
        return isAdded
    }

    fun getSavedAccountNumber(): String {
        return prefs.getString(KEY_ACCOUNT_NUMBER, "142-884-019") ?: "142-884-019"
    }

    fun setSavedAccountNumber(number: String) {
        prefs.edit().putString(KEY_ACCOUNT_NUMBER, number).apply()
    }

    fun getSavedUtilityBills(): List<SavedUtilityCalculation> {
        val raw = prefs.getString(KEY_SAVED_BILLS, null) ?: return emptyList()
        val list = mutableListOf<SavedUtilityCalculation>()
        try {
            val array = JSONArray(raw)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    SavedUtilityCalculation(
                        id = obj.getString("id"),
                        dateText = obj.getString("date"),
                        accountNumber = obj.optString("acc", ""),
                        totalByn = obj.getDouble("total"),
                        details = obj.optString("details", "")
                    )
                )
            }
        } catch (_: Exception) {}
        return list
    }

    fun saveUtilityBill(bill: SavedUtilityCalculation) {
        val existing = getSavedUtilityBills().toMutableList()
        existing.add(0, bill)
        if (existing.size > 20) {
            existing.removeLast()
        }
        val array = JSONArray()
        for (item in existing) {
            val obj = JSONObject()
            obj.put("id", item.id)
            obj.put("date", item.dateText)
            obj.put("acc", item.accountNumber)
            obj.put("total", item.totalByn)
            obj.put("details", item.details)
            array.put(obj)
        }
        prefs.edit().putString(KEY_SAVED_BILLS, array.toString()).apply()
    }
}
