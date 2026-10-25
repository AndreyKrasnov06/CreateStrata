<h1 align="center">Create: Strata</h1>

<p align="center">
  <a href="#-english">🇬🇧 English</a> | <a href="#-русский">🇷🇺 Русский</a>
</p>

---

<a name="-english"></a>
## 🇬🇧 English

Create: Strata is an add-on for the Create mod that alters ore generation mechanics. Standard small ore pockets are replaced with large, rare geological deposits distributed according to world climate parameters and deterministic noise grids.

### Features

* **Geological Formations:** Ores generate as rare deposits using three geometric types:
  * **Pipes (`pipe_vein`):** Vertical or inclined columns with optional tapering (`conicity`).
  * **Discs (`disc_vein`):** Horizontal or tilted layered beds.
  * **Clusters (`cluster_vein`):** Irregular volumetric formations generated using metaball density fields.
* **Climate & Worldgen Filtering:** Vein placement is constrained by biome temperature, moisture, and height bounds. Veins above the local surface level are automatically filtered out.
* **Deterministic Distribution:** Grid cells use a seed hash derived from the world seed, feature type, and configuration parameters (`display_name`, `radius`, `height`, `rarity`) to prevent identical placements across different vein types.
* **Mechanical Radar:** A kinetic block that scans surrounding chunks for ore deposits:
  * Kinetic impact: `16 SU` per RPM.
  * Minimum operational speed: `32 RPM` (below 32 RPM the radar remains idle).
  * Scan radius: `floor(RPM / 16)` chunks (e.g. 32 RPM = 2 chunks, 64 RPM = 4 chunks, 128 RPM = 8 chunks, 256 RPM = 16 chunks).
  * Interface: Cardinal orientation indicator (North marker), chunk grid, deposit coordinates, and color-coded markers with tooltips.
* **Diagnostic Command:** `/locatevein <configured_feature_id>` searches up to 100 grid cells for deposits matching the feature's climate and height criteria (requires permission level 2).
* **Localization:** English (`en_us`) and Russian (`ru_ru`).

---

### Configuration & Data Pack Integration

All veins are defined via data packs. Creating a custom vein requires three components:

#### 1. Configured Feature (`data/<namespace>/worldgen/configured_feature/<name>.json`)

Choose one of three feature types: `createstrata:pipe_vein`, `createstrata:disc_vein`, or `createstrata:cluster_vein`. All types share the same configuration structure.

##### Example A: Pipe Vein (Vertical Column with Conicity)
```json
{
  "type": "createstrata:pipe_vein",
  "config": {
    "radius": 16,
    "height": 70,
    "density": 0.40,
    "rarity": 1500,
    "max_tilt": 20.0,
    "conicity": 0.5,
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
      },
      {
        "target": { "predicate_type": "minecraft:tag_match", "tag": "minecraft:deepslate_ore_replaceables" },
        "state": { "Name": "minecraft:deepslate_copper_ore" },
        "weight": 90.0
      },
      {
        "target": { "predicate_type": "minecraft:tag_match", "tag": "minecraft:stone_ore_replaceables" },
        "state": { "Name": "minecraft:raw_copper_block" },
        "weight": 10.0
      }
    ]
  }
}
```

