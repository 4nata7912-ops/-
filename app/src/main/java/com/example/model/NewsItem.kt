package com.example.model

data class NewsSource(
    val id: String,
    val name: String,
    val siteUrl: String,
    val rssUrl: String,
    val description: String,
    val badge: String
) {
    companion object {
        val ALL = listOf(
            NewsSource(
                id = "belta",
                name = "БелТА",
                siteUrl = "https://www.belta.by",
                rssUrl = "https://www.belta.by/rss/all/",
                description = "Беларускае тэлеграфнае агенцтва (Афіцыйныя навіны краіны)",
                badge = "Афіцыйна"
            ),
            NewsSource(
                id = "onliner",
                name = "Onliner.by",
                siteUrl = "https://www.onliner.by",
                rssUrl = "https://people.onliner.by/feed",
                description = "Самы папулярны лічбавы партал Беларусі: грамадства, аўто, тэхналогіі",
                badge = "Партал"
            ),
            NewsSource(
                id = "minsknews",
                name = "Мінск-Навіны",
                siteUrl = "https://minsknews.by",
                rssUrl = "https://minsknews.by/feed/",
                description = "Аператыўныя навіны сталіцы і Мінскага рэгіёна",
                badge = "Мінск"
            ),
            NewsSource(
                id = "sb",
                name = "СБ. Беларусь Сегодня",
                siteUrl = "https://www.sb.by",
                rssUrl = "https://www.sb.by/rss/",
                description = "Выдавецкі дом «Беларусь сегодня» — аналітыка і рэпартажы",
                badge = "Прэса"
            ),
            NewsSource(
                id = "tochka",
                name = "Точка.BY",
                siteUrl = "https://tochka.by",
                rssUrl = "https://tochka.by/rss/",
                description = "Сучаснае выданне пра жыццё, бізнес, адпачынак і грошы ў Беларусі",
                badge = "Медыя"
            )
        )

        fun findById(id: String): NewsSource =
            ALL.find { it.id == id } ?: ALL.first()
    }
}

data class NewsItem(
    val id: String,
    val title: String,
    val description: String,
    val sourceId: String,
    val sourceName: String,
    val link: String,
    val pubDate: String,
    val imageUrl: String? = null,
    val category: String = "Галоўнае"
)
