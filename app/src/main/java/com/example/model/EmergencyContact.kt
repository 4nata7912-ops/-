package com.example.model

data class EmergencyContact(
    val id: String,
    val number: String,
    val title: String,
    val subtitle: String,
    val description: String,
    val category: String,
    val iconType: String,
    val isPriority: Boolean = false
)

object BelarusEmergencyDirectory {
    val CONTACTS = listOf(
        EmergencyContact(
            id = "mchs",
            number = "101",
            title = "101 / 112 — МНС Рэспублікі Беларусь",
            subtitle = "Пажарная і аварыйна-ратавальная служба",
            description = "Адзіны тэлефон выратавання пры пажарах, надзвычайных сітуацыях і стыхійных бедствах. Кругласутачна і бясплатна з любых тэлефонаў.",
            category = "Экстранныя",
            iconType = "fire",
            isPriority = true
        ),
        EmergencyContact(
            id = "police",
            number = "102",
            title = "102 — Міліцыя",
            subtitle = "Органы ўнутраных спраў",
            description = "Ахова грамадскага парадку, паведамленне аб правапарушэннях і пагрозе грамадзянам.",
            category = "Экстранныя",
            iconType = "police",
            isPriority = true
        ),
        EmergencyContact(
            id = "ambulance",
            number = "103",
            title = "103 — Хуткая медыцынская дапамога",
            subtitle = "Служба неадкладнай дапамогі",
            description = "Выклік медыцынскай брыгады пры раптоўных захворваннях, траўмах і вострых станах.",
            category = "Экстранныя",
            iconType = "ambulance",
            isPriority = true
        ),
        EmergencyContact(
            id = "gas",
            number = "104",
            title = "104 — Аварыйная служба газу",
            subtitle = "УП «Мінгаз» і абласныя газавыя гаспадаркі",
            description = "Тэрміновы выклік пры паху газу ў доме, пад'ездзе ці на вуліцы, пашкоджанні газаправодаў.",
            category = "Экстранныя",
            iconType = "gas",
            isPriority = true
        ),
        EmergencyContact(
            id = "utility_115",
            number = "115",
            title = "115 — Дыспетчарская служба ЖКГ",
            subtitle = "Партал «Мая рэспубліка 115.бел»",
            description = "Адзіны кантакт-цэнтр па пытаннях водазабеспячэння, ацяплення, асвятлення і добраўпарадкавання двароў.",
            category = "Камунальныя",
            iconType = "utility"
        ),
        EmergencyContact(
            id = "railway_151",
            number = "151",
            title = "151 — Даведачная служба чыгункі БЧ",
            subtitle = "Беларуская чыгунка",
            description = "Расклад цягнікоў, наяўнасць месцаў, правілы перавозкі і браніраванне праязных дакументаў.",
            category = "Даведачныя",
            iconType = "train"
        ),
        EmergencyContact(
            id = "infoline_185",
            number = "185",
            title = "185 — Інфалінія і даведка",
            subtitle = "Адзіная інфармацыйна-даведачная служба",
            description = "Адрасы і тэлефоны прадпрыемстваў, аптэк і дзяржаўных устаноў па ўсёй Беларусі.",
            category = "Даведачныя",
            iconType = "info"
        ),
        EmergencyContact(
            id = "child_helpline",
            number = "88011001611",
            title = "8-801-100-1611 — Дзіцячая лінія даверу",
            subtitle = "Бясплатная псіхалагічная падтрымка для непаўналетніх",
            description = "Ананімная кваліфікаваная дапамога спецыялістаў у складаных жыццёвых абставінах.",
            category = "Псіхалагічная дапамога",
            iconType = "support"
        ),
        EmergencyContact(
            id = "adult_crisis",
            number = "80173524444",
            title = "8-017-352-4444 — Экстраная псіхалагічная дапамога",
            subtitle = "Мінскі гарадскі цэнтр псіхатэрапіі",
            description = "Кругласутачная тэлефонная лінія псіхалагічнай падтрымкі для дарослых у крызісных сітуацыях.",
            category = "Псіхалагічная дапамога",
            iconType = "crisis"
        )
    )
}
