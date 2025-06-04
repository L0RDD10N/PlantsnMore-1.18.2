package com.mods.plantsnmore.block.custom.entity.custom;

import com.mods.plantsnmore.block.custom.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CoconutTreeBlockEntity extends BlockEntity {

    public CoconutTreeBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.COCONUTS.get(), blockPos, blockState);
    }

}