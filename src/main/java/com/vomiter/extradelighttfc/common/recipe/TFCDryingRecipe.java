package com.vomiter.extradelighttfc.common.recipe;

import com.google.gson.JsonObject;
import com.vomiter.extradelighttfc.compat.EDTFCFirmaLifeCompat;
import net.minecraftforge.fml.ModList;
import com.google.gson.JsonSyntaxException;
import com.vomiter.extradelight.common.complex.workstations.dryingrack.DryingRackRecipe;
import com.vomiter.extradelighttfc.registry.EDTFCFoodTraits;
import com.vomiter.extradelighttfc.registry.EDTFCRecipeSerializers;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.capabilities.food.IFood;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.Arrays;

/** TFC output providers in ED's drying recipe type, inventory and JEI category. */
public class TFCDryingRecipe extends DryingRackRecipe {
    private final ItemStackProvider output;
    private final boolean driesFood;

    public TFCDryingRecipe(ResourceLocation id, String group, Ingredient ingredient,
                           ItemStackProvider output, float experience, int time) {
        super(id, group, ingredient, output.stack().get().copy(), experience, time);
        if (time < 1) throw new IllegalArgumentException("Drying time must be positive");
        this.output = output;
        this.driesFood = Arrays.stream(output.modifiers()).anyMatch(modifier -> modifier == DriedModifier.INSTANCE);
    }

    @Override
    public boolean matches(SimpleContainer input, Level level) {
        if (!super.matches(input, level)) return false;
        ItemStack stack = input.getItem(0);
        IFood food = FoodCapability.get(stack);
        if (food != null && food.isRotten()) return false;
        return !driesFood || food != null && !EDTFCFoodTraits.isDried(stack);
    }

    @Override
    public ItemStack assemble(SimpleContainer input, RegistryAccess registries) {
        ItemStack source = input.getItem(0);
        if (driesFood && ModList.get().isLoaded("firmalife")) {
            ItemStack firmalife = EDTFCFirmaLifeCompat.driedFoodOutput(source, registries);
            if (!firmalife.isEmpty()) return firmalife;
        }
        return output.getSingleStack(source);
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registries) {
        if (!driesFood) return super.getResultItem(registries);
        // Copy-input providers have no fixed result. Give JEI a real representative.
        ItemStack[] inputs = ingredient.getItems();
        if (inputs.length == 0) return ItemStack.EMPTY;
        ItemStack preview = inputs[0].copyWithCount(1);
        IFood food = FoodCapability.get(preview);
        if (food != null) {
            // TFC requests recipe previews during tag loading, before server configs exist.
            food.setNonDecaying();
            if (!EDTFCFoodTraits.isDried(preview)) food.getTraits().add(EDTFCFoodTraits.dried(preview.getItem()));
        }
        return preview;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return EDTFCRecipeSerializers.DRYING.get();
    }

    public static class Serializer implements RecipeSerializer<TFCDryingRecipe> {
        @Override
        public TFCDryingRecipe fromJson(ResourceLocation id, JsonObject json) {
            int time = GsonHelper.getAsInt(json, "cookingtime", 1000);
            if (time < 1) throw new JsonSyntaxException("Drying time must be positive");
            return new TFCDryingRecipe(id, GsonHelper.getAsString(json, "group", ""),
                    Ingredient.fromJson(json.get("ingredient")),
                    ItemStackProvider.fromJson(GsonHelper.getAsJsonObject(json, "result")),
                    GsonHelper.getAsFloat(json, "experience", 0), time);
        }

        @Override
        public TFCDryingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            return new TFCDryingRecipe(id, buffer.readUtf(), Ingredient.fromNetwork(buffer),
                    ItemStackProvider.fromNetwork(buffer), buffer.readFloat(), buffer.readVarInt());
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, TFCDryingRecipe recipe) {
            buffer.writeUtf(recipe.getGroup());
            recipe.getInput().toNetwork(buffer);
            recipe.output.toNetwork(buffer);
            buffer.writeFloat(recipe.experience);
            buffer.writeVarInt(recipe.getCookingTime());
        }
    }
}
