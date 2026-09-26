package com.AndreyKrasnov.createstrata;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import java.util.stream.Stream;

public class TemperatureFilterModifier extends PlacementModifier {
    public static final MapCodec<TemperatureFilterModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("min").forGetter(m -> m.minTemp),
                    Codec.FLOAT.fieldOf("max").forGetter(m -> m.maxTemp)
            ).apply(instance, TemperatureFilterModifier::new));

    private final float minTemp;
    private final float maxTemp;

    public TemperatureFilterModifier(float min, float max) { this.minTemp = min; this.maxTemp = max; }

    @Override
    public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
        float temp = context.getLevel().getBiome(pos).value().getBaseTemperature();
        if (temp >= minTemp && temp <= maxTemp) return Stream.of(pos);
        return Stream.empty();
    }

    @Override
    public PlacementModifierType<?> type() { return ModRegistries.TEMPERATURE_FILTER.get(); }
}

