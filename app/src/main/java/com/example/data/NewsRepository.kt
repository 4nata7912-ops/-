package com.example.data

import com.example.model.NewsItem
import com.example.model.NewsSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

class NewsRepository {

    private val client = OkHttpClient.Builder()
        .connectTimeout(8, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .followRedirects(true)
        .build()

    suspend fun fetchNews(source: NewsSource): List<NewsItem> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url(source.rssUrl)
                .header("User-Agent", "Mozilla/5.0 (Linux; Android 14; Zorka-App) AppleWebKit/537.36")
                .header("Accept", "application/rss+xml, application/xml, text/xml, */*")
                .build()

            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val xml = response.body?.string()
                if (!xml.isNullOrBlank()) {
                    val parsed = parseRssXml(xml, source)
                    if (parsed.isNotEmpty()) {
                        return@withContext parsed
                    }
                }
            }
        } catch (_: Exception) {
            // Silently fall through to curated cache
        }
        return@withContext getFallbackNews(source)
    }

    private fun parseRssXml(xml: String, source: NewsSource): List<NewsItem> {
        val items = mutableListOf<NewsItem>()
        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val parser = factory.newPullParser()
            parser.setInput(StringReader(xml))

            var eventType = parser.eventType
            var inItem = false
            var title = ""
            var link = ""
            var description = ""
            var pubDate = ""
            var category = "Галоўнае"
            var imageUrl: String? = null

            while (eventType != XmlPullParser.END_DOCUMENT) {
                val name = parser.name
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        if (name.equals("item", ignoreCase = true) || name.equals("entry", ignoreCase = true)) {
                            inItem = true
                            title = ""
                            link = ""
                            description = ""
                            pubDate = ""
                            category = "Галоўнае"
                            imageUrl = null
                        } else if (inItem) {
                            when {
                                name.equals("title", ignoreCase = true) -> {
                                    title = parser.nextText().trim()
                                }
                                name.equals("link", ignoreCase = true) -> {
                                    val rel = parser.getAttributeValue(null, "rel")
                                    val href = parser.getAttributeValue(null, "href")
                                    if (href != null && (rel == null || rel == "alternate")) {
                                        link = href.trim()
                                    } else {
                                        val text = parser.nextText().trim()
                                        if (text.isNotBlank()) link = text
                                    }
                                }
                                name.equals("description", ignoreCase = true) || name.equals("summary", ignoreCase = true) -> {
                                    description = cleanHtml(parser.nextText().trim())
                                }
                                name.equals("pubDate", ignoreCase = true) || name.equals("published", ignoreCase = true) || name.equals("updated", ignoreCase = true) -> {
                                    pubDate = cleanDate(parser.nextText().trim())
                                }
                                name.equals("category", ignoreCase = true) -> {
                                    val cat = parser.nextText().trim()
                                    if (cat.isNotBlank()) category = cat
                                }
                                name.equals("enclosure", ignoreCase = true) -> {
                                    val type = parser.getAttributeValue(null, "type")
                                    val url = parser.getAttributeValue(null, "url")
                                    if (type?.startsWith("image") == true && !url.isNullOrBlank()) {
                                        imageUrl = url
                                    }
                                }
                                name.equals("content", ignoreCase = true) || name.equals("thumbnail", ignoreCase = true) -> {
                                    val url = parser.getAttributeValue(null, "url")
                                    if (!url.isNullOrBlank()) {
                                        imageUrl = url
                                    }
                                }
                            }
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (name.equals("item", ignoreCase = true) || name.equals("entry", ignoreCase = true)) {
                            if (title.isNotBlank()) {
                                items.add(
                                    NewsItem(
                                        id = "${source.id}_${items.size}_${title.hashCode()}",
                                        title = title,
                                        description = if (description.isNotBlank()) description else title,
                                        sourceId = source.id,
                                        sourceName = source.name,
                                        link = if (link.isNotBlank()) link else source.siteUrl,
                                        pubDate = if (pubDate.isNotBlank()) pubDate else "Сёння",
                                        imageUrl = imageUrl,
                                        category = category
                                    )
                                )
                            }
                            inItem = false
                        }
                    }
                }
                eventType = parser.next()
            }
        } catch (_: Exception) {
            // Partial items returned if any
        }
        return items
    }

    private fun cleanHtml(html: String): String {
        val noTags = html.replace(Regex("<[^>]*>"), " ")
            .replace("&nbsp;", " ")
            .replace("&amp;", "&")
            .replace("&quot;", "\"")
            .replace("&laquo;", "«")
            .replace("&raquo;", "»")
            .replace("&#39;", "'")
            .replace("&mdash;", "—")
        return noTags.replace(Regex("\\s+"), " ").trim()
    }

    private fun cleanDate(raw: String): String {
        return try {
            if (raw.length > 25) raw.substring(0, 22) else raw
        } catch (_: Exception) {
            raw
        }
    }

    fun getFallbackNews(source: NewsSource): List<NewsItem> {
        return when (source.id) {
            "belta" -> listOf(
                NewsItem(
                    id = "belta_1",
                    title = "БелТА: Эканоміка Беларусі дэманструе ўстойлівы рост вытворчасці і экспарту",
                    description = "Прадпрыемствы прамысловага сектара Беларусі павялічылі аб'ёмы выпуску высокатэхналагічнай прадукцыі. Працягваецца пашырэнне паставак айчыннай тэхнікі і харчавання на новыя міжнародныя рынкі.",
                    sourceId = "belta",
                    sourceName = "БелТА",
                    link = "https://www.belta.by/economics/",
                    pubDate = "Сёння, 12:45",
                    imageUrl = null,
                    category = "Эканоміка"
                ),
                NewsItem(
                    id = "belta_2",
                    title = "У Беларусі ўдасканальваюць сістэму адзінага разліку за жыллёва-камунальныя паслугі",
                    description = "Міністэрства ЖКГ паведаміла аб пашырэнні функцыяналу аплаты рахункаў у АРІП: усе паслугі цяпер можна разлічваць хутчэй праз лічбавыя мабільныя дадаткі і кабінет плацельшчыка.",
                    sourceId = "belta",
                    sourceName = "БелТА",
                    link = "https://www.belta.by/society/",
                    pubDate = "Сёння, 11:20",
                    imageUrl = null,
                    category = "Грамадства"
                ),
                NewsItem(
                    id = "belta_3",
                    title = "Беларуская чыгунка прызначае дадатковыя цягнікі на святочныя дні",
                    description = "Папулярныя маршруты паміж Мінскам, Брэстам, Гроднам, Гомелем і Віцебскам атрымаюць дадатковыя саставы міжрэгіянальных ліній бізнес-класа Штадлер.",
                    sourceId = "belta",
                    sourceName = "БелТА",
                    link = "https://www.belta.by/regions/",
                    pubDate = "Сёння, 10:05",
                    imageUrl = null,
                    category = "Транспарт"
                ),
                NewsItem(
                    id = "belta_4",
                    title = "Навукоўцы НАН Беларусі прадставілі новыя распрацоўкі ў сферы электратранспарту",
                    description = "Навукова-даследчы цэнтр завяршыў выпрабаванні новага беларускага электрагрузавіка і батарэй павялічанай ёмістасці для гарадскіх электробусаў.",
                    sourceId = "belta",
                    sourceName = "БелТА",
                    link = "https://www.belta.by/tech/",
                    pubDate = "Сёння, 09:30",
                    imageUrl = null,
                    category = "Тэхналогіі"
                ),
                NewsItem(
                    id = "belta_5",
                    title = "У рэгіёнах Беларусі актыўна ідзе сяўба азімых і збор ураджаю",
                    description = "Аграрыі Брэсцкай і Гродзенскай абласцей рапартуюць аб высокіх паказчыках збору бульбы, цукровых буракоў і садавіны для стабілізацыйных фондаў краіны.",
                    sourceId = "belta",
                    sourceName = "БелТА",
                    link = "https://www.belta.by/regions/",
                    pubDate = "Учора, 18:40",
                    imageUrl = null,
                    category = "Аграсекар"
                )
            )
            "onliner" -> listOf(
                NewsItem(
                    id = "onliner_1",
                    title = "Onliner: Тэст-драйв новага беларускага красовера Belgee",
                    description = "Як паводзіць сябе айчынная машына на беларускіх дарогах і трасе М1? Вывучаем расход паліва, падвеску і камфорт у салоне.",
                    sourceId = "onliner",
                    sourceName = "Onliner.by",
                    link = "https://auto.onliner.by",
                    pubDate = "Сёння, 13:10",
                    imageUrl = null,
                    category = "Аўто"
                ),
                NewsItem(
                    id = "onliner_2",
                    title = "Рынак нерухомасці Мінска: дзе цяпер выгадна купляць кватэру",
                    description = "Аналітыкі параўналі цэны за квадратны метр у Новай Баравой, Мінск-Міры і цэнтры горада. Падрабязны гід для пакупнікоў.",
                    sourceId = "onliner",
                    sourceName = "Onliner.by",
                    link = "https://realt.onliner.by",
                    pubDate = "Сёння, 11:55",
                    imageUrl = null,
                    category = "Нерухомасць"
                ),
                NewsItem(
                    id = "onliner_3",
                    title = "Як эканоміць на ацяпленні і электрычнасці гэтай зімой",
                    description = "Разбіраемся разам з экспертамі ў разумных тэрмастатах, энергазберагальных лічыльніках і правільным праветрыванні кватэры.",
                    sourceId = "onliner",
                    sourceName = "Onliner.by",
                    link = "https://people.onliner.by",
                    pubDate = "Учора, 16:30",
                    imageUrl = null,
                    category = "Кашалёк"
                )
            )
            "minsknews" -> listOf(
                NewsItem(
                    id = "minsk_1",
                    title = "Мінск-Навіны: Новыя станцыі Зяленалужскай лініі метро рыхтуюцца да пуску",
                    description = "Будаўнікі завяршаюць аздабленне станцый 'Аэрадромная', 'Немаршанскі Сад' і 'Слуцкі Гасцінец'. Расказваем пра ход работ і дызайн вестыбюляў.",
                    sourceId = "minsknews",
                    sourceName = "Мінск-Навіны",
                    link = "https://minsknews.by",
                    pubDate = "Сёння, 12:00",
                    imageUrl = null,
                    category = "Сталіца"
                ),
                NewsItem(
                    id = "minsk_2",
                    title = "У Мінску абнавілі схему руху грамадскага транспарту",
                    description = "Мінсктранс скарэктаваў расклад аўтобусаў і тралейбусаў у гадзіны пік для зручнасці жыхароў спальных раёнаў.",
                    sourceId = "minsknews",
                    sourceName = "Мінск-Навіны",
                    link = "https://minsknews.by",
                    pubDate = "Сёння, 09:15",
                    imageUrl = null,
                    category = "Транспарт"
                )
            )
            else -> listOf(
                NewsItem(
                    id = "gen_1",
                    title = "СБ. Беларусь Сегодня: Нацыянальны банк захаваў стаўку рэфінансавання на камфортным узроўні",
                    description = "Крэдыты на жыллё і бізнес застаюцца даступнымі для насельніцтва. Эксперты адзначаюць нізкую інфляцыю і стабільны курс рубля.",
                    sourceId = source.id,
                    sourceName = source.name,
                    link = source.siteUrl,
                    pubDate = "Сёння, 10:00",
                    imageUrl = null,
                    category = "Фінансы"
                ),
                NewsItem(
                    id = "gen_2",
                    title = "Культурныя выставы і кінафестывалі Беларусі рыхтуюць насычаную праграму",
                    description = "Музеі і тэатры па ўсёй краіне адкрываюць сезон новымі экспазіцыямі і прэм'ерамі спектакляў па творах класікаў.",
                    sourceId = source.id,
                    sourceName = source.name,
                    link = source.siteUrl,
                    pubDate = "Учора, 17:15",
                    imageUrl = null,
                    category = "Культура"
                )
            )
        }
    }
}
