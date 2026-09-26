<h1 align="center">Create: Strata</h1>

<p align="center">
  <a href="#-english">🇬🇧 English</a> | <a href="#-русский">🇷🇺 Русский</a>
</p>

<a name="-english"></a>
## 🇬🇧 English
Create: Strata is an add-on for the Create mod that overhauls ore generation to mimic TerraFirmaCraft mechanics. Instead of small, scattered clusters, ores generate in large geological veins across the world.

### Features
* **Geological Veins:** Ores generate in large, rare formations (Pipes, Discs, and Clusters) containing substantial amounts of resources.
* **Climate-Based Generation:** Vein placement is mathematically driven by world noise, biome temperature, and moisture, preventing overlaps and adding exploration value.
* **Mechanical Radar:** A kinetic block (`16 SU` at `1 RPM`) that scans the chunk matrix to help locate ore deposits. The display features cardinal scaling, real-world coordinates, and dynamic tooltips.
* **Admin Utilities:** `/locatevein <vein_type>` correctly respects noise biome conditions.
* **Localization:** Fully translated into English (`en_us`) and Russian (`ru_ru`).

### Customization
Create: Strata is completely data-driven. Veins can be added or modified via datapacks. To add a new vein, you must create three files:

**1. Configured Feature (`data/your_namespace/worldgen/configured_feature/my_new_vein.json`):**
Defines the shape, rarity, and internal mechanics.
```json
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
```
*Note:* The `color` defines the dot color on the Mechanical Radar. `display_name` will be used as a translation key (e.g., in your resource pack), or as raw fallback text if no translation is found.

**2. Placed Feature (`data/your_namespace/worldgen/placed_feature/my_new_vein_placed.json`):**
Required by Vanilla Minecraft to process the feature.
```json
{
  "feature": "your_namespace:my_new_vein",
  "placement": [
    { "type": "minecraft:count", "count": 1 },
    { "type": "minecraft:in_square" },
    { "type": "minecraft:biome" }
  ]
}
```

**3. Biome Modifier (`data/your_namespace/neoforge/biome_modifier/add_my_new_vein.json`):**
Instructs NeoForge to inject your vein into the world generation step.
```json
{
  "type": "neoforge:add_features",
  "biomes": "#minecraft:is_overworld",
  "features": "your_namespace:my_new_vein_placed",
  "step": "underground_ores"
}
```

### Dependencies
* NeoForge `1.21.1`
* Create

---

<a name="-русский"></a>
## 🇷🇺 Русский
Create: Strata — это дополнение для мода Create, которое перерабатывает генерацию руд по образу и подобию механик TerraFirmaCraft. Вместо привычных мелких скоплений, руда генерируется крупными геологическими пластами.

### Особенности
* **Геологические жилы:** Руды генерируются в виде крупных редких образований (Трубы, Диски и Кластеры), содержащих большие объемы ресурсов.
* **Генерация по климату:** Расположение жил математически привязано к шумам мира, температуре и влажности биома. Это предотвращает наложение пластов друг на друга и стимулирует исследование мира.
* **Механический радар:** Кинетический блок (потребляет `16 SU` при `1 RPM`), который сканирует сетку чанков для поиска залежей руды. Интерфейс поддерживает масштабирование, привязку к координатам и всплывающие подсказки.
* **Утилиты администратора:** Команда `/locatevein <тип_жилы>` корректно рассчитывает параметры биома при поиске.
* **Локализация:** Полный перевод на английский (`en_us`) и русский (`ru_ru`) языки.

### Кастомизация
Мод Create: Strata полностью управляется данными (Data-Driven). Чтобы добавить новую жилу через датапак, необходимо создать три файла:

**1. Configured Feature (`data/ваш_namespace/worldgen/configured_feature/my_new_vein.json`):**
Задаёт форму, редкость, климат и внутренние механики руды.
```json
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
```
*Примечание:* Параметр `color` задаёт цвет точки на Механическом радаре. `display_name` используется как ключ перевода (добавляется через ресурспак) или выводится как готовый текст, если перевод не найден.

**2. Placed Feature (`data/ваш_namespace/worldgen/placed_feature/my_new_vein_placed.json`):**
Требуется движку ванильного Minecraft для обработки процесса генерации.
```json
{
  "feature": "ваш_namespace:my_new_vein",
  "placement": [
    { "type": "minecraft:count", "count": 1 },
    { "type": "minecraft:in_square" },
    { "type": "minecraft:biome" }
  ]
}
```

**3. Biome Modifier (`data/ваш_namespace/neoforge/biome_modifier/add_my_new_vein.json`):**
Указывает загрузчику NeoForge добавить вашу жилу в этап генерации мира.
```json
{
  "type": "neoforge:add_features",
  "biomes": "#minecraft:is_overworld",
  "features": "ваш_namespace:my_new_vein_placed",
  "step": "underground_ores"
}
```

### Зависимости
* NeoForge `1.21.1`
* Create
