package com.vomiter.extradelighttfc.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.vomiter.extradelight.registry.ExtraDelightItems;
import com.vomiter.extradelighttfc.ExtraDelightTFC;
import com.vomiter.survivorsdelight.data.recipe.builder.SDCuttingRecipeBuilder;
import com.vomiter.survivorsdelight.data.recipe.builder.SDFDCookingPotRecipeBuilder;
import com.vomiter.survivorsdelight.data.recipe.builder.SDJsonAdapters;
import java.util.function.Consumer;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.items.Food;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.common.recipes.TFCRecipeSerializers;
import net.dries007.tfc.common.recipes.ingredients.NotRottenIngredient;
import net.dries007.tfc.common.recipes.outputs.CopyFoodModifier;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

public final class CornRecipes {
    public static final ResourceLocation MAIZE_CUTTING = ResourceLocation.fromNamespaceAndPath("tfc", "crafting/maize_cutting");

    private CornRecipes() {}

    public static void build(Consumer<FinishedRecipe> consumer) {
        Ingredient maize = Ingredient.of(TFCItems.FOOD.get(Food.MAIZE).get());
        Ingredient grain = Ingredient.of(TFCItems.FOOD.get(Food.MAIZE_GRAIN).get());
        Ingredient knife = Ingredient.of(TFCTags.Items.KNIVES);
        ItemStackProvider grainResult = ItemStackProvider.of(
                new ItemStack(TFCItems.FOOD.get(Food.MAIZE_GRAIN).get()), CopyFoodModifier.INSTANCE);

        SDCuttingRecipeBuilder.cutting().notRotten(maize).tool(knife)
                .result(SDJsonAdapters.writeISP(grainResult))
                .result(ExtraDelightItems.CORN_COB.get(), 1)
                .build(consumer, ExtraDelightTFC.modLoc("cutting/maize"));

        // Preserve TFC's not-rotten input, food age and knife durability; replace its straw by a cob.
        JsonObject cutting = new JsonObject();
        JsonArray extraProducts = new JsonArray();
        extraProducts.add(SDJsonAdapters.stackToJson(new ItemStack(ExtraDelightItems.CORN_COB.get())));
        cutting.add("extra_products", extraProducts);
        JsonObject damage = new JsonObject();
        damage.addProperty("type", TFCRecipeSerializers.DAMAGE_INPUTS_SHAPELESS_CRAFTING.getId().toString());
        JsonObject crafting = new JsonObject();
        crafting.addProperty("type", TFCRecipeSerializers.ADVANCED_SHAPELESS_CRAFTING.getId().toString());
        JsonArray ingredients = new JsonArray();
        ingredients.add(NotRottenIngredient.of(maize).toJson());
        ingredients.add(knife.toJson());
        crafting.add("ingredients", ingredients);
        crafting.add("primary_ingredient", maize.toJson());
        crafting.add("result", SDJsonAdapters.writeISP(grainResult));
        damage.add("recipe", crafting);
        cutting.add("recipe", damage);
        consumer.accept(new Result(MAIZE_CUTTING, TFCRecipeSerializers.EXTRA_PRODUCTS_SHAPELESS_CRAFTING.get(), cutting));

        SDFDCookingPotRecipeBuilder.cookingPotRecipe(ExtraDelightItems.COOKED_CORN.get(), 1, 100, 0.35F, Items.BOWL)
                .addIngredientNotRotten(grain)
                .fluid(Fluids.WATER, 100)
                .build(consumer, ExtraDelightTFC.modLoc("cooking/cooked_corn"));

        JsonObject popcorn = new JsonObject();
        popcorn.add("ingredient", NotRottenIngredient.of(grain).toJson());
        popcorn.add("result_item", SDJsonAdapters.writeISP(ItemStackProvider.of(
                new ItemStack(ExtraDelightItems.POPCORN.get()), CopyFoodModifier.INSTANCE)));
        popcorn.addProperty("temperature", 200);
        consumer.accept(new Result(ExtraDelightTFC.modLoc("heating/popcorn"), TFCRecipeSerializers.HEATING.get(), popcorn));
    }

    private record Result(ResourceLocation id, RecipeSerializer<?> type, JsonObject data) implements FinishedRecipe {
        @Override public void serializeRecipeData(JsonObject json) { data.entrySet().forEach(entry -> json.add(entry.getKey(), entry.getValue())); }
        @Override public ResourceLocation getId() { return id; }
        @Override public RecipeSerializer<?> getType() { return type; }
        @Override public @Nullable JsonObject serializeAdvancement() { return null; }
        @Override public @Nullable ResourceLocation getAdvancementId() { return null; }
    }
}
