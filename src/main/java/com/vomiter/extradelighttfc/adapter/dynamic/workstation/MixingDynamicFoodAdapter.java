package com.vomiter.extradelighttfc.adapter.dynamic.workstation;

import com.vomiter.extradelight.common.complex.workstations.mixingbowl.recipes.MixingBowlRecipe;
import com.vomiter.extradelight.common.recipes.ingredients.SizedFluidIngredient;
import com.vomiter.extradelighttfc.adapter.dynamic.DynamicFoodAdapter;
import com.vomiter.extradelighttfc.adapter.dynamic.DynamicFoodContext;
import com.vomiter.survivorsdelight.util.FoodItemContainerApply;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;

public final class MixingDynamicFoodAdapter
        implements DynamicFoodAdapter<ItemStackHandler, IFluidHandler, MixingBowlRecipe, DynamicFoodContext<MixingBowlRecipe>> {

    private static final int INGREDIENT_SLOT_START = 0;
    private static final int INGREDIENT_SLOT_END = 9;
    private static final int CONTAINER_SLOT = 9;

    @Override
    public void applyContainer(ItemStack result, ItemStackHandler inventory, MixingBowlRecipe recipe) {
        FoodItemContainerApply.applyGeneral(
                result,
                inventory.getStackInSlot(CONTAINER_SLOT).copyWithCount(1)
        );
    }

    @Override
    public List<ItemStack> getInputs(ItemStackHandler inventory, MixingBowlRecipe recipe) {
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
    public List<FluidStack> getInputFluids(IFluidHandler source, MixingBowlRecipe recipe) {
        List<FluidStack> result = new ArrayList<>();
        int[] remaining = new int[source.getTanks()];
        for (int i = 0; i < remaining.length; i++) remaining[i] = source.getFluidInTank(i).getAmount();
        for (SizedFluidIngredient sizedFluidIngredient : recipe.getFluids()) {
            var amount = sizedFluidIngredient.getAmount();
            for (int i = 0; i < source.getTanks(); i++) {
                FluidStack stored = source.getFluidInTank(i).copy();
                stored.setAmount(remaining[i]);
                if (sizedFluidIngredient.test(stored)) {
                    FluidStack consumed = stored.copy();
                    consumed.setAmount(amount);
                    result.add(consumed);
                    remaining[i] -= amount;
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public DynamicFoodContext<MixingBowlRecipe> createIndividualContext(
            List<ItemStack> inputs,
            List<FluidStack> inputFluids,
            ItemStack stack,
            ItemStackHandler inventory,
            IFluidHandler fluidHandler,
            MixingBowlRecipe recipe
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
    public DynamicFoodContext<MixingBowlRecipe> createTotalContext(
            List<ItemStack> inputs,
            List<FluidStack> inputFluids,
            ItemStack result,
            ItemStackHandler inventory,
            IFluidHandler fluidHandler,
            MixingBowlRecipe recipe
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