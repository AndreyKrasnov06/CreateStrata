package com.AndreyKrasnov.createstrata;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

public class DataGenerator {
    public static void main(String[] args) throws IOException {
        System.out.println("Запуск генерации кастомных жил...");

        // Генерируем жилы (только нужные параметры: radius, height, density)
        createOre("huge_coal", "cluster_vein", 12, 4, 0.25f, "minecraft:coal_ore", "minecraft:deepslate_coal_ore", 30, 0.2f, 0.9f, -0.1f, 0.8f);
        createOre("massive_iron", "cluster_vein", 11, 11, 0.25f, "minecraft:iron_ore", "minecraft:deepslate_iron_ore", 40, -1.0f, 0.2f, -0.6f, 0.4f);
        createOre("copper_pipe", "pipe_vein", 6, 45, 0.20f, "minecraft:copper_ore", "minecraft:deepslate_copper_ore", 35, -2.0f, 2.0f, -0.3f, 0.7f);
        createOre("gold_pipe", "pipe_vein", 5, 30, 0.20f, "minecraft:gold_ore", "minecraft:deepslate_gold_ore", 50, 1.0f, 2.0f, -1.0f, -0.4f);
        createOre("lapis_cluster", "cluster_vein", 8, 6, 0.25f, "minecraft:lapis_ore", "minecraft:deepslate_lapis_ore", 45, -1.0f, 0.0f, -0.8f, -0.2f);
        createOre("redstone_sill", "disc_vein", 12, 2, 0.30f, "minecraft:redstone_ore", "minecraft:deepslate_redstone_ore", 40, 0.6f, 2.0f, -1.0f, -0.7f); // Изменил тип на disc_vein для пласта!
        createOre("emerald_cluster", "cluster_vein", 4, 4, 0.15f, "minecraft:emerald_ore", "minecraft:deepslate_emerald_ore", 60, -1.0f, -0.5f, 0.3f, 1.0f);
        createOre("diamond_kimberlite", "pipe_vein", 7, 55, 0.15f, "minecraft:diamond_ore", "minecraft:deepslate_diamond_ore", 70, -2.0f, 2.0f, -1.0f, -0.5f);

        System.out.println("✅ Кастомные жилы сгенерированы!");
    }

    private static void createOre(String name, String type, int radius, int height, float density, String oreBlock, String deepOreBlock, int rarity, float minTemp, float maxTemp, float minHeight, float maxHeight) throws IOException {
        String basePath = "src/main/resources/data/createstrata/";

        // Configured Feature (строго соответствует CustomVeinConfig.CODEC)
        String configured = String.format(Locale.US, "{\n  \"type\": \"createstrata:%s\",\n  \"config\": {\n    \"radius\": %d,\n    \"height\": %d,\n    \"density\": %.2f,\n    \"targets\": [\n      { \"target\": { \"predicate_type\": \"minecraft:tag_match\", \"tag\": \"minecraft:stone_ore_replaceables\" }, \"state\": { \"Name\": \"%s\" } },\n      { \"target\": { \"predicate_type\": \"minecraft:tag_match\", \"tag\": \"minecraft:deepslate_ore_replaceables\" }, \"state\": { \"Name\": \"%s\" } }\n    ]\n  }\n}",
                type, radius, height, density, oreBlock, deepOreBlock);
        createFile(basePath + "worldgen/configured_feature/" + name + ".json", configured);

        // Placed Feature
        String placed = String.format(Locale.US, "{\n  \"feature\": \"createstrata:%s\",\n  \"placement\": [\n    { \"type\": \"minecraft:rarity_filter\", \"chance\": %d },\n    { \"type\": \"minecraft:in_square\" },\n    { \"type\": \"createstrata:temperature_filter\", \"min\": %.2f, \"max\": %.2f },\n    { \"type\": \"minecraft:height_range\", \"height\": { \"type\": \"createstrata:normalized\", \"min\": %.2f, \"max\": %.2f } },\n    { \"type\": \"minecraft:biome\" }\n  ]\n}",
                name, rarity, minTemp, maxTemp, minHeight, maxHeight);
        createFile(basePath + "worldgen/placed_feature/" + name + "_placed.json", placed);

        // Biome Modifier
        String modifier = String.format(Locale.US, "{\n  \"type\": \"neoforge:add_features\",\n  \"biomes\": \"#minecraft:is_overworld\",\n  \"features\": \"createstrata:%s_placed\",\n  \"step\": \"underground_ores\"\n}", name);
        createFile(basePath + "neoforge/biome_modifier/add_" + name + ".json", modifier);
    }

    private static void createFile(String pathStr, String content) throws IOException {
        Path path = Paths.get(pathStr);
        Files.createDirectories(path.getParent());
        Files.writeString(path, content.trim());
    }
}

