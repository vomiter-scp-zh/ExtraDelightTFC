package com.vomiter.extradelighttfc.util;

import net.dries007.tfc.common.items.Food;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.common.recipes.TFCRecipeTypes;
import net.dries007.tfc.common.recipes.inventory.ItemStackInventory;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.function.Supplier;

public class FoodUtils {
    public static ItemStack getCookedFromRaw(ItemStack rawStack, Level level){
        var wrapper = new ItemStackInventory(rawStack);
        var recipe = level.getRecipeManager().getRecipeFor(TFCRecipeTypes.HEATING.get(), wrapper, level);
        return recipe.map(heatingRecipe -> heatingRecipe.assemble(wrapper, level.registryAccess())).orElse(ItemStack.EMPTY);
    }

    public static Item getFoodItem(Food food){
        return TFCItems.FOOD.get(food).get();
    }

    public static Item[] getFoodItem(Food... foods){
        return Arrays.stream(foods).map(FoodUtils::getFoodItem).toArray(Item[]::new);
    }

}
