package com.AndreyKrasnov.createstrata.radar;

import com.AndreyKrasnov.createstrata.CreateStrata;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class RadarRegistries {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, CreateStrata.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, CreateStrata.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, CreateStrata.MOD_ID);

    // Регистрируем блок радара, задаем базовые свойства (похожие на металл)
    public static final Supplier<Block> MECHANICAL_RADAR = BLOCKS.register("mechanical_radar", 
            () -> new MechanicalRadarBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(3.0f, 6.0f)
                    .requiresCorrectToolForDrops()));

    // Регистрируем предмет (BlockItem), чтобы радар можно было взять в руки и поставить
    public static final Supplier<Item> MECHANICAL_RADAR_ITEM = ITEMS.register("mechanical_radar",
            () -> new BlockItem(MECHANICAL_RADAR.get(), new Item.Properties()));

    // Регистрируем BlockEntity (Тайл-энтити). Даем ему знать, к какому блоку он привязан
    public static final Supplier<BlockEntityType<MechanicalRadarBlockEntity>> RADAR_BLOCK_ENTITY = BLOCK_ENTITIES.register("mechanical_radar",
            () -> BlockEntityType.Builder.of(MechanicalRadarBlockEntity::new, MECHANICAL_RADAR.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
        BLOCK_ENTITIES.register(eventBus);
    }
}