##### Example B: Disc Vein (Layered Bed)
```json
{
  "type": "createstrata:disc_vein",
  "config": {
    "radius": 24,
    "height": 5,
    "density": 0.60,
    "rarity": 2000,
    "max_tilt": 8.0,
    "conicity": 0.0,
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

##### Example C: Cluster Vein (Metaball Blob)
```json
{
  "type": "createstrata:cluster_vein",
  "config": {
    "radius": 14,
    "height": 14,
    "density": 0.25,
    "rarity": 3000,
    "max_tilt": 0.0,
    "conicity": 0.0,
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
        "weight": 95.0
      },
      {
        "target": { "predicate_type": "minecraft:tag_match", "tag": "minecraft:deepslate_ore_replaceables" },
        "state": { "Name": "minecraft:diamond_block" },
        "weight": 5.0
      }
    ]
  }
}
```

##### Configuration Parameters

| Parameter | Type | Default | Description |
|---|---|---|---|
| `radius` | Integer | *(required)* | Horizontal radius of the formation in blocks. |
| `height` | Integer | *(required)* | Vertical half-extent in blocks (`[-height, height]` along the Y axis relative to center). |
| `density` | Float | *(required)* | Probability (0.0 to 1.0) of replacing target blocks within the vein volume. |
| `targets` | Array | *(required)* | List of target replacement rules. Each entry contains `target` (rule test), `state` (resulting block state), and optional `weight` (default `1.0`). |
| `rarity` | Integer | `40` | Inverse spawn probability per grid cell. Higher values make the deposit rarer. |
| `max_tilt` | Float | `0.0` | Maximum tilt angle in degrees (0.0 to 90.0) around pitch and roll axes. |
| `conicity` | Float | `0.0` | Vertical taper factor for `pipe_vein` (clamped between `-1.0` and `1.0`). Values `> 0` widen toward the top (e.g. kimberlite pipes), values `< 0` widen toward the base, `0.0` is a cylinder. Ignored by other shapes. |
| `min_y` | Float | `-1.0` | Normalized minimum center elevation (`-1.0` = minimum world height, `1.0` = build height limit). |
| `max_y` | Float | `1.0` | Normalized maximum center elevation. |
| `min_temp` | Float | `-2.0` | Minimum biome temperature allowed for placement. |
| `max_temp` | Float | `3.0` | Maximum biome temperature allowed for placement. |
| `min_moisture` | Float | `0.0` | Minimum biome downfall (moisture) allowed for placement. |
| `max_moisture` | Float | `1.0` | Maximum biome downfall (moisture) allowed for placement. |
| `color` | String | `"#55FF55"` | Hex color code (`#RRGGBB` or `#AARRGGBB`) used to render the deposit marker on the radar screen. |
| `display_name` | String | `"Жила"` | Localization key or text displayed in tooltips and `/locatevein`. Also serves as a component of the cell placement salt hash. |

> **Note on Salt & Placement:** Grid cell positioning is calculated from a hash of `display_name`, `radius`, `height`, and `rarity`. If two deposits share the exact same dimensions, rarity, and display name, their cell offsets will be identical. Using different names or dimensions ensures independent spatial distributions.

---

#### 2. Placed Feature (`data/<namespace>/worldgen/placed_feature/<name>_placed.json`)

Registers the configured feature for generation within the world:

```json
{
  "feature": "your_namespace:my_vein",
  "placement": [
    { "type": "minecraft:count", "count": 1 },
    { "type": "minecraft:in_square" },
    { "type": "minecraft:biome" }
  ]
}
```

*Create: Strata also registers optional placement helpers:*
* `createstrata:normalized` — Height provider sampling between normalized values `min` and `max` (`-1.0` to `1.0`).
* `createstrata:temperature_filter` — Placement modifier filtering by biome temperature `min` and `max`.
* `createstrata:moisture_filter` — Placement modifier filtering by biome downfall `min` and `max`.

---

#### 3. Biome Modifier (`data/<namespace>/neoforge/biome_modifier/add_<name>.json`)

Injects the placed feature into the selected biomes during worldgen:

```json
{
  "type": "neoforge:add_features",
  "biomes": "#minecraft:is_overworld",
  "features": "your_namespace:my_vein_placed",
  "step": "underground_ores"
}
```

---

### Requirements

* Minecraft `1.21.1`
* NeoForge `21.1.x`
* Create `6.0.x`

---

<a name="-русский"></a>
## 🇷🇺 Русский

Create: Strata — дополнение для мода Create, изменяющее механику генерации руд. Стандартные мелкие скопления руды заменяются крупными редкими геологическими телами, распределяемыми по климатическим условиям и математической сетке шумов.

### Функционал

