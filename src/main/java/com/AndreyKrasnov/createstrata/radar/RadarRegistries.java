package com.AndreyKrasnov.createstrata.radar;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.OrientedRotatingVisual;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.material.MapColor;

import static com.AndreyKrasnov.createstrata.CreateStrata.REGISTRATE;

public class RadarRegistries {

    public static final BlockEntry<MechanicalRadarBlock> MECHANICAL_RADAR = REGISTRATE
            .block("mechanical_radar", MechanicalRadarBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.mapColor(MapColor.METAL).strength(3.0f, 6.0f).requiresCorrectToolForDrops())
            .item()
            .build()
            .register();

    public static final BlockEntityEntry<MechanicalRadarBlockEntity> RADAR_BLOCK_ENTITY = REGISTRATE
            .blockEntity("mechanical_radar", MechanicalRadarBlockEntity::new)
            .visual(() -> OrientedRotatingVisual.of(AllPartialModels.SHAFT_HALF), false)
            .validBlocks(MECHANICAL_RADAR)
            .register();

    public static void register() {
        // Загрузка класса для инициализации регистров
    }
}
