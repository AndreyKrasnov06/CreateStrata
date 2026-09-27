<h1 align="center">Create: Strata</h1>

<p align="center">
  <a href="#-english">🇬🇧 English</a> | <a href="#-русский">🇷🇺 Русский</a>
</p>

<a name="-english"></a>
## 🇬🇧 English
Create: Strata is an add-on for the Create mod that overhauls ore generation to mimic TerraFirmaCraft mechanics. Instead of small, scattered clusters, ores generate in large geological veins across the world.

### Features
* **Geological Veins:** Ores generate in large, rare formations (Pipes, Discs, and Clusters) containing substantial amounts of resources.
* **Climate-Based Generation:** Vein placement is mathematically driven by world noise, biome temperature, and moisture, preventing overlaps and adding exploration value. Multi-dimensional hashing automatically isolates different vein types using their configuration properties.
* **Mechanical Radar:** A kinetic block (`16 SU` at `1 RPM`) that scans the chunk matrix to help locate ore deposits. The display features cardinal scaling, real-world coordinates, and dynamic tooltips.
* **Admin Utilities:** `/locatevein <vein_type>` correctly respects noise biome conditions.
* **Localization:** Fully translated into English (`en_us`) and Russian (`ru_ru`).

### Customization
Create: Strata is completely data-driven. Veins can be added or modified via datapacks. To add a new vein, you must create three files:

**1. Configured Feature (`data/your_namespace/worldgen/configured_feature/...`):**
There are three generation shapes available: **Pipes** (vertical columns), **Discs** (flat horizontal sheets), and **Clusters** (scattered spherical blobs). They all share the same configuration structure but use different `type` identifiers.

**Example A: Pipe Vein (Vertical Pillars)**
```json
{
  "type": "createstrata:pipe_vein",
  "config": {
    "radius": 16,
    "height": 70,
    "density": 0.40,
    "rarity": 1500,
    "max_tilt": 20.0,
    "min_y": -0.90,
    "max_y": -0.10,
    "min_temp": -1.0,
    "max_temp": 1.0,
    "min_moisture": -1.0,
    "max_moisture": 1.0,
    "color": "#B87333",
    "display_name": "mypack.vein.copper",
    "targets": [
      {
        "target": { "predicate_type": "minecraft:tag_match", "tag": "minecraft:stone_ore_replaceables" },
        "state": { "Name": "minecraft:copper_ore" },
        "weight": 90.0
      }
    ]
  }
}
```

**Example B: Disc Vein (Flat Horizontal Sheets)**
```json
{
  "type": "createstrata:disc_vein",
  "config": {
    "radius": 24,
    "height": 5,
    "density": 0.60,
    "rarity": 2000,
    "max_tilt": 8.0,
    "min_y": 0.0,
    "max_y": 0.50,
    "min_temp": 0.5,
    "max_temp": 2.5,
    "min_moisture": 0.2,
    "max_moisture": 1.0,
    "color": "#1A1A1A",
    "display_name": "mypack.vein.coal",
    "targets": [
      {
        "target": { "predicate_type": "minecraft:tag_match", "tag": "minecraft:stone_ore_replaceables" },
        "state": { "Name": "minecraft:coal_ore" },
        "weight": 100.0
      }
    ]
  }
}
```

**Example C: Cluster Vein (Scattered Spheres)**
```json
{
  "type": "createstrata:cluster_vein",
  "config": {
    "radius": 14,
    "height": 14,
    "density": 0.25,
    "rarity": 3000,
    "max_tilt": 0.0,
    "min_y": -1.0,
    "max_y": -0.60,
    "min_temp": -1.0,
    "max_temp": 2.5,
    "min_moisture": 0.0,
    "max_moisture": 1.0,
    "color": "#00FFFF",
    "display_name": "mypack.vein.diamond",
    "targets": [
      {
        "target": { "predicate_type": "minecraft:tag_match", "tag": "minecraft:deepslate_ore_replaceables" },
        "state": { "Name": "minecraft:diamond_ore" },
        "weight": 100.0
      }
    ]
  }
}
```

**Configuration Parameters Explained:**
* `radius`: Explains horizontal bounds (in blocks).
* `height`: Vertical extent/thickness of the vein shape in blocks.
* `density`: The percentage factor of blocks generated inside the bounds (e.g. `0.40` means roughly 40% of targets are replaced). 
* `rarity`: Inverse probability threshold. Generates once per X cells on the matrix grid.
* `max_tilt`: The maximum random tilt angle (in degrees, 0 to 90) the vein can rotate/slant. Allows veins to generate diagonally within the earth!
* `min_y` & `max_y`: Normalized generation heights (`-1.0` is the absolute bottom of the world, `1.0` is the max build height).
* `min_temp` & `max_temp`: Climate temperature restrictions (from approx `-1.0` to `2.5`).
* `min_moisture` & `max_moisture`: Climate humidity/precipitation restrictions (from `0.0` to `1.0`).
* `targets`: A weighted list array of blocks the vein is allowed to overwrite. Each object must contain `target` (the block or tag), `state` (resulting ore), and `weight` (ratio for this block if multiple match).
* `color`: Hex color code (e.g., `#00FFFF`) representing how the vein appears on the Mechanical Radar limits.
* `display_name`: A language mapping key (translated via Resource Pack) or raw text for fallback display in tooltips and `/locatevein`. **(Note: The `display_name` field is also used as a random seed (salt) for generating ores. Do not use the exact same name and geometrical dimensions for different veins to avoid collisions and overlaps).**

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
* **Генерация по климату:** Расположение жил математически привязано к шумам мира, температуре и влажности биома. Инновационное многомерное хэширование параметров гарантирует, что жилы никогда не будут накладываться друг на друга, стимулируя исследование мира.
* **Механический радар:** Кинетический блок (потребляет `16 SU` при `1 RPM`), который сканирует сетку чанков для поиска залежей руды. Интерфейс поддерживает масштабирование, привязку к координатам и всплывающие подсказки.
* **Утилиты администратора:** Команда `/locatevein <тип_жилы>` корректно рассчитывает параметры биома при поиске.
* **Локализация:** Полный перевод на английский (`en_us`) и русский (`ru_ru`) языки.