* **Геологические тела:** Руды генерируются в виде трех типов образований:
  * **Трубы (`pipe_vein`):** Вертикальные или наклонные столбообразные тела с поддержкой конусности (`conicity`).
  * **Диски (`disc_vein`):** Горизонтальные или наклонные пластообразные залежи.
  * **Кластеры (`cluster_vein`):** Объемные скопления неправильной формы, рассчитываемые через метаболы (потенциальные поля плотности).
* **Климатическая фильтрация:** Генерация жил привязана к температуре и влажности биомов, а также к диапазону высот. Рудные тела, чьи центры оказываются выше поверхности рельефа, исключаются из генерации.
* **Детерминированное распределение:** Позиция жилы в ячейке сетки вычисляется на основе сида мира, типа структуры и хэша конфигурации (`display_name`, `radius`, `height`, `rarity`), что предотвращает совпадение координат разных жил.
* **Механический радар:** Кинетический блок для обнаружения залежей руды в радиусе действия:
  * Нагрузка на сеть (Stress Impact): `16 SU` на 1 RPM.
  * Минимальная рабочая скорость: `32 RPM` (при скорости ниже 32 RPM радар неактивен и сообщает о нехватке мощности).
  * Радиус сканирования: `floor(RPM / 16)` чанков (32 RPM = 2 чанка, 64 RPM = 4 чанка, 128 RPM = 8 чанков, 256 RPM = 16 чанков).
  * Интерфейс: Указатель севера (метка N), сетка чанков, координаты залежей и цветные маркеры с всплывающими подсказками.
* **Команда поиска:** `/locatevein <configured_feature_id>` выполняет поиск ближайшей жилы в радиусе до 100 ячеек сетки с проверкой условий климата и высоты поверхности (требуется уровень доступа оператора 2).
* **Локализация:** Русский (`ru_ru`) и английский (`en_us`) языки.

---

### Настройка через датапаки

Все жилы настраиваются через стандартные датапаки. Для создания новой жилы необходимы три файла:

#### 1. Configured Feature (`data/<namespace>/worldgen/configured_feature/<имя>.json`)

Доступны три идентификатора типа: `createstrata:pipe_vein`, `createstrata:disc_vein` и `createstrata:cluster_vein`. Все они используют одинаковую структуру конфигурации.

##### Пример А: Pipe Vein (Труба с конусностью)
```json
{
  "type": "createstrata:pipe_vein",
  "config": {
    "radius": 16,
    "height": 70,
    "density": 0.40,
    "rarity": 1500,
    "max_tilt": 20.0,
    "conicity": 0.5,
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
      },
      {
        "target": { "predicate_type": "minecraft:tag_match", "tag": "minecraft:deepslate_ore_replaceables" },
        "state": { "Name": "minecraft:deepslate_copper_ore" },
        "weight": 90.0
      },
      {
        "target": { "predicate_type": "minecraft:tag_match", "tag": "minecraft:stone_ore_replaceables" },
        "state": { "Name": "minecraft:raw_copper_block" },
        "weight": 10.0
      }
    ]
  }
}
```

