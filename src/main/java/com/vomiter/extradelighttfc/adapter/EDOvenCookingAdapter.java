package com.vomiter.extradelighttfc.adapter;

import com.vomiter.extradelight.common.complex.workstations.oven.recipes.OvenRecipe;
import com.vomiter.extradelighttfc.adapter.dynamic.DynamicFoodCalculator;
import com.vomiter.extradelighttfc.adapter.dynamic.DynamicFoodContext;
import com.vomiter.extradelighttfc.adapter.dynamic.FoodModifierRegistry;
import com.vomiter.extradelighttfc.adapter.dynamic.workstation.OvenDynamicFoodAdapter;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;

public final class EDOvenCookingAdapter {

    private static final OvenDynamicFoodAdapter ADAPTER = new OvenDynamicFoodAdapter();

    private static final FoodModifierRegistry<DynamicFoodContext<OvenRecipe>> MODIFIERS = new FoodModifierRegistry<>();

    private EDOvenCookingAdapter() {
    }

    public static void registerIndividual(
            FoodModifierRegistry.FoodModifier<DynamicFoodContext<OvenRecipe>> modifier
    ) {
        MODIFIERS.registerIndividual(modifier);
    }

    public static void registerTotal(
            FoodModifierRegistry.FoodModifier<DynamicFoodContext<OvenRecipe>> modifier
    ) {
        MODIFIERS.registerTotal(modifier);
    }

    public static ItemStack calcDynamic(
            ItemStackHandler inventory,
            ItemStack result,
            OvenRecipe recipe,
            Level level
    ) {
        return DynamicFoodCalculator.calculate(
                result,
                inventory,
                recipe,
                null,
                level,
                ADAPTER,
                MODIFIERS
        );
    }
}