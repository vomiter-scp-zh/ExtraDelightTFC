package com.vomiter.extradelighttfc.util;

import com.vomiter.extradelight.ExtraDelight;
import com.vomiter.extradelight.registry.ExtraDelightRecipes;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class EDRecipeFilter {
    public static boolean shouldBlock(Recipe<?> recipe){
        if (FMLEnvironment.production){
            if (recipe.getId().getNamespace().equals(ExtraDelight.MOD_ID)){
                return !recipe.getType().equals(ExtraDelightRecipes.FEAST.get());
            }
        }
        return false;
    }
}
