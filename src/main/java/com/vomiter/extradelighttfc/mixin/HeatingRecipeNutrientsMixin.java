package com.vomiter.extradelighttfc.mixin;

import com.vomiter.extradelighttfc.util.ExtraNutrientsAfterHeating;
import net.dries007.tfc.common.recipes.inventory.ItemStackInventory;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.dries007.tfc.common.recipes.HeatingRecipe;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(value = HeatingRecipe.class, remap = false)
public abstract class HeatingRecipeNutrientsMixin {
    @Inject(method = "assemble(Lnet/dries007/tfc/common/recipes/inventory/ItemStackInventory;Lnet/minecraft/core/RegistryAccess;)Lnet/minecraft/world/item/ItemStack;", at = @At("RETURN"), cancellable = true)
    private void edtfc$extraNutrients(ItemStackInventory inventory, RegistryAccess access, CallbackInfoReturnable<ItemStack> cir) {
        cir.setReturnValue(ExtraNutrientsAfterHeating.apply(inventory.getStack(), cir.getReturnValue(), access));
    }
    @WrapOperation(method = "assembleStacked", at = @At(value = "INVOKE",
            target = "Lnet/dries007/tfc/common/recipes/outputs/ItemStackProvider;getSingleStack(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack edtfc$stackedNutrients(ItemStackProvider provider, ItemStack input, Operation<ItemStack> original) {
        // Correct per-item nutrition before TFC expands/caps/randomizes the output stack.
        return ExtraNutrientsAfterHeating.apply(input, original.call(provider, input), null);
    }
}
