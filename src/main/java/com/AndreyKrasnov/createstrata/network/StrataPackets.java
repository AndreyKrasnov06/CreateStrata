package com.AndreyKrasnov.createstrata.network;

import com.AndreyKrasnov.createstrata.CreateStrata;
import com.AndreyKrasnov.createstrata.radar.MechanicalRadarScreen;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class StrataPackets {

    public static void onRegisterPayloads(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(CreateStrata.MOD_ID);

        registrar.playToClient(
                RadarScanPayload.TYPE,
                RadarScanPayload.STREAM_CODEC,
                (payload, context) -> {
                    // Обработка пакета на стороне клиента (Открытие графического интерфейса)
                    context.enqueueWork(() -> {
                        Minecraft.getInstance().setScreen(new MechanicalRadarScreen(payload));
                    });
                }
        );
    }
}

