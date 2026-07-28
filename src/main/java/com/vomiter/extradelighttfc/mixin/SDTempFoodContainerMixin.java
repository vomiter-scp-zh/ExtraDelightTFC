package com.vomiter.extradelighttfc.mixin;

import com.vomiter.survivorsdelight.util.FoodItemContainerApply;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FoodItemContainerApply.class, remap = false)
public class SDTempFoodContainerMixin {
    @Inject(method = "getRemainder", at = @At("RETURN"), cancellable = true)
    private static void edtfc$getRemainder(ItemStack stack, CallbackInfoReturnable<ItemStack> cir){
        if (cir.getReturnValue().isEmpty()){
            cir.setReturnValue(stack.getCraftingRemainingItem().copyWithCount(1));
        }
    }
}
