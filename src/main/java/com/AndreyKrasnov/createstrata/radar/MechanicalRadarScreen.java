package com.AndreyKrasnov.createstrata.radar;

import com.AndreyKrasnov.createstrata.network.RadarScanPayload;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

public class MechanicalRadarScreen extends Screen {
    
    private final RadarScanPayload payload;
    private final int radarRadiusChunks;
    
    private final float SCALE = 0.5f; 
    
    public MechanicalRadarScreen(RadarScanPayload payload) {
        super(Component.translatable("gui.createstrata.radar.title"));
        this.payload = payload;
        this.radarRadiusChunks = payload.radiusChunks();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        
        if (radarRadiusChunks <= 0) {
            guiGraphics.drawCenteredString(this.font, Component.translatable("gui.createstrata.radar.no_power"), centerX, centerY, 0xFF5555);
            return;
        }

        int maxDistBlocks = radarRadiusChunks * 16;
        
        int windowRadiusPixels = (int)(maxDistBlocks * SCALE);
        int left = centerX - windowRadiusPixels;
        int right = centerX + windowRadiusPixels;
        int top = centerY - windowRadiusPixels;
        int bottom = centerY + windowRadiusPixels;
        
        guiGraphics.fill(left, top, right, bottom, 0xDD0a1420);
        
        int borderColor = 0xAA00DDFF;
        guiGraphics.fill(left - 1, top - 1, right + 1, top, borderColor);
        guiGraphics.fill(left - 1, bottom, right + 1, bottom + 1, borderColor);
        guiGraphics.fill(left - 1, top, left, bottom, borderColor);
        guiGraphics.fill(right, top, right + 1, bottom, borderColor);
        
        guiGraphics.drawCenteredString(this.font, "N", centerX, top - 12, 0xFFFF3333);
        guiGraphics.drawCenteredString(this.font, Component.translatable("gui.createstrata.radar.scanning", radarRadiusChunks), centerX, bottom + 5, 0xFFFFFF);

        BlockPos radarPos = payload.radarPos();
        
        int startX = radarPos.getX() - maxDistBlocks;
        int endX = radarPos.getX() + maxDistBlocks;
        int startZ = radarPos.getZ() - maxDistBlocks;
        int endZ = radarPos.getZ() + maxDistBlocks;
        
        int gridColor = 0x3300FF00;
        
        for (int x = startX; x <= endX; x++) {
            if (x % 16 == 0) {
                int screenX = centerX + (int)((x - radarPos.getX()) * SCALE);
                guiGraphics.fill(screenX, top, screenX + 1, bottom, gridColor);
            }
        }
        
        for (int z = startZ; z <= endZ; z++) {
            if (z % 16 == 0) {
                int screenY = centerY + (int)((z - radarPos.getZ()) * SCALE);
                guiGraphics.fill(left, screenY, right, screenY + 1, gridColor);
            }
        }

        RadarScanPayload.VeinPoint hoveredVein = null;

        for (RadarScanPayload.VeinPoint vp : payload.veinCenters()) {
            BlockPos vein = vp.pos();
            int dx = vein.getX() - radarPos.getX();
            int dz = vein.getZ() - radarPos.getZ();
            
            if (Math.abs(dx) > maxDistBlocks || Math.abs(dz) > maxDistBlocks) continue;
            
            int screenX = centerX + (int)(dx * SCALE);
            int screenY = centerY + (int)(dz * SCALE);
            
            guiGraphics.fill(screenX - 1, screenY - 1, screenX + 2, screenY + 2, vp.colorArgb());
            
            if (mouseX >= screenX - 2 && mouseX <= screenX + 2 && mouseY >= screenY - 2 && mouseY <= screenY + 2) {
                hoveredVein = vp;
            }
        }
        
        guiGraphics.fill(centerX - 1, centerY - 1, centerX + 2, centerY + 2, 0xFFFFFFFF);
        
        if (hoveredVein != null) {
            Component veinName = Component.translatable(hoveredVein.name());
            Component tooltip = Component.translatable("gui.createstrata.radar.vein_format", veinName, hoveredVein.pos().getX(), hoveredVein.pos().getY(), hoveredVein.pos().getZ());
            guiGraphics.renderTooltip(this.font, tooltip, mouseX, mouseY);
        }
    }
    
    @Override
    public boolean isPauseScreen() {
        return false;
    }
}

