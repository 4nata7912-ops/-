package com.example.feature.heritage

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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

data class HeritageSpot(
    val id: String,
    val title: String,
    val location: String,
    val distanceKm: Int,
    val isUnesco: Boolean,
    val category: String, // "Замкі", "Прырода", "Гісторыя", "Святыні"
    val description: String,
    val highlights: String,
    val mapQuery: String
)

object BelarusHeritageData {
    val SPOTS = listOf(
        HeritageSpot(
            id = "mir",
            title = "Мірскі замак",
            location = "г.п. Мір, Карэліцкі раён, Гродзенская вобласць",
            distanceKm = 95,
            isUnesco = true,
            category = "Замкі",
            description = "Унікальны помнік беларускага абарончага дойлідства XVI стагоддзя. Спалучэнне готыкі, рэнесансу і барока з 5 велічнымі вежамі і сажалкай.",
            highlights = "Музейныя залы, склеп князёў Святаполк-Мірскіх, рэканструкцыі рыцарскіх турніраў.",
            mapQuery = "Мирский замок, Мир"
        ),
        HeritageSpot(
            id = "nesvizh",
            title = "Нясвіжскі палацава-паркавы комплекс",
            location = "г. Нясвіж, Мінская вобласць",
            distanceKm = 120,
            isUnesco = true,
            category = "Замкі",
            description = "Былая рэзідэнцыя магутнага роду князёў Радзівілаў. Велічны палац, акружаны валамі, равамі і пяццю маляўнічымі паркамі.",
            highlights = "Залатая і Зорная залы, легенда пра прывід Барбары Радзівіл (Чорная Панна), Фарны касцёл.",
            mapQuery = "Несвижский замок, Несвиж"
        ),
        HeritageSpot(
            id = "pushcha",
            title = "Нацыянальны парк «Белавежская пушча»",
            location = "Камянецкі раён, Брэсцкая вобласць",
            distanceKm = 340,
            isUnesco = true,
            category = "Прырода",
            description = "Найстарэйшы рэліктавы першабытны лес Еўропы. Царства векавечных дубаў і дом найбуйнейшай у свеце папуляцыі зуброў.",
            highlights = "Вальеры з жывёламі, Цар-дуб (больш за 600 гадоў), Памесце беларускага Дзеда Мароза.",
            mapQuery = "Беловежская пуща, Каменюки"
        ),
        HeritageSpot(
            id = "brest_fortress",
            title = "Мемарыяльны комплекс «Брэсцкая крэпасць-герой»",
            location = "г. Брэст",
            distanceKm = 350,
            isUnesco = false,
            category = "Гісторыя",
            description = "Выдатны манументальны ансамбль, прысвечаны гераічнай абароне крэпасці ў чэрвені 1941 года на беразе ракі Мухавец.",
            highlights = "Манумент «Мужнасць», 100-метровы штык-абеліск, Холмская брама, музей абароны.",
            mapQuery = "Брестская крепость, Брест"
        ),
        HeritageSpot(
            id = "braslav",
            title = "Браслаўскія азёры",
            location = "Браслаўскі раён, Віцебская вобласць",
            distanceKm = 240,
            isUnesco = false,
            category = "Прырода",
            description = "Краіна блакітных азёр на поўначы Беларусі: больш за 300 вадаёмаў з ледавіковымі рэльефамі, выспамі і чыстай вадой.",
            highlights = "Гара Маяк (цудоўны панарамны від), экалагічныя сцежкі, водныя паходы на байдарках.",
            mapQuery = "Браславские озера, Браслав"
        ),
        HeritageSpot(
            id = "grodno_old_castle",
            title = "Стары замак і Каложа ў Гродне",
            location = "г. Гродна",
            distanceKm = 290,
            isUnesco = false,
            category = "Замкі",
            description = "Каралеўская рэзідэнцыя Стэфана Баторыя на высокім беразе Нёмана і найстарэйшая царква Беларусі — Барысаглебская (Каложская) XII стагоддзя.",
            highlights = "Адноўлены рэнесансны палац Баторыя, старажытная плінфа з галасніковымі гаршкамі.",
            mapQuery = "Старый замок, Гродно"
        ),
        HeritageSpot(
            id = "polotsk_sophia",
            title = "Сафійскі сабор у Полацку",
            location = "г. Полацк, Віцебская вобласць",
            distanceKm = 230,
            isUnesco = false,
            category = "Святыні",
            description = "Трэці Сафійскі сабор у славянскім свеце пасля Кіева і Ноўгарада, узведзены князем Усяславам Чарадзеем у XI стагоддзі над Дзвіной.",
            highlights = "Фантастычная арганная зала, музей архітэктуры, Барысаў камень каля сабора.",
            mapQuery = "Софийский собор, Полоцк"
        )
    )
}

@Composable
fun HeritageScreen(
    strings: StringsHolder,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf("Усе") }
    val categories = listOf("Усе", "Замкі", "Прырода", "Гісторыя", "Святыні")

    val filteredSpots = remember(selectedCategory) {
        if (selectedCategory == "Усе") BelarusHeritageData.SPOTS
        else BelarusHeritageData.SPOTS.filter { it.category == selectedCategory }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Category Filter row
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Text(
                    text = "Славутасці і падарожжы па Беларусі",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Замкі, нацыянальныя паркі і гістарычныя жамчужыны",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )

                val scrollState = rememberScrollState()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(scrollState)
                        .padding(top = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categories.forEach { cat ->
                        val isSelected = cat == selectedCategory
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat, fontSize = 12.sp) }
                        )
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredSpots, key = { it.id }) { spot ->
                HeritageSpotCard(
                    spot = spot,
                    onOpenMap = {
                        try {
                            val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=${Uri.encode(spot.mapQuery)}"))
                            context.startActivity(mapIntent)
                        } catch (_: Exception) {}
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun HeritageSpotCard(
    spot: HeritageSpot,
    onOpenMap: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = spot.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                if (spot.isUnesco) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF1976D2).copy(alpha = 0.15f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "ЮНЕСКА",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1976D2)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Place, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = spot.location,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "• ~${spot.distanceKm} км ад Мінска",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = spot.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    .padding(10.dp)
            ) {
                Column {
                    Text(
                        text = "Што паглядзець:",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = spot.highlights,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onOpenMap,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(imageVector = Icons.Default.Map, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Пракласці маршрут на карце", fontSize = 13.sp)
            }
        }
    }
}
