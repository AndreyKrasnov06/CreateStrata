# Create: Strata

[🇬🇧 English](#-english) | [🇷🇺 Русский](#-русский)

<a name="-english"></a>
## 🇬🇧 English
A geological add-on for the **Create** mod that completely overhauls ore generation to mimic TerraFirmaCraft mechanics. Gone are the days of finding scattered tiny clusters of ores — prepare to hunt down massive, rare, realistically generated geological strata and veins.

### Features
- **Massive Geological Formations:** Ores now generate in enormous, biome-dependent veins (Pipes, Discs, and Clusters) containing thousands of blocks, rather than small scattered pockets.
- **Data-Driven Ore System:** Overworld and Nether generation is thoroughly rebuilt using noise samplers, precise climates, and custom random salts to guarantee realistic, non-overlapping strata generation.
- **Mechanical Radar:** A powerful new block powered by kinetics (requires 16 SU per RPM) that mathematically scans the chunk matrix to help prospectors pinpoint large ore deposits. Features dynamic cardinal UI scaling and tooltips.
- **Admin Command:** /locatevein <vein_type> correctly respects noise biome humidity and precipitation conditions.
- **i18n Localization:** Fully translated into en_us and u_ru. 

### How to Prospect
Finding ores works fundamentally differently from Vanilla Minecraft:
1. **Explore Climates:** Veins generate based on temperature and moisture (e.g. some ores only spawn in arid, dry peaks, while others need deep aquatic environments).
2. **Setup the Radar:** Build a Mechanical Radar. Feed it kinetic energy. The higher the RPM, the larger the chunk grid the radar can physically scan.
3. **Scan Grid:** Watch the display map to see color-coded dots representing exact mathematical vein generation centers. The map locks to North and provides coordinate tooltips.

### Modpack Creators: How to Add Custom Veins
Create: Strata is 100% Data-Driven! You do not need to write Java or modify the Mod JAR to add hundreds of custom veins.

#### 1. Add a Custom Vein (Datapack)
Create a normal JSON file in your datapack under data/<your_namespace>/worldgen/configured_feature/my_new_vein.json.

Specify the feature type (createstrata:pipe_vein, createstrata:disc_vein, or createstrata:cluster_vein) and add your configuration parameters:

``json
{
  "type": "createstrata:pipe_vein",
  "config": {
    "radius": 16,
    "top_y": 64,
    "bottom_y": -64,
    "rarity": 1500,
    "min_temperature": -1.0,
    "max_temperature": 1.0,
    "min_moisture": -1.0,
    "max_moisture": 1.0,
    "biomes": "#minecraft:is_overworld",
    "replace_target": "minecraft:stone",
    "ore_state": {
      "Name": "minecraft:diamond_ore"
    },
    "salt": 425679,
    "color": "#00FFFF",
    "display_name": "mypack.vein.unobtanium"
  }
}
``
*The Mechanical Radar will automatically read this data and display your new vein dynamically using the provided colorHex!*

#### 2. Add Localization (Translations)
To translate the vein name inside the Mechanical Radar UI and the /locatevein command:

**Option A (Resource Pack):**
Add the language key to your Resource Pack/KubeJS scripts under ssets/<your_namespace>/lang/en_us.json:
``json
{
  "mypack.vein.unobtanium": "Unobtanium Vein"
}
``

**Option B (Lazy Fallback):**
If this is for a private server and you don't want to mess with resource packs, simply write the plain text directly in the datapack json:
``json
"display_name": "Unobtanium Vein"
``
*The UI will attempt to translate it, fail, and output the raw text straight to the screen automatically.*

---

<a name="-русский"></a>
## 🇷🇺 Русский
Геологическое дополнение для мода **Create**, которое полностью перерабатывает генерацию руд, копируя механики TerraFirmaCraft. Прошли времена крошечных разбросанных кластеров руд — приготовьтесь искать массивные, редкие, реалистично генерируемые геологические пласты и жилы.

### Особенности
- **Массивные геологические образования:** Руды генерируются в огромных, зависящих от биома жилах (Трубы, Диски и Кластеры), содержащих тысячи блоков, вместо небольших разбросанных карманов.
- **Data-Driven генерация:** Генерация ада и обычного мира полностью перестроена с использованием карт шумов, точного климата и кастомных солей рандомизации, что гарантирует появление реалистичных, непересекающихся рудных пластов.
- **Механический радар:** Новый мощный блок, работающий от кинетической энергии (требует 16 SU за каждый RPM). Он математически сканирует матрицу чанков, чтобы помочь геологам точно находить крупные залежи руд. Имеет динамическое масштабирование и всплывающие подсказки координат.
- **Команда администратора:** Команда /locatevein <тип_жилы> корректно учитывает биомы и условия влажности шумов при поиске.
- **Локализация:** Полностью переведено на русский (u_ru) и английский (en_us).

### Как проводить разведку
Поиск руд в корне отличается от ванильного Minecraft:
1. **Исследуйте климат:** Жилы генерируются на основе температуры и влажности (например, некоторые руды появляются только в засушливых, сухих горах, а другие — глубоко в водных биомах).
2. **Установите радар:** Постройте Механический радар и подведите к нему вращение. Чем выше RPM (скорость), тем больший радиус чанков радар может физически просканировать.
3. **Проверьте сетку:** Откройте интерфейс радара, чтобы увидеть цветные точки, отображающие точные центры математической генерации жил. Карта фиксирует север сверху и показывает всплывающие координаты руд.

### Для создателей сборок: Как добавлять свои жилы
Create: Strata на 100% управляется данными (Data-Driven)! Вам не нужно писать код на Java, чтобы добавить сотни своих кастомных жил. За это отвечают JSON файлы.

#### 1. Добавление кастомной жилы (Датапак)
Создайте обычный JSON файл в вашем датапаке по пути data/<ваш_namespace>/worldgen/configured_feature/my_new_vein.json.

Укажите тип генератора (createstrata:pipe_vein, createstrata:disc_vein или createstrata:cluster_vein) и добавьте свои параметры конфигурации:

``json
{
  "type": "createstrata:pipe_vein",
  "config": {
    "radius": 16,
    "top_y": 64,
    "bottom_y": -64,
    "rarity": 1500,
    "min_temperature": -1.0,
    "max_temperature": 1.0,
    "min_moisture": -1.0,
    "max_moisture": 1.0,
    "biomes": "#minecraft:is_overworld",
    "replace_target": "minecraft:stone",
    "ore_state": {
      "Name": "minecraft:diamond_ore"
    },
    "salt": 425679,
    "color": "#00FFFF",
    "display_name": "mypack.vein.unobtanium"
  }
}
``
*Механический радар автоматически прочитает эти данные и отобразит вашу новую жилу на экране, используя указанный в файле color!*

#### 2. Добавление перевода
Чтобы перевести название жилы внутри интерфейса Механического радара и команды /locatevein:

**Вариант А (Ресурспак):**
Добавьте ключ адаптации языка в ваш Ресурспак (или скрипты KubeJS) по пути ssets/<ваш_namespace>/lang/ru_ru.json:
``json
{
  "mypack.vein.unobtanium": "Жила Анобтаниума"
}
``

**Вариант Б (Быстрый хардкод):**
Если разработка ведется для приватного русскоязычного сервера и вы не хотите возиться с ресурспаками, просто укажите кириллицу напрямую в JSON датапака:
``json
"display_name": "Жила Анобтаниума"
``
*Интерфейс попытается перевести ключ, не найдет его файлов, и автоматически выведет ваш сырой текст на радар без сбоев.*

### Зависимости
- NeoForge 1.21.1
- [Create](https://modrinth.com/mod/create)
- Flywheel
- Ponder

### License
All Rights Reserved.
