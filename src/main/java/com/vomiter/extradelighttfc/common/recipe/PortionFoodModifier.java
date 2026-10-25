package com.vomiter.extradelighttfc.common.recipe;

import com.vomiter.extradelighttfc.util.FoodPortions;
import net.dries007.tfc.common.recipes.outputs.ItemStackModifier;
import net.minecraft.world.item.ItemStack;

public enum PortionFoodModifier implements ItemStackModifier.SingleInstance<PortionFoodModifier> {
    INSTANCE;
    @Override public ItemStack apply(ItemStack result, ItemStack input) { return FoodPortions.transfer(input, result); }
    @Override public boolean dependsOnInput() { return true; }
    @Override public PortionFoodModifier instance() { return INSTANCE; }
}
