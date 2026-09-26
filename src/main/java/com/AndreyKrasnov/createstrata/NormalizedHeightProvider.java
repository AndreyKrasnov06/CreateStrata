package com.AndreyKrasnov.createstrata;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.heightproviders.HeightProviderType;

public class NormalizedHeightProvider extends HeightProvider {
    public static final MapCodec<NormalizedHeightProvider> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("min").forGetter(p -> p.min),
                    Codec.FLOAT.fieldOf("max").forGetter(p -> p.max)
            ).apply(instance, NormalizedHeightProvider::new));

    private final float min;
    private final float max;

    public NormalizedHeightProvider(float min, float max) {
        this.min = Math.max(-1.0f, Math.min(1.0f, min));
        this.max = Math.max(-1.0f, Math.min(1.0f, max));
    }

    @Override
    public int sample(RandomSource random, WorldGenerationContext context) {
        int minY = getAbsoluteY(this.min, context);
        int maxY = getAbsoluteY(this.max, context);
        if (minY >= maxY) return minY;
        return Mth.randomBetweenInclusive(random, minY, maxY);
    }

    private int getAbsoluteY(float normValue, WorldGenerationContext context) {
        int minGenY = context.getMinGenY();
        int maxGenY = minGenY + context.getGenDepth();
        int zeroY = 0;
        if (normValue >= 0) {
            return zeroY + Math.round(normValue * (maxGenY - zeroY));
        } else {
            return zeroY - Math.round(Math.abs(normValue) * (zeroY - minGenY));
        }
    }

    @Override
    public HeightProviderType<?> getType() { return ModRegistries.NORMALIZED_HEIGHT.get(); }
}

