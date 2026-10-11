package com.example.feature.transport

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsSubway
import androidx.compose.material.icons.filled.LocalTaxi
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Train
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.StringsHolder

@Composable
fun TransportScreen(
    strings: StringsHolder,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) } // 0 = БЧ Цягнікі, 1 = Метро, 2 = Таксі / Гарадскі

    // Railway route selector states
    var fromCity by remember { mutableStateOf("Мінск") }
    var toCity by remember { mutableStateOf("Брэст") }

    val cities = listOf("Мінск", "Брэст", "Гродна", "Віцебск", "Гомель", "Магілёў", "Баранавічы", "Полацк")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Tabs
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
                        Icon(imageVector = Icons.Default.Train, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(strings.railwaysBch, fontWeight = FontWeight.SemiBold)
                    }
                },
                modifier = Modifier.testTag("tab_railways")
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.DirectionsSubway, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(strings.metroMinsk, fontWeight = FontWeight.SemiBold)
                    }
                },
                modifier = Modifier.testTag("tab_metro")
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.LocalTaxi, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(strings.taxiServices, fontWeight = FontWeight.SemiBold)
                    }
                },
                modifier = Modifier.testTag("tab_taxi")
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (selectedTab == 0) {
                // Belarusian Railway (БЧ)
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "🚆", fontSize = 24.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "Беларуская чыгунка (БЧ)",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Пасажырскія зносіны і расклад цягнікоў па Беларусі",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Route Selector
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = "Адкуль", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                                    CityChipsScroll(selected = fromCity, cities = cities, onSelect = { fromCity = it })
                                }

                                IconButton(
                                    onClick = {
                                        val temp = fromCity
                                        fromCity = toCity
                                        toCity = temp
                                    },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.SwapHoriz, contentDescription = "Swap")
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = "Куды", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                                    CityChipsScroll(selected = toCity, cities = cities, onSelect = { toCity = it })
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                            Divider(color = MaterialTheme.colorScheme.outlineVariant)
                            Spacer(modifier = Modifier.height(14.dp))

                            // Route estimated info
                            val travelTime = when {
                                (fromCity == "Мінск" && toCity == "Брэст") || (fromCity == "Брэст" && toCity == "Мінск") -> "3 г 15 хв (Штадлер ЭПР) / 3 г 45 хв"
                                (fromCity == "Мінск" && toCity == "Гродна") || (fromCity == "Гродна" && toCity == "Мінск") -> "4 г 00 хв (Міжрэгіянальныя лініі)"
                                (fromCity == "Мінск" && toCity == "Гомель") || (fromCity == "Гомель" && toCity == "Мінск") -> "2 г 50 хв (Штадлер бізнес-клас)"
                                (fromCity == "Мінск" && toCity == "Віцебск") || (fromCity == "Віцебск" && toCity == "Мінск") -> "3 г 05 хв (Хуткі міжрэгіянальны)"
                                (fromCity == "Мінск" && toCity == "Магілёў") || (fromCity == "Магілёў" && toCity == "Мінск") -> "2 г 45 хв"
                                fromCity == toCity -> "Адзін і той жа горад"
                                else -> "Ад 2 г 30 хв да 5 г у залежнасці ад саставу"
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f))
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "$fromCity ➔ $toCity",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Прыкладны час у дарозе: $travelTime",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Button(
                                onClick = {
                                    val url = "https://pass.rw.by"
                                    try {
                                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                                    } catch (_: Exception) {}
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(imageVector = Icons.Default.OpenInBrowser, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "Адкрыць білеты і расклад на pass.rw.by")
                            }
                        }
                    }
                }
            } else if (selectedTab == 1) {
                // Minsk Metro
                item {
                    MetroLineCard(
                        lineName = "1. Маскоўская лінія (Сіняя)",
                        lineColor = Color(0xFF1E88E5),
                        stations = listOf(
                            "Уручча", "Барысаўскі тракт", "Усход", "Маскоўская",
                            "Парк Чалюскінцаў", "Акадэмія навук", "Плошча Якуба Коласа",
                            "Плошча Перамогі", "Кастрычніцкая 🔁", "Плошча Леніна 🔁",
                            "Інстытут культуры", "Грушаўка", "Міхалова", "Пятроўшчына", "Малінаўка"
                        ),
                        lengthText = "19.1 км • 15 станцый"
                    )
                }

                item {
                    MetroLineCard(
                        lineName = "2. Аўтазаводская лінія (Чырвоная)",
                        lineColor = Color(0xFFE53935),
                        stations = listOf(
                            "Каменная Горка", "Кунцаўшчына", "Спартыўная", "Пушкінская",
                            "Маладзёжная", "Фрунзенская 🔁", "Няміга", "Купалаўская 🔁",
                            "Першамайская", "Пралетарская", "Трактарны завод", "Партызанская",
                            "Аўтазаводская", "Магілёўская"
                        ),
                        lengthText = "18.1 км • 14 станцый"
                    )
                }

                item {
                    MetroLineCard(
                        lineName = "3. Зеленалужская лінія (Зялёная)",
                        lineColor = Color(0xFF43A047),
                        stations = listOf(
                            "Юбілейная плошча 🔁", "Плошча Францішка Багушэвіча", "Вакзальная 🔁",
                            "Кавальская Слабада", "Аэрадромная", "Немаршанскі Сад", "Слуцкі Гасцінец"
                        ),
                        lengthText = "7 станцый • Аўтаматычныя цягнікі Штадлер"
                    )
                }
            } else {
                // Taxi & City Transport
                item {
                    TaxiDirectoryCard(
                        title = "Службы таксі і заказ паездак",
                        items = listOf(
                            TaxiItem("Яндэкс Go", "Праз дадатак або 7220", "Папулярны сэрвіс паездак і дастаўкі па ўсёй Беларусі"),
                            TaxiItem("Таксі 135 «Сталіца»", "Кароткі нумар 135", "Адна з найстарэйшых надзейных службаў таксі Мінска"),
                            TaxiItem("Таксі 7788 «Алмаз»", "Кароткі нумар 7788", "Кругласутачны заказ аўто"),
                            TaxiItem("Anytime / Мульцімотарс", "Каршэрынг у Мінску", "Пахвілінная арэнда аўтамабіляў")
                        )
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun CityChipsScroll(
    selected: String,
    cities: List<String>,
    onSelect: (String) -> Unit
) {
    val scrollState = rememberScrollState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        cities.forEach { city ->
            val isSelected = city == selected
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.clickable { onSelect(city) }
            ) {
                Text(
                    text = city,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun MetroLineCard(
    lineName: String,
    lineColor: Color,
    stations: List<String>,
    lengthText: String
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded }
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
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(lineColor)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = lineName,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = lengthText,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }

                Text(
                    text = if (isExpanded) "Згарнуць ▲" else "Станцыі ▼",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Divider(color = MaterialTheme.colorScheme.outlineVariant)
                    Spacer(modifier = Modifier.height(8.dp))
                    stations.forEachIndexed { i, station ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 3.dp)
                        ) {
                            Text(
                                text = "${i + 1}.",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.outline,
                                modifier = Modifier.width(24.dp)
                            )
                            Text(
                                text = station,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = if (station.contains("🔁")) FontWeight.Bold else FontWeight.Normal
                                ),
                                color = if (station.contains("🔁")) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}

private data class TaxiItem(val name: String, val contact: String, val desc: String)

@Composable
private fun TaxiDirectoryCard(
    title: String,
    items: List<TaxiItem>
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(10.dp))
            items.forEachIndexed { index, item ->
                Column(modifier = Modifier.padding(vertical = 6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = item.name,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = item.contact,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = item.desc,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
                if (index < items.size - 1) {
                    Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                }
            }
        }
    }
}
