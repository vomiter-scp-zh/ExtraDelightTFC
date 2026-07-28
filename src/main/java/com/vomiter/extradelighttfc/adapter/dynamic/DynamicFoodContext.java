package com.vomiter.extradelighttfc.adapter.dynamic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public record DynamicFoodContext<R>(
        List<ItemStack> inputs,
        List<FluidStack> inputFluids,
        ItemStack stack,
        R recipe,
        ResourceLocation recipeId,
        Phase phase
) {
    public enum Phase {
        INDIVIDUAL,
        TOTAL
    }
}