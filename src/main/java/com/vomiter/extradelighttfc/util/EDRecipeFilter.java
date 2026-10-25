package com.vomiter.extradelighttfc.util;

import com.vomiter.extradelight.ExtraDelight;
import com.vomiter.extradelight.registry.ExtraDelightRecipes;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class EDRecipeFilter {
    public static boolean shouldBlock(Recipe<?> recipe){
        // Keep the black-steel progression and salt-water requirement identical in dev and releases.
        if (recipe.getId().getNamespace().equals(ExtraDelight.MOD_ID)
                && (recipe.getType().equals(ExtraDelightRecipes.EVAPORATOR.get())
                || recipe.getType().equals(ExtraDelightRecipes.DRYING_RACK.get())
                || recipe.getType().equals(ExtraDelightRecipes.MIXING_BOWL.get())
                || recipe.getType().equals(ExtraDelightRecipes.DOUGH_SHAPING.get())
                || recipe.getId().getPath().equals("crafting/wooden_spoon")
                || recipe.getId().getPath().equals("crafting/dough_shaping")
                || recipe.getType().equals(ExtraDelightRecipes.OVEN.get())
                    && (recipe.getId().getPath().contains("cookie") || recipe.getId().getPath().contains("gingerbread"))
                || recipe.getId().getPath().equals("crafting/drying_rack")
                || recipe.getId().getPath().equals("cooking/seaweed_paste")
                || recipe.getId().getPath().equals("crafting/evaporator"))) {
            return true;
        }
        if (FMLEnvironment.production){
            if (recipe.getId().getNamespace().equals(ExtraDelight.MOD_ID)){
                return !recipe.getType().equals(ExtraDelightRecipes.FEAST.get());
            }
        }
        return false;
    }
}
