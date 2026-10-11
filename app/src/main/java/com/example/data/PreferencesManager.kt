package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.AccentPalette
import com.example.model.AppLanguage
import com.example.model.AppModule
import com.example.model.ThemeMode

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
        // Ensure SETTINGS is always present at the end if omitted
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
}
