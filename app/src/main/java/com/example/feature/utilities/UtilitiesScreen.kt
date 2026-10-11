package com.example.feature.utilities

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.RestartAlt
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BelarusUtilityTariffs
import com.example.model.StringsHolder
import com.example.model.UtilityItem
import java.util.Locale

@Composable
fun UtilitiesScreen(
    strings: StringsHolder,
    modifier: Modifier = Modifier
) {
    // State maps for previous and current readings
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

    // Custom tariffs in case user edits
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
        // Grand Total Header Card
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
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Сума разлічана па субсідаваных тарыфах для насельніцтва Беларусі",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                    )
                }
            }
        }

        // Section Title
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Паслугі і лічыльнікі",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )

                OutlinedButton(
                    onClick = {
                        prevReadings.clear()
                        currReadings.clear()
                    },
                    modifier = Modifier.testTag("clear_utilities_button")
                ) {
                    Icon(imageVector = Icons.Default.RestartAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = strings.clear, fontSize = 12.sp)
                }
            }
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

        // ERIP (АРІП) Instructions Guide
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Receipt,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = strings.eripGuide,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("utility_item_${item.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Title and calculated cost
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = item.iconEmoji, fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(8.dp))
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

            // Inputs Row
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
