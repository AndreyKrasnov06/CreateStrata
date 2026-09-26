package com.AndreyKrasnov.createstrata;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.BulkSectionAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.biome.Biome;

import java.util.ArrayList;
import java.util.List;

public class DiscVeinFeature extends Feature<CustomVeinConfig> {
    public DiscVeinFeature(Codec<CustomVeinConfig> codec) { super(codec); }

    @Override
    public boolean place(FeaturePlaceContext<CustomVeinConfig> context) {
        WorldGenLevel level = context.level();
        CustomVeinConfig config = context.config();
        
        ChunkPos chunkPos = new ChunkPos(context.origin());
        int currentChunkX = chunkPos.x;
        int currentChunkZ = chunkPos.z;
        long worldSeed = level.getSeed();
        
        int radius = config.radius();
        int thickness = config.height();
        float density = config.density();
        
        int maxDim = Math.max(radius, thickness);
        int maxBound = (int) Math.ceil(maxDim * 1.5); 
        int cellSpanChunks = Math.max(2, (maxBound / 16) + 2);
        
        boolean placedAny = false;

        try (BulkSectionAccess sectionAccess = new BulkSectionAccess(level)) {
            BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
            int cellXBase = Math.floorDiv(currentChunkX, cellSpanChunks);
            int cellZBase = Math.floorDiv(currentChunkZ, cellSpanChunks);

            for (int cellX = cellXBase - 1; cellX <= cellXBase + 1; cellX++) {
                for (int cellZ = cellZBase - 1; cellZ <= cellZBase + 1; cellZ++) {
                    
                    RandomSource cellRandom = RandomSource.create(worldSeed ^ (cellX * 341873128712L) ^ (cellZ * 132897987541L) ^ 0x4D495343);
                    
                    int chunksInCell = cellSpanChunks * cellSpanChunks;
                    float cellChance = Math.min(1.0f, (float) chunksInCell / config.rarity()); 
                    if (cellRandom.nextFloat() > cellChance) continue;

                    int centerX = (cellX * cellSpanChunks * 16) + cellRandom.nextInt(cellSpanChunks * 16);
                    int centerZ = (cellZ * cellSpanChunks * 16) + cellRandom.nextInt(cellSpanChunks * 16);
                    
                    Biome centerBiome;
                    if (level instanceof net.minecraft.server.level.WorldGenRegion region) {
                        net.minecraft.world.level.levelgen.RandomState randomState = region.getLevel().getChunkSource().randomState();
                        centerBiome = context.chunkGenerator().getBiomeSource().getNoiseBiome(centerX >> 2, 64 >> 2, centerZ >> 2, randomState.sampler()).value();
                    } else {
                        centerBiome = level.getNoiseBiome(centerX >> 2, 64 >> 2, centerZ >> 2).value();
                    }

                    float biomeTemp = centerBiome.getBaseTemperature();
                    float biomeMoisture = centerBiome.getModifiedClimateSettings().downfall();
                    
                    if (biomeTemp < config.minTemp() || biomeTemp > config.maxTemp()) continue;
                    if (biomeMoisture < config.minMoisture() || biomeMoisture > config.maxMoisture()) continue;

                    int actualMinY = Math.max(level.getMinBuildHeight() + maxBound, config.getAbsoluteMinY(level));
                    int actualMaxY = Math.min(level.getMaxBuildHeight() - maxBound, config.getAbsoluteMaxY(level));
                    if (actualMinY > actualMaxY) actualMinY = actualMaxY;
                    int centerY = cellRandom.nextIntBetweenInclusive(actualMinY, actualMaxY);
                    
                    float maxTilt = config.maxTilt() * (float)Math.PI / 180.0f;
                    float pitch = (cellRandom.nextFloat() - 0.5f) * 2.0f * maxTilt;
                    float roll = (cellRandom.nextFloat() - 0.5f) * 2.0f * maxTilt;
                    float cosP = (float)Math.cos(pitch);
                    float sinP = (float)Math.sin(pitch);
                    float cosR = (float)Math.cos(roll);
                    float sinR = (float)Math.sin(roll);

                    for (int lx = 0; lx < 16; lx++) {
                        for (int lz = 0; lz < 16; lz++) {
                            int worldX = (currentChunkX << 4) + lx;
                            int worldZ = (currentChunkZ << 4) + lz;

                            for (int worldY = centerY - maxBound; worldY <= centerY + maxBound; worldY++) {
                                
                                int dx = worldX - centerX;
                                int dy = worldY - centerY;
                                int dz = worldZ - centerZ;

                                float rY1 = dy * cosP - dz * sinP;
                                float rZ1 = dy * sinP + dz * cosP;
                                float rX1 = dx;

                                float localX = rX1 * cosR - rY1 * sinR;
                                float localY = rX1 * sinR + rY1 * cosR;
                                float localZ = rZ1;

                                if (localY >= -thickness && localY <= thickness) {
                                    
                                    long posHash = (long)worldX * 3133742L + (long)worldY * 23423412L + (long)worldZ * 453123L;
                                    posHash ^= worldSeed;
                                    RandomSource bpRand = RandomSource.create(posHash);
                                    float jaggedRadiusSq = radius * radius - bpRand.nextFloat() * (radius * radius * 0.4f);

                                    if (localX * localX + localZ * localZ <= jaggedRadiusSq) {
                                        if (bpRand.nextFloat() > density) continue;

                                        mutablePos.set(worldX, worldY, worldZ);
                                        if (level.isOutsideBuildHeight(mutablePos)) continue;

                                        LevelChunkSection section = sectionAccess.getSection(mutablePos);
                                        if (section != null) {
                                            int locY = worldY & 15;
                                            BlockState currentState = section.getBlockState(lx, locY, lz);
                                            
                                            List<WeightedTarget> validTargets = new ArrayList<>();
                                            float totalWeight = 0.0f;
                                            for (WeightedTarget target : config.targets()) {
                                                if (target.target().test(currentState, bpRand)) {
                                                    validTargets.add(target);
                                                    totalWeight += target.weight();
                                                }
                                            }

                                            if (!validTargets.isEmpty()) {
                                                float randomWeight = bpRand.nextFloat() * totalWeight;
                                                float currentWeight = 0.0f;
                                                for (WeightedTarget target : validTargets) {
                                                    currentWeight += target.weight();
                                                    if (randomWeight <= currentWeight) {
                                                        section.setBlockState(lx, locY, lz, target.state(), false);
                                                        placedAny = true;
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } 
        return placedAny;
    }
}

