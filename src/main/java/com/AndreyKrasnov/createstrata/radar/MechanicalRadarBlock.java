package com.AndreyKrasnov.createstrata.radar;

import com.AndreyKrasnov.createstrata.CreateStrata;
import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;

public class MechanicalRadarBlock extends KineticBlock implements IBE<MechanicalRadarBlockEntity> {

    public MechanicalRadarBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Class<MechanicalRadarBlockEntity> getBlockEntityClass() {
        return MechanicalRadarBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends MechanicalRadarBlockEntity> getBlockEntityType() {
        return RadarRegistries.RADAR_BLOCK_ENTITY.get();
    }

    @Override
    public Axis getRotationAxis(BlockState state) {
        return Axis.Y;
    }

    @Override
    public boolean hasShaftTowards(net.minecraft.world.level.LevelReader world, net.minecraft.core.BlockPos pos, BlockState state, Direction face) {
        return face == Direction.DOWN;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            withBlockEntityDo(level, pos, be -> {
                be.sendScanToPlayer(player);
            });
        }
        return InteractionResult.SUCCESS;
    }
}

