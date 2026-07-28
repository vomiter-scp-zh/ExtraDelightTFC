package com.vomiter.extradelighttfc.adapter;

import com.vomiter.extradelight.common.complex.workstations.oven.OvenBlockEntity;
import com.vomiter.extradelight.common.complex.workstations.oven.recipes.OvenRecipe;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.capabilities.food.IFood;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class EDOvenCanCookAdapter {
    public static boolean canCook(OvenBlockEntity oven, OvenRecipe recipe) {
        assert oven.getLevel() != null;
        ItemStack resultStack = EDOvenCookingAdapter.calcDynamic(
                oven.getInventory(),
                recipe.assemble(
                        new RecipeWrapper(oven.getInventory()),
                        oven.getLevel().registryAccess()
                ),
                recipe,
                oven.getLevel()
        );
        if (oven.getInventory().getStackInSlot(10).getItem() != recipe.getOutputContainer().getItem()) {
            return false;
        }

        if (resultStack.isEmpty()) {
            return false;
        }

        boolean anyRotten = false;
        for (int i = 0; i < oven.getInventory().getSlots(); i++) {
            var item = oven.getInventory().getStackInSlot(i);
            if(item.isEmpty()) continue;
            IFood food = FoodCapability.get(item);
            if(food == null) continue;
            if(food.isRotten()) anyRotten = true;
        }
        if(anyRotten) return false;

        ItemStack storedMealStack = oven.getInventory().getStackInSlot(11);
        if (storedMealStack.isEmpty()) {
            return true;
        }

        if (!FoodCapability.areStacksStackableExceptCreationDate(storedMealStack, resultStack)) {
            return false;
        }

        if (storedMealStack.getCount() + resultStack.getCount() <= oven.getInventory().getSlotLimit(11) && storedMealStack.getCount() + resultStack.getCount() <= resultStack.getMaxStackSize()) {
            return true;
        }

        return false;
    }

}
