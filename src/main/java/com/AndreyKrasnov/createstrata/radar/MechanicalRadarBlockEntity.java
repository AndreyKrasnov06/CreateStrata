package com.AndreyKrasnov.createstrata.radar;

import com.AndreyKrasnov.createstrata.ClusterVeinFeature;
import com.AndreyKrasnov.createstrata.CustomVeinConfig;
import com.AndreyKrasnov.createstrata.DiscVeinFeature;
import com.AndreyKrasnov.createstrata.PipeVeinFeature;
import com.AndreyKrasnov.createstrata.network.RadarScanPayload;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class MechanicalRadarBlockEntity extends KineticBlockEntity {
    
    private List<RadarScanPayload.VeinPoint> lastScannedVeins = new ArrayList<>();
    private int currentRadiusChunks = 0;

    public MechanicalRadarBlockEntity(BlockPos pos, BlockState state) {
        super(RadarRegistries.RADAR_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    public float calculateStressApplied() {
        float impact = 16.0f; 
        this.lastStressApplied = impact;
        return impact;
    }

    @Override
    public void tick() {
        super.tick();
        
        if (level == null || level.isClientSide()) return;
        
        float currentSpeed = Math.abs(getSpeed());
        
        if (currentSpeed < 32.0f) {
            currentRadiusChunks = 0;
            lastScannedVeins.clear();
            return;
        }

        if (level.getGameTime() % 20 == 0) {
            performScan(currentSpeed);
        }
    }

    private void performScan(float rpm) {
        this.currentRadiusChunks = Math.max(1, (int)(rpm / 16.0f));
        this.lastScannedVeins.clear();

        if (!(level instanceof ServerLevel serverLevel)) return;
        
        long worldSeed = serverLevel.getSeed();
        RandomState randomState = serverLevel.getChunkSource().randomState();
        var registry = serverLevel.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE);

        BlockPos radarPos = getBlockPos();
        int maxRadiusBlocks = currentRadiusChunks * 16;
        int maxScanX = radarPos.getX() + maxRadiusBlocks;
        int minScanX = radarPos.getX() - maxRadiusBlocks;
        int maxScanZ = radarPos.getZ() + maxRadiusBlocks;
        int minScanZ = radarPos.getZ() - maxRadiusBlocks;

        for (ConfiguredFeature<?, ?> featureHolder : registry) {
            if (featureHolder.config() instanceof CustomVeinConfig config) {
                
                int maxDim = Math.max(config.radius(), config.height());
                int maxBound = (int) Math.ceil(maxDim * 1.5);
                int cellSpanChunks = Math.max(2, (maxBound / 16) + 2);
                int cellSpanBlocks = cellSpanChunks * 16;
                int rarity = config.rarity(); 
                
                long salt1 = 0, salt2 = 0, salt3 = 0;
                if (featureHolder.feature() instanceof ClusterVeinFeature) {
                    salt1 = 812371813L; salt2 = 912381231L; salt3 = config.getSalt();
                } else if (featureHolder.feature() instanceof DiscVeinFeature) {
                    salt1 = 341873128712L; salt2 = 132897987541L; salt3 = config.getSalt();
                } else if (featureHolder.feature() instanceof PipeVeinFeature) {
                    salt1 = 123987123L; salt2 = 987123987L; salt3 = config.getSalt();
                } else {
                    continue; 
                }

                int startCellX = Math.floorDiv(minScanX, cellSpanBlocks) - 1;
                int endCellX = Math.floorDiv(maxScanX, cellSpanBlocks) + 1;
                
                int startCellZ = Math.floorDiv(minScanZ, cellSpanBlocks) - 1;
                int endCellZ = Math.floorDiv(maxScanZ, cellSpanBlocks) + 1;

                int chunksInCell = cellSpanChunks * cellSpanChunks;
                float cellChance = Math.min(1.0f, (float) chunksInCell / rarity);

                for (int cellX = startCellX; cellX <= endCellX; cellX++) {
                    for (int cellZ = startCellZ; cellZ <= endCellZ; cellZ++) {
                        RandomSource random = RandomSource.create(worldSeed ^ (cellX * salt1) ^ (cellZ * salt2) ^ salt3);
                        
                        if (random.nextFloat() <= cellChance) {
                            int centerX = (cellX * cellSpanBlocks) + random.nextInt(cellSpanBlocks);
                            int centerZ = (cellZ * cellSpanBlocks) + random.nextInt(cellSpanBlocks);
                            
                            Biome centerBiome = serverLevel.getChunkSource().getGenerator().getBiomeSource().getNoiseBiome(centerX >> 2, 64 >> 2, centerZ >> 2, randomState.sampler()).value();
                            
                            float centerTemp = centerBiome.getBaseTemperature();
                            float centerMoisture = centerBiome.getModifiedClimateSettings().downfall();
                            
                            boolean tempPass = centerTemp >= config.minTemp() && centerTemp <= config.maxTemp();
                            boolean moistPass = centerMoisture >= config.minMoisture() && centerMoisture <= config.maxMoisture();
                            
                            if (tempPass && moistPass) {
                                if (Math.abs(centerX - radarPos.getX()) <= maxRadiusBlocks && Math.abs(centerZ - radarPos.getZ()) <= maxRadiusBlocks) {
                                    int actualMinY = Math.max(serverLevel.getMinBuildHeight() + maxBound, config.getAbsoluteMinY(serverLevel));
                                    int actualMaxY = Math.min(serverLevel.getMaxBuildHeight() - maxBound, config.getAbsoluteMaxY(serverLevel));
                                    if (actualMinY > actualMaxY) actualMinY = actualMaxY;
                                    int centerY = random.nextIntBetweenInclusive(actualMinY, actualMaxY);
                                    
                                    int surfaceY = serverLevel.getChunkSource().getGenerator().getBaseHeight(
                                        centerX, centerZ, 
                                        Heightmap.Types.WORLD_SURFACE_WG, 
                                        serverLevel, 
                                        randomState
                                    );
                                    
                                    if (centerY > surfaceY) {
                                        continue;
                                    }
                                    
                                    lastScannedVeins.add(new RadarScanPayload.VeinPoint(
                                            new BlockPos(centerX, centerY, centerZ), 
                                            config.getParsedColor(),
                                            config.displayName()
                                    ));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void sendScanToPlayer(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            if (currentRadiusChunks <= 0) {
                PacketDistributor.sendToPlayer(serverPlayer, new RadarScanPayload(getBlockPos(), 0, new ArrayList<>()));
                return;
            }
            PacketDistributor.sendToPlayer(serverPlayer, new RadarScanPayload(getBlockPos(), currentRadiusChunks, lastScannedVeins));
        }
    }
}
