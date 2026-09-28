package com.AndreyKrasnov.createstrata;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.biome.Biome;

public class LocateVeinCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("locatevein")
                .requires(source -> source.hasPermission(2))
                .then(Commands.argument("feature", ResourceLocationArgument.id())
                        .suggests((context, builder) -> {
                            var registry = context.getSource().getServer().registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE);
                            return SharedSuggestionProvider.suggestResource(
                                    registry.keySet().stream()
                                            .filter(id -> {
                                                var holder = registry.get(id);
                                                return holder != null && holder.config() instanceof CustomVeinConfig;
                                            }),
                                    builder
                            );
                        })
                        .executes(context -> execute(context.getSource(), ResourceLocationArgument.getId(context, "feature"))))
        );
    }

    private static int execute(CommandSourceStack source, ResourceLocation id) {
        var registry = source.getServer().registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE);
        var holder = registry.get(id);
        
        if (holder == null) {
            source.sendFailure(Component.translatable("commands.createstrata.locatevein.unknown_feature", id.toString()));
            return 0;
        }
        
        if (!(holder.config() instanceof CustomVeinConfig config)) {
            source.sendFailure(Component.translatable("commands.createstrata.locatevein.not_custom_vein", id.toString()));
            return 0;
        }

        int radius = config.radius();
        int height = config.height();
        int rarity = config.rarity();
        
        int maxDim = Math.max(radius, height);
        int maxBound = (int) Math.ceil(maxDim * 1.5);
        int cellSpanChunks = Math.max(2, (maxBound / 16) + 2);
        
        long worldSeed = source.getLevel().getSeed();
        
        BlockPos playerPos = BlockPos.containing(source.getPosition());
        int playerChunkX = playerPos.getX() >> 4;
        int playerChunkZ = playerPos.getZ() >> 4;
        
        int startCellX = Math.floorDiv(playerChunkX, cellSpanChunks);
        int startCellZ = Math.floorDiv(playerChunkZ, cellSpanChunks);

        long salt1 = 0, salt2 = 0, salt3 = 0;
        if (holder.feature() instanceof ClusterVeinFeature) {
            salt1 = 812371813L; salt2 = 912381231L; salt3 = config.getSalt();
        } else if (holder.feature() instanceof DiscVeinFeature) {
            salt1 = 341873128712L; salt2 = 132897987541L; salt3 = config.getSalt();
        } else if (holder.feature() instanceof PipeVeinFeature) {
            salt1 = 123987123L; salt2 = 987123987L; salt3 = config.getSalt();
        } else {
            source.sendFailure(Component.translatable("commands.createstrata.locatevein.unsupported_feature"));
            return 0;
        }

        int maxSearchRadius = 100; 
        int chunksInCell = cellSpanChunks * cellSpanChunks;
        float cellChance = Math.min(1.0f, (float) chunksInCell / rarity);
        
        var serverLevel = source.getLevel();
        var randomState = serverLevel.getChunkSource().randomState();
        var biomeSource = serverLevel.getChunkSource().getGenerator().getBiomeSource();

        for (int r = 0; r <= maxSearchRadius; r++) {
            for (int dx = -r; dx <= r; dx++) {
                for (int dz = -r; dz <= r; dz++) {
                    if (Math.abs(dx) == r || Math.abs(dz) == r) {
                        int cellX = startCellX + dx;
                        int cellZ = startCellZ + dz;
                        
                        RandomSource random = RandomSource.create(worldSeed ^ (cellX * salt1) ^ (cellZ * salt2) ^ salt3);
                        
                        if (random.nextFloat() <= cellChance) {
                            int centerX = (cellX * cellSpanChunks * 16) + random.nextInt(cellSpanChunks * 16);
                            int centerZ = (cellZ * cellSpanChunks * 16) + random.nextInt(cellSpanChunks * 16);
                            
                            int actualMinY = Math.max(serverLevel.getMinBuildHeight() + maxBound, config.getAbsoluteMinY(serverLevel));
                            int actualMaxY = Math.min(serverLevel.getMaxBuildHeight() - maxBound, config.getAbsoluteMaxY(serverLevel));
                            if (actualMinY > actualMaxY) actualMinY = actualMaxY;
                            int centerY = random.nextIntBetweenInclusive(actualMinY, actualMaxY);
                            
                            Biome biome = biomeSource.getNoiseBiome(centerX >> 2, 64 >> 2, centerZ >> 2, randomState.sampler()).value();
                            
                            float centerTemp = biome.getBaseTemperature();
                            float centerMoisture = biome.getModifiedClimateSettings().downfall();
                            
                            boolean tempPass = centerTemp >= config.minTemp() && centerTemp <= config.maxTemp();
                            boolean moistPass = centerMoisture >= config.minMoisture() && centerMoisture <= config.maxMoisture();
                            
                            if (tempPass && moistPass) {
                                int surfaceY = serverLevel.getChunkSource().getGenerator().getBaseHeight(
                                    centerX, centerZ, 
                                    Heightmap.Types.OCEAN_FLOOR_WG, 
                                    serverLevel, 
                                    randomState
                                );
                                
                                if (centerY > surfaceY) {
                                    continue;
                                }
                                
                                int distBlocks = (int) Math.sqrt(Math.pow(centerX - playerPos.getX(), 2) + Math.pow(centerZ - playerPos.getZ(), 2));
                                
                                source.sendSuccess(() -> Component.translatable(
                                    "commands.createstrata.locatevein.found", Component.translatable(config.displayName()), centerX, centerY, centerZ, distBlocks
                                ), false);
                                return 1;
                            }
                        }
                    }
                }
            }
        }
        
        source.sendFailure(Component.translatable("commands.createstrata.locatevein.not_found"));
        return 0;
    }
}
