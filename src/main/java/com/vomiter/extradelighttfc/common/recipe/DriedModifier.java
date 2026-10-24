package com.vomiter.extradelighttfc.common.recipe;

import com.vomiter.extradelighttfc.registry.EDTFCFoodTraits;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.recipes.outputs.ItemStackModifier;
import net.minecraft.world.item.ItemStack;

public enum DriedModifier implements ItemStackModifier.SingleInstance<DriedModifier> {
    INSTANCE;

    @Override
    public ItemStack apply(ItemStack stack, ItemStack input) {
        return FoodCapability.applyTrait(stack, EDTFCFoodTraits.DRIED);
    }

    @Override
    public DriedModifier instance() {
        return INSTANCE;
    }
}
