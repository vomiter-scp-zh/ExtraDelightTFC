package com.vomiter.extradelighttfc.mixin.fancy_tank;

import com.vomiter.extradelight.gui.FancyTankInSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = FancyTankInSlot.class, remap = false)
public class FancyTankInSlotMixin {

    @ModifyConstant(method = "calcFluidInSlotSize", constant = @Constant(intValue = 250))
    private int edtfc$bottleVolume(int constant){
        return 500;
    }
}
