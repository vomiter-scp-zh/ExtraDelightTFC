package com.vomiter.extradelighttfc.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.vomiter.extradelight.util.BottleFluidRegistry;
import net.dries007.tfc.common.recipes.ingredients.DelegateIngredient;
import net.dries007.tfc.common.recipes.ingredients.FluidItemIngredient;
import net.dries007.tfc.common.recipes.ingredients.FluidStackIngredient;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = FluidItemIngredient.class)
public abstract class FluidItemIngredientMixin extends DelegateIngredient {
    @Shadow
    @Final
    private FluidStackIngredient fluid;

    private FluidItemIngredientMixin(@Nullable Ingredient delegate) {
        super(delegate);
    }

    @WrapMethod(method = "test(Lnet/minecraft/world/item/ItemStack;)Z")
    private boolean edtfc$matchRegisteredBottle(ItemStack stack, Operation<Boolean> original) {
        if (original.call(stack)) return true;
        return stack != null
                && !stack.isEmpty()
                && super.test(stack)
                && this.fluid.test(BottleFluidRegistry.getFluidFromBottle(stack));
    }
}
