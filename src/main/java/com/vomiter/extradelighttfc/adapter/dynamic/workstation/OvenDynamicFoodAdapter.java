package com.vomiter.extradelighttfc.adapter.dynamic.workstation;

import com.vomiter.extradelight.common.complex.workstations.oven.recipes.OvenRecipe;
import com.vomiter.extradelighttfc.adapter.dynamic.DynamicFoodAdapter;
import com.vomiter.extradelighttfc.adapter.dynamic.DynamicFoodContext;
import com.vomiter.extradelighttfc.compat.EDTFCFirmaLifeCompat;
import com.vomiter.extradelighttfc.util.FoodUtils;
import com.vomiter.survivorsdelight.util.FoodItemContainerApply;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;

public final class OvenDynamicFoodAdapter
        implements DynamicFoodAdapter<ItemStackHandler, IFluidHandler, OvenRecipe, DynamicFoodContext<OvenRecipe>> {

    private static final int INGREDIENT_SLOT_START = 0;
    private static final int INGREDIENT_SLOT_END = 9;
    private static final int CONTAINER_SLOT = 10;

    @Override
    public void applyContainer(ItemStack result, ItemStackHandler inventory, OvenRecipe recipe) {
        if (!recipe.shouldConsumeContainer()) {
            return;
        }

        FoodItemContainerApply.applyGeneral(
                result,
                inventory
                        .getStackInSlot(CONTAINER_SLOT)
                        .copyWithCount(1)
        );


    }

    @Override
    public List<ItemStack> getInputs(ItemStackHandler inventory, OvenRecipe recipe) {
        List<ItemStack> inputs = new ArrayList<>();

        for (int slot = INGREDIENT_SLOT_START; slot < INGREDIENT_SLOT_END; slot++) {
            ItemStack stack =
                    inventory.getStackInSlot(slot);

            if (!stack.isEmpty()) {
                inputs.add(stack);
            }
        }

        return inputs;
    }

    @Override
    public List<FluidStack> getInputFluids(IFluidHandler source, OvenRecipe recipe) {
        return List.of();
    }

    @Override
    public ItemStack transformIngredient(
            ItemStack ingredient,
            ItemStackHandler inventory,
            OvenRecipe recipe,
            Level level
    ) {
        if (ModList.get().isLoaded("firmalife")) {
            ItemStack ovenResult
                    = EDTFCFirmaLifeCompat.getOvenResult(
                            ingredient.copy(),
                            level
                    );

            if (!ovenResult.isEmpty()) {
                return ovenResult;
            }
        }

        ItemStack cooked
                = FoodUtils.getCookedFromRaw(
                        ingredient.copy(),
                        level
                );

        return cooked.isEmpty()
                ? ingredient
                : cooked;
    }

    @Override
    public DynamicFoodContext<OvenRecipe> createIndividualContext(
            List<ItemStack> inputs,
            List<FluidStack> inputFluids,
            ItemStack stack,
            ItemStackHandler inventory,
            IFluidHandler fluidHandler,
            OvenRecipe recipe
    ) {
        return new DynamicFoodContext<>(
                inputs,
                inputFluids,
                stack,
                recipe,
                recipe.getId(),
                DynamicFoodContext.Phase.INDIVIDUAL
        );
    }

    @Override
    public DynamicFoodContext<OvenRecipe> createTotalContext(
            List<ItemStack> inputs,
            List<FluidStack> inputFluids,
            ItemStack result,
            ItemStackHandler inventory,
            IFluidHandler fluidHandler,
            OvenRecipe recipe
    ) {
        return new DynamicFoodContext<>(
                inputs,
                inputFluids,
                result,
                recipe,
                recipe.getId(),
                DynamicFoodContext.Phase.TOTAL
        );
    }
}