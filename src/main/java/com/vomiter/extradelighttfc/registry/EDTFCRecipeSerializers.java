package com.vomiter.extradelighttfc.registry;

import com.vomiter.extradelighttfc.ExtraDelightTFC;
import com.vomiter.extradelighttfc.common.recipe.TFCMortarRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class EDTFCRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ExtraDelightTFC.MOD_ID);
    public static final RegistryObject<RecipeSerializer<TFCMortarRecipe>> MORTAR =
            SERIALIZERS.register("mortar", TFCMortarRecipe.Serializer::new);

    private EDTFCRecipeSerializers() {}
}
