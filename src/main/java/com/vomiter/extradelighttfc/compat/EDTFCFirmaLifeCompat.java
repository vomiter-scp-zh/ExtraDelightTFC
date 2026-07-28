package com.vomiter.extradelighttfc.compat;

import com.eerussianguy.firmalife.common.recipes.FLRecipeTypes;
import com.eerussianguy.firmalife.common.recipes.OvenRecipe;
import net.dries007.tfc.common.recipes.inventory.ItemStackInventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;


public class EDTFCFirmaLifeCompat {
    public static ItemStack getOvenResult(ItemStack ingredient, Level level){
        var recipe = level.getRecipeManager()
                        .getRecipeFor(
                                FLRecipeTypes.OVEN.get(),
                                new ItemStackInventory(ingredient),
                                level
                        );
        return recipe
                .map(ovenRecipe -> ovenRecipe.assemble(new ItemStackInventory(ingredient), level.registryAccess()))
                .orElse(ItemStack.EMPTY);
    }
}
