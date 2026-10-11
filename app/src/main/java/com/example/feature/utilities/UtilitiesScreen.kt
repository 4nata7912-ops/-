package com.example.feature.utilities

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Propane
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PreferencesManager
import com.example.model.BelarusUtilityTariffs
import com.example.model.SavedUtilityCalculation
import com.example.model.StringsHolder
import com.example.model.UtilityItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun UtilitiesScreen(
    prefsManager: PreferencesManager,
    strings: StringsHolder,
    modifier: Modifier = Modifier
) {
    var accountNumber by remember { mutableStateOf(prefsManager.getSavedAccountNumber()) }
    var savedHistory by remember { mutableStateOf(prefsManager.getSavedUtilityBills()) }
    var showSavedHistory by remember { mutableStateOf(false) }

    val prevReadings = remember {
        mutableStateMapOf<String, String>().apply {
            put("elec", "1420")
            put("cold_water", "210")
            put("hot_water", "105")
            put("gas", "45")
            put("heating", "1.2")
        }
    }

    val currReadings = remember {
        mutableStateMapOf<String, String>().apply {
            put("elec", "1560")
            put("cold_water", "218")
            put("hot_water", "109")
            put("gas", "52")
            put("heating", "1.8")
        }
    }

    val customTariffs = remember {
        mutableStateMapOf<String, Double>().apply {
            BelarusUtilityTariffs.ALL_SERVICES.forEach { put(it.id, it.defaultTariff) }
        }
    }

    // Calculate total
    var totalByn = 0.0
    BelarusUtilityTariffs.ALL_SERVICES.forEach { item ->
        val prev = prevReadings[item.id]?.toDoubleOrNull() ?: 0.0
        val curr = currReadings[item.id]?.toDoubleOrNull() ?: 0.0
        val tariff = customTariffs[item.id] ?: item.defaultTariff
        val diff = if (curr >= prev) curr - prev else 0.0
        totalByn += diff * tariff
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Grand Total Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("utilities_total_card")
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = strings.totalToPay,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${String.format(Locale.US, "%.2f", totalByn)} BYN",
                        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Субсідаваныя тарыфы Рэспублікі Беларусь",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                    )
                }
            }
        }

        // Account number input & Actions row
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    OutlinedTextField(
                        value = accountNumber,
                        onValueChange = {
                            accountNumber = it
                            prefsManager.setSavedAccountNumber(it)
                        },
                        label = { Text(strings.accountNumber) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                val now = SimpleDateFormat("dd.MM.yyyy, HH:mm", Locale.getDefault()).format(Date())
                                val newBill = SavedUtilityCalculation(
                                    id = "bill_${System.currentTimeMillis()}",
                                    dateText = now,
                                    accountNumber = accountNumber,
                                    totalByn = totalByn,
                                    details = "Святло, вада, газ, ацяпленне"
                                )
                                prefsManager.saveUtilityBill(newBill)
                                savedHistory = prefsManager.getSavedUtilityBills()
                                showSavedHistory = true
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(imageVector = Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = strings.saveCalculation, fontSize = 12.sp)
                        }

                        OutlinedButton(
                            onClick = {
                                prevReadings.clear()
                                currReadings.clear()
                            },
                            modifier = Modifier.testTag("clear_utilities_button")
                        ) {
                            Icon(imageVector = Icons.Default.RestartAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }

        // Saved History toggle
        if (savedHistory.isNotEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showSavedHistory = !showSavedHistory }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.History, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${strings.savedHistory} (${savedHistory.size})",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = if (showSavedHistory) "Згарнуць ▲" else "Паказаць ▼",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            if (showSavedHistory) {
                items(savedHistory.take(5), key = { it.id }) { bill ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = bill.dateText, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                                if (bill.accountNumber.isNotBlank()) {
                                    Text(text = "Л/р: ${bill.accountNumber}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                                }
                            }
                            Text(
                                text = "${String.format(Locale.US, "%.2f", bill.totalByn)} BYN",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )
                        }
                    }
                }
            }
        }

        // Section Title
        item {
            Text(
                text = "Лічыльнікі і паслугі",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Each Utility service card
        items(BelarusUtilityTariffs.ALL_SERVICES) { item ->
            val prevStr = prevReadings[item.id] ?: ""
            val currStr = currReadings[item.id] ?: ""
            val prev = prevStr.toDoubleOrNull() ?: 0.0
            val curr = currStr.toDoubleOrNull() ?: 0.0
            val tariff = customTariffs[item.id] ?: item.defaultTariff
            val diff = if (curr >= prev) curr - prev else 0.0
            val cost = diff * tariff

            UtilityCard(
                item = item,
                prevReading = prevStr,
                currReading = currStr,
                consumption = diff,
                cost = cost,
                tariff = tariff,
                onPrevChanged = { prevReadings[item.id] = it },
                onCurrChanged = { currReadings[item.id] = it },
                strings = strings
            )
        }

        // ERIP Guide
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Receipt,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = strings.eripGuide,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = strings.eripInstructions,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun UtilityCard(
    item: UtilityItem,
    prevReading: String,
    currReading: String,
    consumption: Double,
    cost: Double,
    tariff: Double,
    onPrevChanged: (String) -> Unit,
    onCurrChanged: (String) -> Unit,
    strings: StringsHolder
) {
    val iconVector: ImageVector = when (item.iconName) {
        "bolt" -> Icons.Default.Bolt
        "water_drop" -> Icons.Default.WaterDrop
        "hot_water" -> Icons.Default.LocalFireDepartment
        "gas" -> Icons.Default.Propane
        else -> Icons.Default.Thermostat
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("utility_item_${item.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = iconVector,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = when (item.id) {
                                "elec" -> strings.electricity
                                "cold_water" -> strings.coldWater
                                "hot_water" -> strings.hotWater
                                "gas" -> strings.gas
                                else -> strings.heating
                            },
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${strings.tariff}: ${String.format(Locale.US, "%.4f", tariff)} BYN / ${item.unit}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${String.format(Locale.US, "%.2f", cost)} BYN",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                    Text(
                        text = "${String.format(Locale.US, "%.1f", consumption)} ${item.unit}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Inputs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = prevReading,
                    onValueChange = onPrevChanged,
                    label = { Text(strings.previousReading, fontSize = 11.sp) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = currReading,
                    onValueChange = onCurrChanged,
                    label = { Text(strings.currentReading, fontSize = 11.sp) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        }
    }
}
