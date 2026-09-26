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

public class MoistureFilterModifier extends PlacementModifier {
    public static final MapCodec<MoistureFilterModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("min").forGetter(m -> m.minMoisture),
                    Codec.FLOAT.fieldOf("max").forGetter(m -> m.maxMoisture)
            ).apply(instance, MoistureFilterModifier::new));

    private final float minMoisture;
    private final float maxMoisture;

    public MoistureFilterModifier(float min, float max) { this.minMoisture = min; this.maxMoisture = max; }

    @Override
    public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
        float moisture = context.getLevel().getBiome(pos).value().getModifiedClimateSettings().downfall();
        if (moisture >= minMoisture && moisture <= maxMoisture) return Stream.of(pos);
        return Stream.empty();
    }

    @Override
    public PlacementModifierType<?> type() { return ModRegistries.MOISTURE_FILTER.get(); }
}

