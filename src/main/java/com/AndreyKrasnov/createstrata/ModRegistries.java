package com.AndreyKrasnov.createstrata;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.heightproviders.HeightProviderType;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModRegistries {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, CreateStrata.MOD_ID);
    public static final DeferredRegister<HeightProviderType<?>> HEIGHT_PROVIDERS = DeferredRegister.create(Registries.HEIGHT_PROVIDER_TYPE, CreateStrata.MOD_ID);
    public static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIERS = DeferredRegister.create(Registries.PLACEMENT_MODIFIER_TYPE, CreateStrata.MOD_ID);

    public static final Supplier<Feature<CustomVeinConfig>> PIPE_VEIN = FEATURES.register("pipe_vein", () -> new PipeVeinFeature(CustomVeinConfig.CODEC));
    public static final Supplier<Feature<CustomVeinConfig>> CLUSTER_VEIN = FEATURES.register("cluster_vein", () -> new ClusterVeinFeature(CustomVeinConfig.CODEC));
    public static final Supplier<Feature<CustomVeinConfig>> DISC_VEIN = FEATURES.register("disc_vein", () -> new DiscVeinFeature(CustomVeinConfig.CODEC));

    public static final Supplier<HeightProviderType<NormalizedHeightProvider>> NORMALIZED_HEIGHT = HEIGHT_PROVIDERS.register("normalized", () -> () -> NormalizedHeightProvider.CODEC);

    public static final Supplier<PlacementModifierType<TemperatureFilterModifier>> TEMPERATURE_FILTER = PLACEMENT_MODIFIERS.register("temperature_filter", () -> () -> TemperatureFilterModifier.CODEC);
    public static final Supplier<PlacementModifierType<MoistureFilterModifier>> MOISTURE_FILTER = PLACEMENT_MODIFIERS.register("moisture_filter", () -> () -> MoistureFilterModifier.CODEC);

    public static void register(IEventBus eventBus) {
        FEATURES.register(eventBus);
        HEIGHT_PROVIDERS.register(eventBus);
        PLACEMENT_MODIFIERS.register(eventBus);
    }
}

