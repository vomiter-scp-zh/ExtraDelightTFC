package com.vomiter.extradelighttfc.mixin;

import com.vomiter.extradelight.common.complex.workstations.doughshaping.recipes.DoughShapingRecipe;
import com.vomiter.extradelighttfc.util.FoodPortions;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SingleItemRecipe.class)
public abstract class DoughShapingFoodMixin {
    @Inject(method = "assemble", at = @At("RETURN"), cancellable = true)
    private void edtfc$portionDough(Container input, RegistryAccess access, CallbackInfoReturnable<ItemStack> cir) {
        if ((Object) this instanceof DoughShapingRecipe) {
            cir.setReturnValue(FoodPortions.transfer(input.getItem(0), cir.getReturnValue()));
        }
    }
}
