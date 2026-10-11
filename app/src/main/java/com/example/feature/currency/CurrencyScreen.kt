package com.example.feature.currency

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SouthEast
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.CurrencyRepository
import com.example.model.BankRatesSet
import com.example.model.CurrencyRate
import com.example.model.StringsHolder
import kotlinx.coroutines.launch

@Composable
fun CurrencyScreen(
    repository: CurrencyRepository,
    selectedBankId: String,
    onBankChanged: (String) -> Unit,
    strings: StringsHolder,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    var ratesData by remember { mutableStateOf<Map<String, BankRatesSet>>(emptyMap()) }
    var isLoading by remember { mutableStateOf(false) }
    var currentBankId by remember(selectedBankId) { mutableStateOf(selectedBankId) }

    // Converter states
    var converterAmount by remember { mutableStateOf("100") }
    var fromCurrency by remember { mutableStateOf("USD") }
    var toCurrency by remember { mutableStateOf("BYN") }
    var isClientSelling by remember { mutableStateOf(true) } // true = sell foreign to bank, false = buy
    var selectedTab by remember { mutableIntStateOf(0) }

    fun loadRates() {
        isLoading = true
        coroutineScope.launch {
            ratesData = repository.fetchRates()
            isLoading = false
        }
    }

    LaunchedEffect(Unit) {
        loadRates()
    }

    val currentBankRates = ratesData[currentBankId] ?: ratesData["nbrb"]

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Bank Selection Bar
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(vertical = 6.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = strings.selectBank,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        currentBankRates?.let {
                            Text(
                                text = "${strings.updatedJustNow}: ${it.updatedAt}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }

                    IconButton(
                        onClick = { loadRates() },
                        modifier = Modifier
                            .testTag("refresh_rates_button")
                            .size(34.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                        } else {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = strings.refresh,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                val bankScroll = rememberScrollState()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(bankScroll)
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repository.banks.forEach { bank ->
                        val isSelected = bank.id == currentBankId
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                currentBankId = bank.id
                                onBankChanged(bank.id)
                            },
                            label = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(Color(bank.brandColorHex))
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = bank.shortName,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            },
                            modifier = Modifier.testTag("bank_chip_${bank.id}")
                        )
                    }
                }
            }
        }

        // Subtabs: [Канвертар] vs [Курсы банка]
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Calculate, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(strings.converterTitle, fontWeight = FontWeight.SemiBold)
                    }
                },
                modifier = Modifier.testTag("tab_converter")
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.CurrencyExchange, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(strings.currencyTitle, fontWeight = FontWeight.SemiBold)
                    }
                },
                modifier = Modifier.testTag("tab_rates_table")
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (selectedTab == 0) {
                // Converter Card
                item {
                    ConverterCard(
                        amount = converterAmount,
                        onAmountChanged = { converterAmount = it },
                        fromCurrency = fromCurrency,
                        onFromCurrencyChanged = { fromCurrency = it },
                        toCurrency = toCurrency,
                        onToCurrencyChanged = { toCurrency = it },
                        isClientSelling = isClientSelling,
                        onToggleOperation = { isClientSelling = !isClientSelling },
                        currentBankRates = currentBankRates,
                        strings = strings,
                        onSwap = {
                            val temp = fromCurrency
                            fromCurrency = toCurrency
                            toCurrency = temp
                            isClientSelling = !isClientSelling
                        }
                    )
                }

                // Bank comparison list
                item {
                    Text(
                        text = "Параўнанне ставак у банках Беларусі",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                item {
                    BankComparisonList(
                        amount = converterAmount.toDoubleOrNull() ?: 100.0,
                        fromCurrency = fromCurrency,
                        toCurrency = toCurrency,
                        isClientSelling = isClientSelling,
                        allBanksRates = ratesData,
                        banks = repository.banks,
                        currentBankId = currentBankId,
                        strings = strings,
                        onSelectBank = {
                            currentBankId = it
                            onBankChanged(it)
                        }
                    )
                }
            } else {
                // Hero Banner for Finance
                item {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(modifier = Modifier.fillMaxWidth().height(120.dp)) {
                            Image(
                                painter = painterResource(id = R.drawable.img_finance_banner),
                                contentDescription = "Finance Analytics",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        androidx.compose.ui.graphics.Brush.verticalGradient(
                                            colors = listOf(
                                                androidx.compose.ui.graphics.Color.Transparent,
                                                androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.7f)
                                            )
                                        )
                                    )
                            )
                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(14.dp)
                            ) {
                                Text(
                                    text = currentBankRates?.bankName ?: strings.bankNbrb,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = androidx.compose.ui.graphics.Color.White
                                    )
                                )
                                Text(
                                    text = if (currentBankId == "nbrb") "Афіцыйныя фіксаваныя курсы Нацбанка" else "Бягучыя курсы куплі і продажу",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.85f)
                                    )
                                )
                            }
                        }
                    }
                }

                // Table of Rates
                currentBankRates?.rates?.forEach { rate ->
                    item(key = rate.code) {
                        CurrencyRateRow(rate = rate, isNbrb = currentBankId == "nbrb", strings = strings)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun ConverterCard(
    amount: String,
    onAmountChanged: (String) -> Unit,
    fromCurrency: String,
    onFromCurrencyChanged: (String) -> Unit,
    toCurrency: String,
    onToCurrencyChanged: (String) -> Unit,
    isClientSelling: Boolean,
    onToggleOperation: () -> Unit,
    currentBankRates: BankRatesSet?,
    strings: StringsHolder,
    onSwap: () -> Unit
) {
    val doubleAmount = amount.toDoubleOrNull() ?: 0.0

    val convertedAmount = remember(doubleAmount, fromCurrency, toCurrency, isClientSelling, currentBankRates) {
        calculateConversion(
            amount = doubleAmount,
            from = fromCurrency,
            to = toCurrency,
            isClientSelling = isClientSelling,
            bankRates = currentBankRates
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("converter_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Direction toggle pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = strings.converterTitle,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.clickable { onToggleOperation() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isClientSelling) Icons.Default.SouthEast else Icons.Default.NorthEast,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isClientSelling) "Здаць валюту" else "Купіць валюту",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Amount Input
            OutlinedTextField(
                value = amount,
                onValueChange = { if (it.all { c -> c.isDigit() || c == '.' }) onAmountChanged(it) },
                label = { Text(strings.enterAmount) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("converter_amount_input"),
                shape = RoundedCornerShape(12.dp)
            )

            // Quick presets
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("50", "100", "500", "1000", "2000").forEach { preset ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                        modifier = Modifier.clickable { onAmountChanged(preset) }
                    ) {
                        Text(
                            text = preset,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Currency Selectors
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = strings.fromCurrency, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                    CurrencySelectorRow(
                        selected = fromCurrency,
                        onSelect = onFromCurrencyChanged,
                        currencies = listOf("USD", "EUR", "RUB", "BYN", "CNY", "PLN")
                    )
                }

                IconButton(
                    onClick = onSwap,
                    modifier = Modifier
                        .testTag("swap_currencies_button")
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Icon(
                        imageVector = Icons.Default.SwapVert,
                        contentDescription = strings.swap,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                    Text(text = strings.toCurrency, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                    CurrencySelectorRow(
                        selected = toCurrency,
                        onSelect = onToCurrencyChanged,
                        currencies = listOf("BYN", "USD", "EUR", "RUB", "CNY", "PLN")
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            Divider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(14.dp))

            // Result Display
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = strings.conversionResult,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${formatDouble(convertedAmount)} $toCurrency",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$amount $fromCurrency = ${formatDouble(convertedAmount)} $toCurrency",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

@Composable
private fun CurrencySelectorRow(
    selected: String,
    onSelect: (String) -> Unit,
    currencies: List<String>
) {
    val scrollState = rememberScrollState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        currencies.forEach { code ->
            val isSelected = code == selected
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.clickable { onSelect(code) }
            ) {
                Text(
                    text = code,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun CurrencyRateRow(
    rate: CurrencyRate,
    isNbrb: Boolean,
    strings: StringsHolder
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("rate_card_${rate.code}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Currency badge & title
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = rate.symbol,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "${rate.scale} ${rate.code}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = rate.name,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }

            // Rates values
            if (isNbrb) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${rate.nbrbRate} BYN",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val isPositive = rate.change >= 0
                        Icon(
                            imageVector = if (isPositive) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                            contentDescription = null,
                            tint = if (isPositive) Color(0xFF2E7D32) else Color(0xFFC62828),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "${if (isPositive) "+" else ""}${rate.change}",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isPositive) Color(0xFF2E7D32) else Color(0xFFC62828)
                        )
                    }
                }
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = strings.buyRate, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                        Text(
                            text = "${rate.buyRate}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = strings.sellRate, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                        Text(
                            text = "${rate.sellRate}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BankComparisonList(
    amount: Double,
    fromCurrency: String,
    toCurrency: String,
    isClientSelling: Boolean,
    allBanksRates: Map<String, BankRatesSet>,
    banks: List<com.example.model.BankInfo>,
    currentBankId: String,
    strings: StringsHolder,
    onSelectBank: (String) -> Unit
) {
    val results = banks.map { bank ->
        val rates = allBanksRates[bank.id]
        val result = calculateConversion(
            amount = amount,
            from = fromCurrency,
            to = toCurrency,
            isClientSelling = isClientSelling,
            bankRates = rates
        )
        BankComparisonItem(bank = bank, convertedAmount = result)
    }.sortedByDescending { it.convertedAmount }

    val bestBank = results.firstOrNull()

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            results.forEachIndexed { index, item ->
                val isSelected = item.bank.id == currentBankId
                val isBest = item.bank.id == bestBank?.bank?.id

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f) else Color.Transparent)
                        .clickable { onSelectBank(item.bank.id) }
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color(item.bank.brandColorHex))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = item.bank.shortName,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                if (isBest) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(Color(0xFF2E7D32))
                                            .padding(horizontal = 5.dp, vertical = 1.dp)
                                    ) {
                                        Text(
                                            text = strings.bestBankRate,
                                            fontSize = 9.sp,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Text(
                        text = "${formatDouble(item.convertedAmount)} $toCurrency",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = if (isBest) FontWeight.Bold else FontWeight.Medium,
                            color = if (isBest) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    )
                }

                if (index < results.size - 1) {
                    Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                }
            }
        }
    }
}

private data class BankComparisonItem(
    val bank: com.example.model.BankInfo,
    val convertedAmount: Double
)

private fun calculateConversion(
    amount: Double,
    from: String,
    to: String,
    isClientSelling: Boolean,
    bankRates: BankRatesSet?
): Double {
    if (amount <= 0 || bankRates == null) return 0.0
    if (from == to) return amount

    val amountInByn = if (from == "BYN") {
        amount
    } else {
        val rateObj = bankRates.rates.find { it.code == from } ?: return 0.0
        val rateToUse = if (isClientSelling) rateObj.buyRate else rateObj.sellRate
        amount * (rateToUse / rateObj.scale)
    }

    return if (to == "BYN") {
        amountInByn
    } else {
        val rateObj = bankRates.rates.find { it.code == to } ?: return 0.0
        val rateToUse = if (isClientSelling) rateObj.sellRate else rateObj.buyRate
        amountInByn / (rateToUse / rateObj.scale)
    }
}

private fun formatDouble(value: Double): String {
    return String.format(java.util.Locale.US, "%.2f", value)
}
