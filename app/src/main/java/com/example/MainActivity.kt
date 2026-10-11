package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.data.CurrencyRepository
import com.example.data.NewsRepository
import com.example.data.PreferencesManager
import com.example.data.WeatherRepository
import com.example.feature.calendar.CalendarScreen
import com.example.feature.currency.CurrencyScreen
import com.example.feature.emergency.EmergencyScreen
import com.example.feature.heritage.HeritageScreen
import com.example.feature.news.NewsScreen
import com.example.feature.runa.RunaChatScreen
import com.example.feature.runa.RunaEngine
import com.example.feature.settings.SettingsScreen
import com.example.feature.transport.TransportScreen
import com.example.feature.utilities.UtilitiesScreen
import com.example.feature.weather.WeatherScreen
import com.example.model.AccentPalette
import com.example.model.AppLanguage
import com.example.model.AppModule
import com.example.model.AppTranslations
import com.example.model.NewsSource
import com.example.model.ThemeMode
import com.example.ui.components.ModuleSelectionSheet
import com.example.ui.components.TopModuleBar
import com.example.ui.theme.ZorkaTheme

class MainActivity : ComponentActivity() {

    private val newsRepository = NewsRepository()
    private val currencyRepository = CurrencyRepository()
    private val weatherRepository = WeatherRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val prefsManager = PreferencesManager(applicationContext)

        setContent {
            ZorkaApp(
                prefsManager = prefsManager,
                newsRepository = newsRepository,
                currencyRepository = currencyRepository,
                weatherRepository = weatherRepository
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZorkaApp(
    prefsManager: PreferencesManager,
    newsRepository: NewsRepository,
    currencyRepository: CurrencyRepository,
    weatherRepository: WeatherRepository
) {
    var currentLanguage by remember { mutableStateOf(prefsManager.getLanguage()) }
    var currentThemeMode by remember { mutableStateOf(prefsManager.getThemeMode()) }
    var currentAccent by remember { mutableStateOf(prefsManager.getAccentPalette()) }
    var enabledModules by remember { mutableStateOf(prefsManager.getEnabledModules()) }
    var selectedNewsSourceId by remember { mutableStateOf(prefsManager.getSelectedNewsSource()) }
    var selectedBankId by remember { mutableStateOf(prefsManager.getSelectedBank()) }

    val runaEngine = remember {
        RunaEngine(newsRepository, currencyRepository)
    }

    var activeModule by remember {
        mutableStateOf(enabledModules.firstOrNull() ?: AppModule.NEWS)
    }

    var showModuleSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(Unit) {
        if (prefsManager.isFirstLaunch) {
            showModuleSheet = true
            prefsManager.isFirstLaunch = false
        }
    }

    LaunchedEffect(enabledModules) {
        if (!enabledModules.contains(activeModule)) {
            activeModule = enabledModules.firstOrNull() ?: AppModule.NEWS
        }
    }

    val strings = remember(currentLanguage) {
        AppTranslations.get(currentLanguage)
    }

    val firstModule = enabledModules.firstOrNull() ?: AppModule.NEWS
    BackHandler(enabled = activeModule != firstModule) {
        activeModule = firstModule
    }

    val currentNewsSource = remember(selectedNewsSourceId) {
        NewsSource.findById(selectedNewsSourceId)
    }

    ZorkaTheme(
        themeMode = currentThemeMode,
        accentPalette = currentAccent
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            topBar = {
                TopModuleBar(
                    activeModule = activeModule,
                    enabledModules = enabledModules,
                    strings = strings,
                    currentLanguage = currentLanguage,
                    onSelectModule = { activeModule = it },
                    onOpenModuleCustomizer = { showModuleSheet = true }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (activeModule) {
                    AppModule.NEWS -> {
                        NewsScreen(
                            repository = newsRepository,
                            prefsManager = prefsManager,
                            selectedSourceId = selectedNewsSourceId,
                            onSourceChanged = {
                                selectedNewsSourceId = it
                                prefsManager.setSelectedNewsSource(it)
                            },
                            strings = strings
                        )
                    }

                    AppModule.CURRENCY -> {
                        CurrencyScreen(
                            repository = currencyRepository,
                            selectedBankId = selectedBankId,
                            onBankChanged = {
                                selectedBankId = it
                                prefsManager.setSelectedBank(it)
                            },
                            strings = strings
                        )
                    }

                    AppModule.RUNA_AI -> {
                        RunaChatScreen(
                            engine = runaEngine,
                            currentLanguage = currentLanguage,
                            currentNewsSource = currentNewsSource,
                            strings = strings
                        )
                    }

                    AppModule.UTILITIES -> {
                        UtilitiesScreen(
                            prefsManager = prefsManager,
                            strings = strings
                        )
                    }

                    AppModule.TRANSPORT -> {
                        TransportScreen(
                            strings = strings
                        )
                    }

                    AppModule.EMERGENCY -> {
                        EmergencyScreen(
                            strings = strings
                        )
                    }

                    AppModule.WEATHER -> {
                        WeatherScreen(
                            repository = weatherRepository,
                            currentLanguage = currentLanguage,
                            strings = strings
                        )
                    }

                    AppModule.HERITAGE -> {
                        HeritageScreen(
                            strings = strings
                        )
                    }

                    AppModule.CALENDAR -> {
                        CalendarScreen(
                            strings = strings
                        )
                    }

                    AppModule.SETTINGS -> {
                        SettingsScreen(
                            currentLanguage = currentLanguage,
                            onLanguageChanged = {
                                currentLanguage = it
                                prefsManager.setLanguage(it)
                            },
                            currentThemeMode = currentThemeMode,
                            onThemeModeChanged = {
                                currentThemeMode = it
                                prefsManager.setThemeMode(it)
                            },
                            currentAccent = currentAccent,
                            onAccentChanged = {
                                currentAccent = it
                                prefsManager.setAccentPalette(it)
                            },
                            enabledModules = enabledModules,
                            onOpenModuleCustomizer = { showModuleSheet = true },
                            strings = strings
                        )
                    }
                }
            }

            if (showModuleSheet) {
                ModuleSelectionSheet(
                    sheetState = sheetState,
                    currentModules = enabledModules,
                    strings = strings,
                    onSaveModules = { newModules ->
                        enabledModules = newModules
                        prefsManager.setEnabledModules(newModules)
                        showModuleSheet = false
                    },
                    onDismiss = { showModuleSheet = false }
                )
            }
        }
    }
}
