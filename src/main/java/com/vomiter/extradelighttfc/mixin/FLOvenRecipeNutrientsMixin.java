package com.vomiter.extradelighttfc.mixin;

import com.vomiter.extradelighttfc.util.ExtraNutrientsAfterHeating;
import net.dries007.tfc.common.recipes.inventory.ItemStackInventory;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "com.eerussianguy.firmalife.common.recipes.OvenRecipe", remap = false)
public abstract class FLOvenRecipeNutrientsMixin {
    @Inject(method = "assemble(Lnet/dries007/tfc/common/recipes/inventory/ItemStackInventory;Lnet/minecraft/core/RegistryAccess;)Lnet/minecraft/world/item/ItemStack;", at = @At("RETURN"), cancellable = true)
    private void edtfc$extraNutrients(ItemStackInventory inventory, RegistryAccess access, CallbackInfoReturnable<ItemStack> cir) {
        cir.setReturnValue(ExtraNutrientsAfterHeating.apply(inventory.getStack(), cir.getReturnValue(), access));
    }
}
