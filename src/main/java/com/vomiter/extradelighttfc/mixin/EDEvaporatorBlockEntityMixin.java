package com.vomiter.extradelighttfc.mixin;

import com.vomiter.extradelight.common.complex.workstations.evaporator.EvaporatorBlockEntity;
import net.dries007.tfc.util.climate.Climate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EvaporatorBlockEntity.class)
public abstract class EDEvaporatorBlockEntityMixin {
    // ED stores the biome temperature in its first (and only) float local.
    @ModifyVariable(method = "tick", at = @At("STORE"), remap = false, name = "temp")
    private static float edtfc$climateTemperature(float temp, Level level, BlockPos pos,
                                                  BlockState state, BlockEntity blockEntity) {
        return Climate.getVanillaBiomeTemperature(level, pos);
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true, remap = false)
    private static void edtfc$waitForHarvest(Level level, BlockPos pos, BlockState state,
                                            BlockEntity blockEntity, CallbackInfo ci) {
        EvaporatorBlockEntity evaporator = (EvaporatorBlockEntity) blockEntity;
        if (level.isClientSide || !evaporator.isInventoryEmpty()) {
            ci.cancel();
        }
    }
}
