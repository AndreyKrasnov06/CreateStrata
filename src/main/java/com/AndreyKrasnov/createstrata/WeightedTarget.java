package com.AndreyKrasnov.createstrata;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

public record WeightedTarget(RuleTest target, BlockState state, float weight) {
    public static final Codec<WeightedTarget> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    RuleTest.CODEC.fieldOf("target").forGetter(WeightedTarget::target),
                    BlockState.CODEC.fieldOf("state").forGetter(WeightedTarget::state),
                    Codec.FLOAT.optionalFieldOf("weight", 1.0f).forGetter(WeightedTarget::weight)
            ).apply(instance, WeightedTarget::new));
}


