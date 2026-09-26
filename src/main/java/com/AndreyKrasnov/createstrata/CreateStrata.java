package com.AndreyKrasnov.createstrata;

import com.AndreyKrasnov.createstrata.network.StrataPackets;
import com.AndreyKrasnov.createstrata.radar.RadarRegistries;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@Mod(CreateStrata.MOD_ID)
public class CreateStrata {
    public static final String MOD_ID = "createstrata";
    
    public CreateStrata(IEventBus modEventBus) {
        // Регистрируем генерацию мира
        ModRegistries.register(modEventBus);
        
        // Регистрируем блоки и предметы нашего Create-Радара
        RadarRegistries.register(modEventBus);
        
        // Подключаем сетевые пакеты для передачи скана Радара
        modEventBus.addListener(StrataPackets::onRegisterPayloads);
        
        // Добавляем наш предмет в творческое меню
        modEventBus.addListener(this::addCreative);
        
        NeoForge.EVENT_BUS.addListener(this::onRegisterCommands);
    }
    
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        // Кладем радар во вкладку Редстоун Механизмов
        if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
            event.accept(RadarRegistries.MECHANICAL_RADAR_ITEM.get());
        }
    }
    
    private void onRegisterCommands(RegisterCommandsEvent event) {
        LocateVeinCommand.register(event.getDispatcher());
    }
}



