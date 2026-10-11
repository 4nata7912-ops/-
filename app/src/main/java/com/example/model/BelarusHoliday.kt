package com.example.model

data class BelarusHoliday(
    val dateText: String,
    val title: String,
    val type: String,
    val isDayOff: Boolean,
    val description: String,
    val iconType: String
)

object BelarusCalendarData {
    val HOLIDAYS = listOf(
        BelarusHoliday(
            dateText = "1–2 студзеня",
            title = "Новы год",
            type = "Дзяржаўнае",
            isDayOff = true,
            description = "Афіцыйныя непрацоўныя святочныя дні ў Беларусі.",
            iconType = "calendar"
        ),
        BelarusHoliday(
            dateText = "7 студзеня",
            title = "Раство Хрыстова (праваслаўнае)",
            type = "Рэлігійнае",
            isDayOff = true,
            description = "Свята Нараджэння Хрыстова па юліянскім календары.",
            iconType = "star"
        ),
        BelarusHoliday(
            dateText = "8 сакавіка",
            title = "Дзень жанчын",
            type = "Дзяржаўнае",
            isDayOff = true,
            description = "Міжнароднае жаночае свята, непрацоўны дзень.",
            iconType = "women"
        ),
        BelarusHoliday(
            dateText = "15 сакавіка",
            title = "Дзень Канстытуцыі Беларусі",
            type = "Дзяржаўнае",
            isDayOff = false,
            description = "Прыняцце Асноўнага Закона Рэспублікі Беларусь у 1994 годзе.",
            iconType = "constitution"
        ),
        BelarusHoliday(
            dateText = "1 мая",
            title = "Свята працы",
            type = "Дзяржаўнае",
            isDayOff = true,
            description = "Традыцыйнае вясновае свята міру і стваральнай працы.",
            iconType = "work"
        ),
        BelarusHoliday(
            dateText = "9 мая",
            title = "Дзень Перамогі",
            type = "Дзяржаўнае",
            isDayOff = true,
            description = "Дзень Перамогі ў Вялікай Айчыннай вайне. Усенароднае свята воінскай славы.",
            iconType = "victory"
        ),
        BelarusHoliday(
            dateText = "9-ы дзень пасля Вялікадня",
            title = "Радаўніца",
            type = "Рэлігійнае",
            isDayOff = true,
            description = "Дзень памінання продкаў у Беларусі. Непрацоўны дзень.",
            iconType = "memory"
        ),
        BelarusHoliday(
            dateText = "3 ліпеня",
            title = "Дзень Незалежнасці Рэспублікі Беларусь",
            type = "Дзяржаўнае",
            isDayOff = true,
            description = "Галоўнае свята беларускай дзяржаўнасці, дзень вызвалення Мінска ад захопнікаў.",
            iconType = "state"
        ),
        BelarusHoliday(
            dateText = "6–7 ліпеня",
            title = "Купалле",
            type = "Народнае",
            isDayOff = false,
            description = "Старадаўняе народнае свята з песнямі, традыцыйнымі абрадамі і пошукам папараць-кветкі.",
            iconType = "folk"
        ),
        BelarusHoliday(
            dateText = "7 лістапада",
            title = "Дзень Кастрычніцкай рэвалюцыі",
            type = "Дзяржаўнае",
            isDayOff = true,
            description = "Афіцыйны святочны непрацоўны дзень у краіне.",
            iconType = "autumn"
        ),
        BelarusHoliday(
            dateText = "25 снежня",
            title = "Раство Хрыстова (каталіцкае)",
            type = "Рэлігійнае",
            isDayOff = true,
            description = "Свята Нараджэння Хрыстова па грыгарыянскім календары.",
            iconType = "christmas"
        )
    )

    val TRANSFERS_INFO = "Паводле пастаноў Урада Беларусі: калі святочны непрацоўны дзень прыпадае на аўторак або чацвер, панядзелак або пятніца пераносяцца на адпаведную суботу для стварэння працяглых выхадных."
}
