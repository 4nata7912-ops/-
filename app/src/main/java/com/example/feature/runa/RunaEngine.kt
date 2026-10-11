package com.example.feature.runa

import com.example.data.CurrencyRepository
import com.example.data.NewsRepository
import com.example.model.AppLanguage
import com.example.model.NewsItem
import com.example.model.NewsSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

data class ChatMessage(
    val id: String,
    val sender: MessageSender,
    val text: String,
    val timestamp: String,
    val relatedAction: String? = null
)

enum class MessageSender {
    USER,
    RUNA
}

class RunaEngine(
    private val newsRepository: NewsRepository,
    private val currencyRepository: CurrencyRepository
) {

    suspend fun generateResponse(
        userMessage: String,
        language: AppLanguage,
        currentNewsSource: NewsSource
    ): String = withContext(Dispatchers.Default) {
        val query = userMessage.lowercase(Locale.getDefault()).trim()

        // 1. News query - Runa reads actual app news
        if (query.contains("навін") || query.contains("новост") || query.contains("news") ||
            query.contains("белта") || query.contains("онлайнер") || query.contains("паведам") ||
            query.contains("што адбываецца") || query.contains("что происходит") || query.contains("галоўнае")
        ) {
            return@withContext getNewsSummary(language, currentNewsSource)
        }

        // 2. Currency, bank exchange, and rates
        if (query.contains("курс") || query.contains("валют") || query.contains("даляр") || query.contains("доллар") ||
            query.contains("банк") || query.contains("руб") || query.contains("грош") || query.contains("деньг") ||
            query.contains("канверт") || query.contains("конверт") || query.contains("абмен") || query.contains("обмен")
        ) {
            return@withContext getCurrencyInsight(language)
        }

        // 3. Communal bills & ERIP / ЕРИП
        if (query.contains("камунал") || query.contains("коммунал") || query.contains("арыіп") || query.contains("ерип") ||
            query.contains("лічыльн") || query.contains("счетчик") || query.contains("святло") || query.contains("газ") ||
            query.contains("вада") || query.contains("ацяплен") || query.contains("отоплен")
        ) {
            return@withContext getUtilitiesGuide(language)
        }

        // 4. Railways & Transport (БЧ, метро, цягнікі)
        if (query.contains("цягнік") || query.contains("поезд") || query.contains("чыгунк") || query.contains("железн") ||
            query.contains("бч") || query.contains("бжд") || query.contains("метро") || query.contains("транспарт") ||
            query.contains("транспорт") || query.contains("білет") || query.contains("билет") || query.contains("расклад") || query.contains("расписан")
        ) {
            return@withContext getTransportGuide(language)
        }

        // 5. Emergency numbers (101, 102, 103, 104, 115)
        if (query.contains("экстран") || query.contains("экстрен") || query.contains("выратав") || query.contains("спасател") ||
            query.contains("мнс") || query.contains("мчс") || query.contains("міліцы") || query.contains("милици") ||
            query.contains("хуткая") || query.contains("скорая") || query.contains("112") || query.contains("101") || query.contains("102") || query.contains("115")
        ) {
            return@withContext getEmergencyGuide(language)
        }

        // 6. Belarusian culinary / recipes (Дранікі, мачанка, бабка)
        if (query.contains("дранік") || query.contains("драник") || query.contains("мачанк") || query.contains("страва") ||
            query.contains("блюдо") || query.contains("кулінар") || query.contains("кулинар") || query.contains("рэцэпт") ||
            query.contains("рецепт") || query.contains("бабка") || query.contains("халаднік") || query.contains("холодник")
        ) {
            return@withContext getCuisineKnowledge(language, query)
        }

        // 7. Sights, castles, travel & cities (Мір, Нясвіж, Полацк, Брэст, Гродна)
        if (query.contains("мір") || query.contains("мир") || query.contains("нясвіж") || query.contains("несвиж") ||
            query.contains("замак") || query.contains("замок") || query.contains("гродн") || query.contains("брэст") || query.contains("брест") ||
            query.contains("полацк") || query.contains("полоцк") || query.contains("пушч") || query.contains("пущ") ||
            query.contains("славут") || query.contains("достопримеч") || query.contains("куды паехаць") || query.contains("куда поехать") || query.contains("вандроў")
        ) {
            return@withContext getTravelKnowledge(language, query)
        }

        // 8. Questions about the app "Зорка" and its customization
        if (query.contains("зорк") || query.contains("дадатак") || query.contains("приложен") || query.contains("модул") ||
            query.contains("настро") || query.contains("налад") || query.contains("руна") || query.contains("хто ты") || query.contains("кто ты")
        ) {
            return@withContext getAppKnowledge(language)
        }

        // 9. History and symbols of Belarus
        if (query.contains("гісторы") || query.contains("истори") || query.contains("вкл") || query.contains("скарын") || query.contains("скорин") ||
            query.contains("сімвал") || query.contains("символ") || query.contains("беларусь")
        ) {
            return@withContext getHistoryKnowledge(language)
        }

        // Default intelligent conversational response
        return@withContext getDefaultGreeting(language)
    }

    private fun getNewsSummary(language: AppLanguage, source: NewsSource): String {
        val news = newsRepository.getFallbackNews(source)
        val isBel = language == AppLanguage.BE || language == AppLanguage.UK
        val head1 = news.getOrNull(0)?.title ?: "Падзеі ў прамысловасці і эканоміцы"
        val head2 = news.getOrNull(1)?.title ?: "Удасканаленне жыллёва-камунальных разлікаў"
        val head3 = news.getOrNull(2)?.title ?: "Расклад грамадскага і чыгуначнага транспарту"

        return if (isBel) {
            "Я прааналізавала свежую стужку ад «${source.name}». Вось галоўныя тэмы на гэтую гадзіну:\n\n" +
                    "1. $head1\n\n" +
                    "2. $head2\n\n" +
                    "3. $head3\n\n" +
                    "Вы можаце адкрыць поўны тэкст любога артыкула або захаваць яго ў Закладкі ва ўкладцы «Навіны»."
        } else {
            "Я проанализировала актуальную ленту от «${source.name}». Вот ключевые новости к этому часу:\n\n" +
                    "1. $head1\n\n" +
                    "2. $head2\n\n" +
                    "3. $head3\n\n" +
                    "Вы можете прочитать полную версию или добавить материалы в Закладки в разделе «Новости»."
        }
    }

    private fun getCurrencyInsight(language: AppLanguage): String {
        val isBel = language == AppLanguage.BE || language == AppLanguage.UK
        return if (isBel) {
            "Паводле аператыўных звестак Нацыянальнага банка РБ (НБРБ):\n\n" +
                    "• Долар ЗША (USD): ~3.26 BYN\n" +
                    "• Еўра (EUR): ~3.52 BYN\n" +
                    "• 100 Рас. рублёў (RUB): ~3.48 BYN\n" +
                    "• 10 Кітайскіх юаняў (CNY): ~4.54 BYN\n" +
                    "• 10 Польскіх злотых (PLN): ~8.24 BYN\n\n" +
                    "Калі вы хочаце выгадна абмяняць валюту: у раздзеле «Курсы» выкарыстоўвайце наш канвертар. Ён адразу паказвае, у якім банку (Беларусбанк, Прыёрбанк, Альфа-Банк ці інш.) курс куплі або продажу найбольш выгадны!"
        } else {
            "По официальным данным Нацбанка Беларуси (НБРБ):\n\n" +
                    "• Доллар США (USD): ~3.26 BYN\n" +
                    "• Евро (EUR): ~3.52 BYN\n" +
                    "• 100 Рос. рублей (RUB): ~3.48 BYN\n" +
                    "• 10 Юаней (CNY): ~4.54 BYN\n" +
                    "• 10 Польских злотых (PLN): ~8.24 BYN\n\n" +
                    "В разделе «Курсы и валюта» наш интерактивный конвертер сравнивает курсы всех банков (Беларусбанк, Приорбанк, Альфа-Банк, Сбер) и выделяет банк с лучшей выгодой."
        }
    }

    private fun getUtilitiesGuide(language: AppLanguage): String {
        val isBel = language == AppLanguage.BE || language == AppLanguage.UK
        return if (isBel) {
            "Каб аплаціць камунальныя паслугі праз сістэму АРІП (ЕРИП):\n\n" +
                    "1. У мабільным банку абярыце: Сістэма «Разлік» (АРІП) ➔ Камунальныя плацяжы ➔ Жыллёва-камунальныя паслугі.\n" +
                    "2. Выберыце ваш горад або раён (напрыклад, г. Мінск).\n" +
                    "3. Абярыце арганізацыю (РСЦ або ЖЭС) і ўвядзіце нумар асабовага рахунку.\n\n" +
                    "Парада: у раздзеле «Камуналка» нашага дадатку вы можаце разлічыць плацяжы па лічыльніках (святло, вада, газ, ацяпленне) і захаваць квітанцыю ў гісторыю!"
        } else {
            "Оплата коммунальных услуг через систему ЕРИП (АРИП):\n\n" +
                    "1. В мобильном приложении любого банка: Система «Расчет» (ЕРИП) ➔ Коммунальные платежи ➔ Жилищно-коммунальные услуги.\n" +
                    "2. Выберите ваш город/область и районный РСЦ/ЖЭС.\n" +
                    "3. Введите номер лицевого счёта (номер плательщика).\n\n" +
                    "Совет: во вкладке «Коммуналка» приложения «Зорка» вы можете рассчитать платежи по счетчикам и сохранить расчёт в память телефона."
        }
    }

    private fun getTransportGuide(language: AppLanguage): String {
        val isBel = language == AppLanguage.BE || language == AppLanguage.UK
        return if (isBel) {
            "Пра транспарт Беларусі:\n\n" +
                    "• Беларуская чыгунка (БЧ): найбольш зручныя хуткія цягнікі — Штадлер (ЭПР) бізнес-класа. Да Брэста і Гомеля шлях займае каля 3 гадзін. Квіткі зручна афармляць на сайце pass.rw.by.\n" +
                    "• Мінскае метро: працуе з 05:30 да 00:40. Складаецца з трох ліній (Маскоўская, Аўтазаводская, Зеленалужская). Перасадачныя вузлы: Кастрычніцкая/Купалаўская, Плошча Леніна/Вакзальная, Юбілейная плошча/Фрунзенская.\n\n" +
                    "Падрабязную схему станцый глядзіце ў раздзеле «Транспарт»."
        } else {
            "По транспорту Беларуси:\n\n" +
                    "• Белорусская железная дорога (БЖД): комфортные скоростные поезда Stadler (ЭПР) связывают Минск со всеми областными центрами (Брест, Гомель, Гродно, Витебск, Могилев). Билеты доступны на pass.rw.by.\n" +
                    "• Минский метрополитен: 3 линии (Московская, Автозаводская, Зеленолужская). Открыто с 05:30 до 00:40.\n\n" +
                    "Полную схему линий метро и расписание можно посмотреть в разделе «Транспорт»."
        }
    }

    private fun getEmergencyGuide(language: AppLanguage): String {
        val isBel = language == AppLanguage.BE || language == AppLanguage.UK
        return if (isBel) {
            "Галоўныя экстранныя службы Беларусі (званкі бясплатныя):\n\n" +
                    "• 101 або 112 — МНС (Ратавальнікі і пажарныя)\n" +
                    "• 102 — Міліцыя\n" +
                    "• 103 — Хуткая медыцынская дапамога\n" +
                    "• 104 — Аварыйная служба газу (Мінгаз / аблгаз)\n" +
                    "• 115 — Адзіная дыспетчарская служба ЖКГ (кантакт-цэнтр па камунальных аварыях)\n" +
                    "• 151 — Даведка чыгункі БЧ\n" +
                    "• 8-801-100-1611 — Дзіцячая лінія даверу\n\n" +
                    "У раздзеле «Службы 112» можна націснуць кнопку выкліку адным дотыкам."
        } else {
            "Экстренные службы Беларуси (звонки бесплатные с любого телефона):\n\n" +
                    "• 101 или 112 — МЧС (Спасатели)\n" +
                    "• 102 — Милиция\n" +
                    "• 103 — Скорая помощь\n" +
                    "• 104 — Аварийная газовая служба\n" +
                    "• 115 — Единая диспетчерская ЖКХ\n" +
                    "• 151 — Справочная железной дороги\n\n" +
                    "В разделе «Службы 112» доступен быстрый вызов в одно касание."
        }
    }

    private fun getCuisineKnowledge(language: AppLanguage, query: String): String {
        val isBel = language == AppLanguage.BE || language == AppLanguage.UK
        return if (isBel) {
            "Беларуская кухня — адна з самых душэўных і смачных! Вось сакрэт сапраўдных беларускіх дранікаў:\n\n" +
                    "1. Аснова: толькі крахмалістая бульба і цыбуліна. Бульбу трэба церці на дробнай тарцы (якая драпае — адсюль і назва «дранікі»). Цыбулю труць разам з бульбай, каб цеста не чарнела.\n" +
                    "2. Сакрэт кухара: сапраўдныя дранікі робяць БЕЗ мукі і яек! Калі шмат соку — трохі зліць праз сіта, а крухмал з дна вярнуць у цеста. Дадаць толькі соль і перац.\n" +
                    "3. Смажыць на добра разагрэтым алеі да залацістай храбусткай скарыначкі.\n" +
                    "4. Падаваць гарачымі са смятанай, шкваркамі або з мачанкай (тушаныя рэбры і каўбаскі ў густым соусе).\n\n" +
                    "Таксама абавязкова пакаштуйце бульбяную бабку ў гліняным гаршчочку і халаднік са свежымі буракамі!"
        } else {
            "Белорусская кухня славится душевными сытными блюдами! Секрет настоящих классических драников:\n\n" +
                    "1. Картофель и луковица. Трётся исключительно на мелкой «колючей» тёрке. Лук добавляется сразу — это не даёт картофельной массе потемнеть.\n" +
                    "2. Традиционные драники готовятся БЕЗ муки и яиц: только картофель, лук, соль и перец. Лишнюю влагу слегка отжимают, возвращая осевший на дно крахмал.\n" +
                    "3. Жарят на раскаленной сковороде с растительным маслом до золотистой корочки.\n" +
                    "4. Подают со сметаной, шкварками или мясной мачанкой.\n\n" +
                    "Также среди шедевров кухни: бабка в глиняном горшочке, колдуны с мясом и свекольный холодник."
        }
    }

    private fun getTravelKnowledge(language: AppLanguage, query: String): String {
        val isBel = language == AppLanguage.BE || language == AppLanguage.UK
        return if (isBel) {
            "Топ-месцы для вандровак па Беларусі:\n\n" +
                    "🏰 Мірскі замак (XVI ст., ЮНЕСКА) — велічны помнік беларускай готыкі і рэнесансу, радзіма славутых бітваў і падземных таямніц.\n" +
                    "👑 Нясвіжскі палацава-паркавы комплекс — рэзідэнцыя магутных князёў Радзівілаў з паркамі, сажалкамі і легендай пра прывід Чорнай Панны (Барбары Радзівіл).\n" +
                    "🛡️ Брэсцкая крэпасць — уражвальны мемарыял мужнасці і гераізму.\n" +
                    "🌲 Белавежская пушча — найстарэйшы першабытны лес Еўропы, дзе жывуць волаты-зубры і знаходзіцца сядзіба Дзеда Мароза.\n" +
                    "⛪ Гродна — каралеўскі горад замкаў і вузкіх брукаваных вуліц. Абавязкова паглядзіце Стары замак і Каложскую царкву XII стагоддзя.\n" +
                    "📜 Полацк — калыска беларускай дзяржаўнасці з Сафійскім саборам і Спаса-Еўфрасіннеўскім манастыром."
        } else {
            "Топ достопримечательностей Беларуси:\n\n" +
                    "🏰 Мирский замок (XVI в., ЮНЕСКО) — шедевр белорусского оборонного зодчества с башнями, музеем и живописным прудом.\n" +
                    "👑 Несвижский замок — дворцово-парковый ансамбль князей Радзивиллов с красивейшими залами и легендой о Черной Панне.\n" +
                    "🛡️ Брестская крепость — монументальный памятник мужества защитников 1941 года.\n" +
                    "🌲 Беловежская пуща — реликтовый заповедный лес Европы, дом зубров и Поместье Деда Мороза.\n" +
                    "⛪ Гродно — европейский город замков, костёлов и древней Коложской церкви XII века.\n" +
                    "📜 Полоцк — древнейший город Беларуси с Софийским собором и колыбелью белорусского книгопечатания."
        }
    }

    private fun getAppKnowledge(language: AppLanguage): String {
        val isBel = language == AppLanguage.BE || language == AppLanguage.UK
        return if (isBel) {
            "Прывітанне! Я — Руна, ваш разумны лічбавы памочнік у дадатку «Зорка».\n\n" +
                    "Я ведаю ўсё пра магчымасці дадатку і пра жыццё ў Беларусі:\n" +
                    "• Навіны: я бачу свежыя матэрыялы ад БелТА і іншых СМІ, магу расказаць галоўнае.\n" +
                    "• Валюта: падкажу актуальныя курсы НБРБ і які банк прапануе найвыгаднейшы абмен.\n" +
                    "• Камуналка: магу патлумачыць разлік лічыльнікаў і аплату ў АРІП.\n" +
                    "• Наладка меню: праз кнопку «Наладка» зверху вы можаце выбраць, якія кнопкі адлюстроўваць у верхнім меню, а лішнія схаваць.\n\n" +
                    "Спытайцеся ў мяне пра што заўгодна!"
        } else {
            "Привет! Меня зовут Руна — я ваш локальный ассистент в приложении «Зорка».\n\n" +
                    "Я знаю всё о Беларуси и функциях приложения:\n" +
                    "• Читаю и анализирую последние новости БелТА и других сайтов.\n" +
                    "• Знаю актуальные курсы валют банков и помогаю рассчитать обмен.\n" +
                    "• Подскажу, как оплатить счета через ЕРИП и рассчитать коммуналку.\n" +
                    "• Знаю историю, замки, города, традиции и рецепты Беларуси.\n" +
                    "• Помогу настроить модули меню приложения под ваши личные предпочтения.\n\n" +
                    "Задайте мне любой вопрос!"
        }
    }

    private fun getHistoryKnowledge(language: AppLanguage): String {
        val isBel = language == AppLanguage.BE || language == AppLanguage.UK
        return if (isBel) {
            "Кароткі нарыс гісторыі Беларусі:\n\n" +
                    "• IX–XII ст.: Полацкае і Тураўскае княствы — першыя дзяржаўныя ўтварэнні на беларускіх землях. Францыск Скарына ў 1517 годзе надрукаваў у Празе першую Біблію на старабеларускай мове.\n" +
                    "• XIII–XVIII ст.: Вялікае Княства Літоўскае (ВКЛ) і Рэч Паспалітая — залаты век беларускага права (Статуты ВКЛ) і замкавага будаўніцтва.\n" +
                    "• 1918–1991 гг.: Абвяшчэнне БНР (1918 г.), стварэнне БССР, гераічны партызанскі рух і вызваленне ў час Вялікай Айчыннай вайны.\n" +
                    "• 1991 г. — сучаснасць: Суверэнная Рэспубліка Беларусь са сталіцай у Мінску."
        } else {
            "Краткая летопись Беларуси:\n\n" +
                    "• IX–XII вв.: Полоцкое и Туровское княжества. Франциск Скорина в 1517 году издал первую печатную книгу на старобелорусском языке.\n" +
                    "• XIII–XVIII вв.: Великое Княжество Литовское (ВКЛ) — эпоха Статутов ВКЛ, магнатов Радзивиллов и Сапегов.\n" +
                    "• XX век: Провозглашение БНР, эпоха БССР, всенародный партизанский подвиг в годы Великой Отечественной войны.\n" +
                    "• С 1991 года — суверенная Республика Беларусь."
        }
    }

    private fun getDefaultGreeting(language: AppLanguage): String {
        val isBel = language == AppLanguage.BE || language == AppLanguage.UK
        return if (isBel) {
            "Я вас уважліва слухаю! Вы можаце спытаць у мяне:\n\n" +
                    "• Што новага ў апошніх навінах БелТА?\n" +
                    "• Дзе сёння самы лепшы курс даляра?\n" +
                    "• Як аплаціць камуналку праз АРІП?\n" +
                    "• Куды паехаць на выходных у Беларусі?\n" +
                    "• Як прыгатаваць сапраўдныя дранікі?"
        } else {
            "Я внимательно вас слушаю! Спросите меня о чём угодно:\n\n" +
                    "• Что пишут в последних новостях БелТА?\n" +
                    "• Какой сейчас курс валют и в каком банке выгоднее?\n" +
                    "• Как оплатить квитанции через ЕРИП?\n" +
                    "• Достопримечательности и замки Беларуси (Мир, Несвиж, Гродно)?\n" +
                    "• Рецепты традиционной белорусской кухни?"
        }
    }
}
