package com.AndreyKrasnov.createstrata;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.List;

public record CustomVeinConfig(
        List<WeightedTarget> targets, 
        int radius, 
        int height, 
        float density, 
        int rarity,
        float maxTilt,
        float minY,
        float maxY,
        float minTemp,
        float maxTemp,
        float minMoisture,
        float maxMoisture,
        String colorHex,
        String displayName
) implements FeatureConfiguration {
    public static final Codec<CustomVeinConfig> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    WeightedTarget.CODEC.listOf().fieldOf("targets").forGetter(CustomVeinConfig::targets),
                    Codec.INT.fieldOf("radius").forGetter(CustomVeinConfig::radius),
                    Codec.INT.fieldOf("height").forGetter(CustomVeinConfig::height),
                    Codec.FLOAT.fieldOf("density").forGetter(CustomVeinConfig::density),
                    Codec.INT.optionalFieldOf("rarity", 40).forGetter(CustomVeinConfig::rarity),
                    Codec.FLOAT.optionalFieldOf("max_tilt", 0.0f).forGetter(CustomVeinConfig::maxTilt),
                    Codec.FLOAT.optionalFieldOf("min_y", -1.0f).forGetter(CustomVeinConfig::minY),
                    Codec.FLOAT.optionalFieldOf("max_y", 1.0f).forGetter(CustomVeinConfig::maxY),
                    Codec.FLOAT.optionalFieldOf("min_temp", -2.0f).forGetter(CustomVeinConfig::minTemp),
                    Codec.FLOAT.optionalFieldOf("max_temp", 3.0f).forGetter(CustomVeinConfig::maxTemp),
                    Codec.FLOAT.optionalFieldOf("min_moisture", 0.0f).forGetter(CustomVeinConfig::minMoisture),
                    Codec.FLOAT.optionalFieldOf("max_moisture", 1.0f).forGetter(CustomVeinConfig::maxMoisture),
                    Codec.STRING.optionalFieldOf("color", "#55FF55").forGetter(CustomVeinConfig::colorHex),
                    Codec.STRING.optionalFieldOf("display_name", "Жила").forGetter(CustomVeinConfig::displayName)
            ).apply(instance, CustomVeinConfig::new));
            
    public int getAbsoluteMinY(net.minecraft.world.level.LevelHeightAccessor level) {
        return convertNormalizedToAbsolute(minY, level);
    }
    
    public int getAbsoluteMaxY(net.minecraft.world.level.LevelHeightAccessor level) {
        return convertNormalizedToAbsolute(maxY, level);
    }

    public static int convertNormalizedToAbsolute(float normY, net.minecraft.world.level.LevelHeightAccessor level) {
        if (normY < 0) {
            return (int) (Math.abs(level.getMinBuildHeight()) * normY);
        } else {
            return (int) (level.getMaxBuildHeight() * normY);
        }
    }
    
    public int getParsedColor() {
        try {
            String hex = colorHex.replace("#", "");
            if (hex.length() == 6) {
                hex = "FF" + hex;
            }
            return (int) Long.parseLong(hex, 16);
        } catch (Exception e) {
            return 0xFF55FF55;
        }
    }
}

