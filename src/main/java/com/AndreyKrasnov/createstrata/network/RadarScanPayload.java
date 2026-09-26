package com.AndreyKrasnov.createstrata.network;

import com.AndreyKrasnov.createstrata.CreateStrata;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public record RadarScanPayload(BlockPos radarPos, int radiusChunks, List<RadarScanPayload.VeinPoint> veinCenters) implements CustomPacketPayload {

    public record VeinPoint(BlockPos pos, int colorArgb, String name) {}

    public static final StreamCodec<FriendlyByteBuf, VeinPoint> VEIN_POINT_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, VeinPoint::pos,
            ByteBufCodecs.INT, VeinPoint::colorArgb,
            ByteBufCodecs.STRING_UTF8, VeinPoint::name,
            VeinPoint::new
    );

    public static final Type<RadarScanPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(CreateStrata.MOD_ID, "radar_scan"));

    public static final StreamCodec<FriendlyByteBuf, RadarScanPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, RadarScanPayload::radarPos,
            ByteBufCodecs.INT, RadarScanPayload::radiusChunks,
            ByteBufCodecs.collection(ArrayList::new, VEIN_POINT_CODEC), RadarScanPayload::veinCenters,
            RadarScanPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

