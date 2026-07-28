package com.vomiter.extradelighttfc.mixin.fancy_tank;

import com.vomiter.extradelight.util.BottleFluidRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = BottleFluidRegistry.class, remap = false)
public class FluidBottleRegistryMixin {

    @ModifyConstant(
            method = "<clinit>",
            constant = @Constant(intValue = 250),
            require = 1
    )
    private static int edtfc$modifyBottleVolume(int original) {
        return 500;
    }


}