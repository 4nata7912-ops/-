package com.example.model

data class EmergencyContact(
    val id: String,
    val number: String,
    val title: String,
    val subtitle: String,
    val description: String,
    val category: String, // "Экстранныя", "Камунальныя", "Даведачныя", "Псіхалагічная дапамога"
    val iconEmoji: String,
    val isPriority: Boolean = false
)

object BelarusEmergencyDirectory {
    val CONTACTS = listOf(
        EmergencyContact(
            id = "mchs",
            number = "101",
            title = "101 / 112 — МНС Рэспублікі Беларусь",
            subtitle = "Пажарная і аварыйна-ратавальная служба",
            description = "Адзіны тэлефон выратавання пры пажарах, надзвычайных сітуацыях, стыхійных бедствах і аварыях. Працуе кругласутачна і бясплатна з любых тэлефонаў.",
            category = "Экстранныя",
            iconEmoji = "🚒",
            isPriority = true
        ),
        EmergencyContact(
            id = "police",
            number = "102",
            title = "102 — Міліцыя",
            subtitle = "Органы ўнутраных спраў",
            description = "Ахова грамадскага парадку, паведамленне аб правапарушэннях, крадзяжах і небяспецы для жыцця. Кругласутачна.",
            category = "Экстранныя",
            iconEmoji = "🚔",
            isPriority = true
        ),
        EmergencyContact(
            id = "ambulance",
            number = "103",
            title = "103 — Хуткай медыцынскай дапамогі",
            subtitle = "Неадкладная медыцынская дапамога",
            description = "Выклік брыгады хуткай дапамогі пры раптоўных захворваннях, траўмах, атручваннях і пагрозе здароўю. Кругласутачна.",
            category = "Экстранныя",
            iconEmoji = "🚑",
            isPriority = true
        ),
        EmergencyContact(
            id = "gas",
            number = "104",
            title = "104 — Аварыйная газавая служба",
            subtitle = "УП «Мінгаз» і абласныя газавыя гаспадаркі",
            description = "Тэрміновы выклік пры паху газу ў кватэры, пад'ездзе або на вуліцы, пашкоджанні газаправодаў або збоях абсталявання.",
            category = "Экстранныя",
            iconEmoji = "🔥",
            isPriority = true
        ),
        EmergencyContact(
            id = "utility_115",
            number = "115",
            title = "115 — Адзіная служба ЖКГ «Кантакт-цэнтр»",
            subtitle = "Партал «Мая рэспубліка 115.бел»",
            description = "Прыём заяў па пытаннях водазабеспячэння, ацяплення, асвятлення пад'ездаў і вуліц, прыбірання двароў і працы ліфтаў па ўсёй краіне.",
            category = "Камунальныя",
            iconEmoji = "🛠️"
        ),
        EmergencyContact(
            id = "railway_151",
            number = "151",
            title = "151 — Даведачная служба БЧ",
            subtitle = "Беларуская чыгунка",
            description = "Расклад цягнікоў, наяўнасць білетаў, правілы перавозкі пасажыраў і багажу, браніраванне месцаў.",
            category = "Даведачныя",
            iconEmoji = "🚆"
        ),
        EmergencyContact(
            id = "infoline_185",
            number = "185",
            title = "185 — Інфалінія і даведка",
            subtitle = "Адзіная інфармацыйна-даведачная служба",
            description = "Тэлефоны і адрасы арганізацый, прадпрыемстваў, аптэк і ўстаноў ва ўсіх рэгіёнах Беларусі.",
            category = "Даведачныя",
            iconEmoji = "📞"
        ),
        EmergencyContact(
            id = "child_helpline",
            number = "88011001611",
            title = "8-801-100-1611 — Дзіцячая лінія даверу",
            subtitle = "Бясплатная псіхалагічная падтрымка для дзяцей і падлеткаў",
            description = "Кругласутачная бясплатная ананімная дапамога спецыялістаў па праблемах зносін, канфліктаў і перажыванняў.",
            category = "Псіхалагічная дапамога",
            iconEmoji = "🤝"
        ),
        EmergencyContact(
            id = "adult_crisis",
            number = "80173524444",
            title = "8-017-352-4444 — Экстраная псіхалагічная дапамога",
            subtitle = "Мінскі гарадскі цэнтр псіхатэрапіі",
            description = "Кругласутачная тэлефонная лінія для дарослых, якія апынуліся ў складанай жыццёвай або крызіснай сітуацыі.",
            category = "Псіхалагічная дапамога",
            iconEmoji = "💚"
        )
    )
}