### Кастомизация
Мод Create: Strata полностью управляется данными (Data-Driven). Чтобы добавить новую жилу через датапак, необходимо создать три файла:

**1. Configured Feature (`data/ваш_namespace/worldgen/configured_feature/...`):**
Существует три формы генерации: **Трубы (Pipe)** (вертикальные столбы), **Диски (Disc)** (горизонтальные плоские слои) и **Кластеры (Cluster)** (разбросанные сферические скопления). Все они используют единый формат параметров (один на всех), меняется лишь `type`.

**Пример А: Pipe Vein (Вертикальные столбы)**
```json
{
  "type": "createstrata:pipe_vein",
  "config": {
    "radius": 16,
    "height": 70,
    "density": 0.40,
    "rarity": 1500,
    "max_tilt": 20.0,
    "min_y": -0.90,
    "max_y": -0.10,
    "min_temp": -1.0,
    "max_temp": 1.0,
    "min_moisture": -1.0,
    "max_moisture": 1.0,
    "color": "#B87333",
    "display_name": "mypack.vein.copper",
    "targets": [
      {
        "target": { "predicate_type": "minecraft:tag_match", "tag": "minecraft:stone_ore_replaceables" },
        "state": { "Name": "minecraft:copper_ore" },
        "weight": 90.0
      }
    ]
  }
}
```

**Пример Б: Disc Vein (Плоские пласты)**
```json
{
  "type": "createstrata:disc_vein",
  "config": {
    "radius": 24,
    "height": 5,
    "density": 0.60,
    "rarity": 2000,
    "max_tilt": 8.0,
    "min_y": 0.0,
    "max_y": 0.50,
    "min_temp": 0.5,
    "max_temp": 2.5,
    "min_moisture": 0.2,
    "max_moisture": 1.0,
    "color": "#1A1A1A",
    "display_name": "mypack.vein.coal",
    "targets": [
      {
        "target": { "predicate_type": "minecraft:tag_match", "tag": "minecraft:stone_ore_replaceables" },
        "state": { "Name": "minecraft:coal_ore" },
        "weight": 100.0
      }
    ]
  }
}
```

**Пример В: Cluster Vein (Сферические скопления)**
```json
{
  "type": "createstrata:cluster_vein",
  "config": {
    "radius": 14,
    "height": 14,
    "density": 0.25,
    "rarity": 3000,
    "max_tilt": 0.0,
    "min_y": -1.0,
    "max_y": -0.60,
    "min_temp": -1.0,
    "max_temp": 2.5,
    "min_moisture": 0.0,
    "max_moisture": 1.0,
    "color": "#00FFFF",
    "display_name": "mypack.vein.diamond",
    "targets": [
      {
        "target": { "predicate_type": "minecraft:tag_match", "tag": "minecraft:deepslate_ore_replaceables" },
        "state": { "Name": "minecraft:diamond_ore" },
        "weight": 100.0
      }
    ]
  }
}
```

**Подробное описание параметров:**
* `radius`: Горизонтальный предельный размер / радиус жилы (в блоках).
* `height`: Вертикальная толщина (высота) формы жилы в блоках.
* `density`: Плотность руды (в процентах) внутри этих границ (например `0.40` означает, что заменится ~40% камня).
* `rarity`: Редкость генерации (шанс 1 к X на ячейку в математической матрице мира).
* `max_tilt`: Максимальный случайный угол наклона формы (в градусах от 0 до 90). Позволяет жилам генерироваться диагонально или наискосок в толще земли!
* `min_y` и `max_y`: Нормализованные высоты мира, где `-1.0` - это абсолютный низ коренной породы (бедрока), а `1.0` - максимально возможная высота мира постройки потолка.
* `min_temp` и `max_temp`: Температурные ограничения климата (от `-1.0` до `2.5`).
* `min_moisture` и `max_moisture`: Ограничения влажности и осадков биома (от `0.0` до `1.0`).
* `targets`: Взвешенный список (массив) блоков, в которых может заспавниться руда. Каждое условие содержит `target` (блок для замены или тег, например `minecraft:stone_ore_replaceables`), `state` (блок вашей руды) и `weight` (шанс, если совпало несколько блоков).
* `color`: HEX-цвет (например, `#00FFFF`), которым жила нарисуется в Механическом Радаре.
* `display_name`: Ключ локализации (добавляется через ресурспак) или готовый текст в качестве заглушки интерфейса и команды `/locatevein`. **(Важно: Поле `display_name` также используется как случайное число (соль) для генерации руд. Не используйте одинаковое название и геометрические размеры во избежание коллизий).**

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