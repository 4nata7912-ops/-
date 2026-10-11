package com.example.model

data class BelarusHoliday(
    val dateText: String,
    val title: String,
    val type: String, // "Дзяржаўнае", "Рэлігійнае", "Народнае", "Перанос"
    val isDayOff: Boolean,
    val description: String,
    val emoji: String
)

object BelarusCalendarData {
    val HOLIDAYS = listOf(
        BelarusHoliday(
            dateText = "1–2 студзеня",
            title = "Новы год",
            type = "Дзяржаўнае",
            isDayOff = true,
            description = "Афіцыйныя непрацоўныя святочныя дні ў Беларусі.",
            emoji = "🎄"
        ),
        BelarusHoliday(
            dateText = "7 студзеня",
            title = "Раство Хрыстова (праваслаўнае)",
            type = "Рэлігійнае",
            isDayOff = true,
            description = "Свята Нараджэння Хрыстова па юліянскім календары.",
            emoji = "⭐"
        ),
        BelarusHoliday(
            dateText = "8 сакавіка",
            title = "Дзень жанчын",
            type = "Дзяржаўнае",
            isDayOff = true,
            description = "Міжнароднае жаночае свята, непрацоўны дзень.",
            emoji = "💐"
        ),
        BelarusHoliday(
            dateText = "15 сакавіка",
            title = "Дзень Канстытуцыі Беларусі",
            type = "Дзяржаўнае",
            isDayOff = false,
            description = "Прыняцце Асноўнага Закона Рэспублікі Беларусь у 1994 годзе.",
            emoji = "📜"
        ),
        BelarusHoliday(
            dateText = "2 красавіка",
            title = "Дзень яднання народаў Беларусі і Расіі",
            type = "Дзяржаўнае",
            isDayOff = false,
            description = "Свята Саюзнай дзяржавы Беларусі і Расіі.",
            emoji = "🤝"
        ),
        BelarusHoliday(
            dateText = "1 мая",
            title = "Свята працы",
            type = "Дзяржаўнае",
            isDayOff = true,
            description = "Традыцыйнае вясновае свята міру і працы.",
            emoji = "🌸"
        ),
        BelarusHoliday(
            dateText = "9 мая",
            title = "Дзень Перамогі",
            type = "Дзяржаўнае",
            isDayOff = true,
            description = "Дзень Перамогі ў Вялікай Айчыннай вайне. Усенароднае свята памяці і славы.",
            emoji = "🎖️"
        ),
        BelarusHoliday(
            dateText = "9-ы дзень пасля Вялікадня",
            title = "Радаўніца",
            type = "Рэлігійнае",
            isDayOff = true,
            description = "Дзень памінання памерлых продкаў у Беларусі. Непрацоўны дзень.",
            emoji = "🕯️"
        ),
        BelarusHoliday(
            dateText = "Другая нядзеля мая",
            title = "Дзень Дзяржаўнага сцяга, герба і гімна",
            type = "Дзяржаўнае",
            isDayOff = false,
            description = "Ушанаванне галоўных дзяржаўных сімвалаў Рэспублікі Беларусь.",
            emoji = "🇧🇾"
        ),
        BelarusHoliday(
            dateText = "3 ліпеня",
            title = "Дзень Незалежнасці Рэспублікі Беларусь",
            type = "Дзяржаўнае",
            isDayOff = true,
            description = "Галоўнае свята беларускай дзяржаўнасці, дзень вызвалення Мінска ад нямецка-фашысцкіх захопнікаў.",
            emoji = "🎇"
        ),
        BelarusHoliday(
            dateText = "6–7 ліпеня",
            title = "Купалле",
            type = "Народнае",
            isDayOff = false,
            description = "Старадаўняе свята сонцастаяння з вогнішчамі, пляценнем вянкоў і пошукам папараць-кветкі.",
            emoji = "🌿"
        ),
        BelarusHoliday(
            dateText = "7 лістапада",
            title = "Дзень Кастрычніцкай рэвалюцыі",
            type = "Дзяржаўнае",
            isDayOff = true,
            description = "Афіцыйны непрацоўны дзень у Беларусі.",
            emoji = "🚩"
        ),
        BelarusHoliday(
            dateText = "25 снежня",
            title = "Раство Хрыстова (каталіцкае)",
            type = "Рэлігійнае",
            isDayOff = true,
            description = "Свята Нараджэння Хрыстова па грыгарыянскім календары.",
            emoji = "✨"
        )
    )

    val TRANSFERS_INFO = "Паводле пастаноў Савета Міністраў Беларусі: калі святочны непрацоўны дзень прыпадае на аўторак або чацвер, панядзелак або пятніца пераносяцца на адпаведную суботу для стварэння бесперапынных доўгіх выходных."
}
