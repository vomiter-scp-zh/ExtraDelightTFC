package com.vomiter.extradelighttfc.adapter;

import com.vomiter.extradelight.common.complex.workstations.mixingbowl.recipes.MixingBowlRecipe;
import com.vomiter.extradelighttfc.adapter.dynamic.DynamicFoodCalculator;
import com.vomiter.extradelighttfc.adapter.dynamic.DynamicFoodContext;
import com.vomiter.extradelighttfc.adapter.dynamic.FoodModifierRegistry;
import com.vomiter.extradelighttfc.adapter.dynamic.workstation.MixingDynamicFoodAdapter;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.ItemStackHandler;

public final class EDMixingBowlAdapter {

    private static final MixingDynamicFoodAdapter ADAPTER = new MixingDynamicFoodAdapter();

    private static final FoodModifierRegistry<DynamicFoodContext<MixingBowlRecipe>> MODIFIERS = new FoodModifierRegistry<>();

    private EDMixingBowlAdapter() {
    }

    public static void registerIndividual(
            FoodModifierRegistry.FoodModifier<DynamicFoodContext<MixingBowlRecipe>> modifier
    ) {
        MODIFIERS.registerIndividual(modifier);
    }

    public static void registerTotal(
            FoodModifierRegistry.FoodModifier<DynamicFoodContext<MixingBowlRecipe>> modifier
    ) {
        MODIFIERS.registerTotal(modifier);
    }

    public static ItemStack calcDynamic(
            ItemStackHandler inventory,
            IFluidHandler fluidHandler,
            ItemStack result,
            MixingBowlRecipe recipe,
            Level level
    ) {
        return DynamicFoodCalculator.calculate(
                result,
                inventory,
                recipe,
                fluidHandler,
                level,
                ADAPTER,
                MODIFIERS
        );
    }
}