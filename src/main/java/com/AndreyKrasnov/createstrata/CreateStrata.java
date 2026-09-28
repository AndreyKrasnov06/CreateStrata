package com.AndreyKrasnov.createstrata;

import com.AndreyKrasnov.createstrata.network.StrataPackets;
import com.AndreyKrasnov.createstrata.radar.RadarRegistries;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@Mod(CreateStrata.MOD_ID)
public class CreateStrata {
    public static final String MOD_ID = "createstrata";
    
    // Инициализация CreateRegistrate с привязкой по умолчанию к вкладке механизмов
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID)
            .defaultCreativeTab(CreativeModeTabs.REDSTONE_BLOCKS);
    
    public CreateStrata(IEventBus modEventBus) {
        REGISTRATE.registerEventListeners(modEventBus);
        
        ModRegistries.register(modEventBus);
        
        RadarRegistries.register();
        
        modEventBus.addListener(StrataPackets::onRegisterPayloads);
        
        NeoForge.EVENT_BUS.addListener(this::onRegisterCommands);
    }
    
    private void onRegisterCommands(RegisterCommandsEvent event) {
        LocateVeinCommand.register(event.getDispatcher());
    }
}
