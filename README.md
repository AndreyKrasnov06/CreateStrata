# Create: Strata
A geological add-on for the **Create** mod that completely overhauls ore generation to mimic TerraFirmaCraft mechanics. Gone are the days of finding scattered tiny clusters of ores — prepare to hunt down massive, rare, realistically generated geological strata and veins.

## Features
- **Massive Geological Formations:** Ores now generate in enormous, biome-dependent veins (Pipes, Discs, and Clusters) containing thousands of blocks, rather than small scattered pockets.
- **Data-Driven Ore System:** Overworld and Nether generation is thoroughly rebuilt using noise samplers, precise climates, and custom random salts to guarantee realistic, non-overlapping strata generation.
- **Mechanical Radar:** A powerful new block powered by kinetics (requires 16 SU per RPM) that mathematically scans the chunk matrix to help prospectors pinpoint large ore deposits. Features dynamic cardinal UI scaling and tooltips.
- **Admin Command:** /locatevein <vein_type> correctly respects noise biome humidity and precipitation conditions.
- **i18n Localization:** Fully translated into en_us and u_ru. 

## How to Prospect
Finding ores works fundamentally differently from Vanilla Minecraft:
1. **Explore Climates:** Veins generate based on temperature and moisture (e.g. some ores only spawn in arid, dry peaks, while others need deep aquatic environments).
2. **Setup the Radar:** Build a Mechanical Radar. Feed it kinetic energy. The higher the RPM, the larger the chunk grid the radar can physically scan.
3. **Scan Grid:** Watch the display map to see color-coded dots representing exact mathematical vein generation centers. The map locks to North and provides coordinate tooltips.

## Modpack Creators: How to Add Custom Veins
Create: Strata is 100% Data-Driven! You do not need to write Java or modify the Mod JAR to add hundreds of custom veins.

### 1. Add a Custom Vein (Datapack)
Create a normal JSON file in your datapack under data/<your_namespace>/worldgen/configured_feature/my_new_vein.json.

Specify the feature type (	fcores:pipe_vein, 	fcores:disc_vein, or 	fcores:cluster_vein) and add your configuration parameters:

``json
{
  "type": "tfcores:pipe_vein",
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

### 2. Add Localization (Translations)
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

## Dependencies
- NeoForge 1.21.1
- [Create](https://modrinth.com/mod/create)
- Flywheel
- Ponder

## License
All Rights Reserved.