##### Пример Б: Disc Vein (Пластовая залежь)
```json
{
  "type": "createstrata:disc_vein",
  "config": {
    "radius": 24,
    "height": 5,
    "density": 0.60,
    "rarity": 2000,
    "max_tilt": 8.0,
    "conicity": 0.0,
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

##### Пример В: Cluster Vein (Кластерное скопление)
```json
{
  "type": "createstrata:cluster_vein",
  "config": {
    "radius": 14,
    "height": 14,
    "density": 0.25,
    "rarity": 3000,
    "max_tilt": 0.0,
    "conicity": 0.0,
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
        "weight": 95.0
      },
      {
        "target": { "predicate_type": "minecraft:tag_match", "tag": "minecraft:deepslate_ore_replaceables" },
        "state": { "Name": "minecraft:diamond_block" },
        "weight": 5.0
      }
    ]
  }
}
```

##### Описание параметров конфигурации

| Параметр | Тип | По умолчанию | Описание |
|---|---|---|---|
| `radius` | Integer | *(обязательный)* | Горизонтальный радиус рудного тела в блоках. |
| `height` | Integer | *(обязательный)* | Вертикальный полуохват формы в блоках (`[-height, height]` по оси Y относительно центра). |
| `density` | Float | *(обязательный)* | Плотность заполнения (от `0.0` до `1.0`), определяющая вероятность замены целевого блока на руду. |
| `targets` | Array | *(обязательный)* | Список правил замены блоков. Каждый элемент содержит `target` (условие проверки блока), `state` (результирующий блок) и `weight` (относительный вес, по умолчанию `1.0`). |
| `rarity` | Integer | `40` | Обратная вероятность генерации жилы в ячейке сетки. Чем выше значение, тем реже появляется жила. |
| `max_tilt` | Float | `0.0` | Максимальный угол наклона формы в градусах (от `0.0` до `90.0`) по осям pitch и roll. |
| `conicity` | Float | `0.0` | Коэффициент конусности для `pipe_vein` (от `-1.0` до `1.0`). Значения `> 0` расширяют трубу кверху (кимберлитовые трубки), значения `< 0` расширяют к основанию, `0.0` — прямой цилиндр. Другими типами жил игнорируется. |
| `min_y` | Float | `-1.0` | Нормализованная минимальная высота центра жилы (`-1.0` — нижняя граница мира, `1.0` — верхний лимит строительства). |
| `max_y` | Float | `1.0` | Нормализованная максимальная высота центра жилы. |
| `min_temp` | Float | `-2.0` | Минимальная температура биома для генерации. |
| `max_temp` | Float | `3.0` | Максимальная температура биома для генерации. |
| `min_moisture` | Float | `0.0` | Минимальная влажность биома для генерации. |
| `max_moisture` | Float | `1.0` | Максимальная влажность биома для генерации. |
| `color` | String | `"#55FF55"` | Шестнадцатеричный код цвета (`#RRGGBB` или `#AARRGGBB`) метки на экране механического радара. |
| `display_name` | String | `"Жила"` | Ключ локализации или текст названия жилы в интерфейсе, подсказках и выводе `/locatevein`. Также участвует в формировании соли генерации. |

> **Расчет соли и координат:** Координаты жилы внутри ячейки рассчитываются на основе хэша из параметров `display_name`, `radius`, `height` и `rarity`. При совпадении всех этих параметров у двух разных жил их смещения внутри ячейки совпадут. Использование уникальных имен или размеров гарантирует независимое размещение.

---

#### 2. Placed Feature (`data/<namespace>/worldgen/placed_feature/<имя>_placed.json`)

Регистрирует настроенную жилу для размещения в генераторе мира:

```json
{
  "feature": "your_namespace:my_vein",
  "placement": [
    { "type": "minecraft:count", "count": 1 },
    { "type": "minecraft:in_square" },
    { "type": "minecraft:biome" }
  ]
}
```

*Дополнительные инструменты размещения от Create: Strata:*
* `createstrata:normalized` — Провайдер высоты между нормализованными границами `min` и `max` (от `-1.0` до `1.0`).
* `createstrata:temperature_filter` — Модификатор фильтрации биомов по температуре (`min`, `max`).
* `createstrata:moisture_filter` — Модификатор фильтрации биомов по влажности (`min`, `max`).

---

#### 3. Biome Modifier (`data/<namespace>/neoforge/biome_modifier/add_<имя>.json`)

Добавляет жилу в выбранные биомы на этапе генерации подземных руд:

```json
{
  "type": "neoforge:add_features",
  "biomes": "#minecraft:is_overworld",
  "features": "your_namespace:my_vein_placed",
  "step": "underground_ores"
}
```

---

### Зависимости

* Minecraft `1.21.1`
* NeoForge `21.1.x`
* Create `6.0.x`
