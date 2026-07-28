package com.vomiter.extradelighttfc.common;

import com.vomiter.extradelighttfc.registry.EDTFCBlockEntityTypes;
import com.vomiter.survivorsdelight.common.food.block.SDDecayingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class RecipeFeastBlockEntity extends SDDecayingBlockEntity {
    public RecipeFeastBlockEntity(BlockPos pos, BlockState state) {
        super(EDTFCBlockEntityTypes.DECAY_RECIPE_FEAST.get(), pos, state);
    }
}
