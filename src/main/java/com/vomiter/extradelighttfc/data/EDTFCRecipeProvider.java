package com.vomiter.extradelighttfc.data;

import com.google.gson.JsonObject;
import com.vomiter.extradelight.registry.ExtraDelightBlocks;
import com.vomiter.extradelight.registry.ExtraDelightItems;
import com.vomiter.extradelight.registry.ExtraDelightRecipes;
import com.vomiter.extradelighttfc.ExtraDelightTFC;
import com.vomiter.extradelighttfc.util.FoodUtils;
import com.vomiter.survivorsdelight.data.recipe.SDCuttingRecipe;
import com.vomiter.survivorsdelight.data.recipe.builder.SDCuttingRecipeBuilder;
import com.vomiter.survivorsdelight.data.recipe.builder.SDJsonAdapters;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.fluids.TFCFluids;
import net.dries007.tfc.common.items.Food;
import net.dries007.tfc.common.items.Powder;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.common.recipes.outputs.CopyFoodModifier;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.tag.CommonTags;

import java.util.function.Consumer;

public class EDTFCRecipeProvider extends RecipeProvider {
    public static final ResourceLocation EVAPORATOR_SALT_LOOT = ExtraDelightTFC.modLoc("evaporator/salt");
    private static final TagKey<Item> BLACK_STEEL_RODS = ItemTags.create(
            ResourceLocation.fromNamespaceAndPath("forge", "rods/black_steel"));
    private static final TagKey<Item> BLACK_STEEL_SHEETS = ItemTags.create(
            ResourceLocation.fromNamespaceAndPath("forge", "sheets/black_steel"));
    public EDTFCRecipeProvider(PackOutput p_248933_) {
        super(p_248933_);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        EDTFCMortarRecipes.build(consumer);
        evaporator(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ExtraDelightItems.JUICER.get())
                        .pattern("R")
                        .pattern("B")
                        .pattern("S")
                                .define('R', ItemTags.create(ResourceLocation.fromNamespaceAndPath("forge", "rods/copper")))
                                .define('B', ItemTags.create(ResourceLocation.fromNamespaceAndPath("tfc", "barrels")))
                                .define('S', ItemTags.create(ResourceLocation.fromNamespaceAndPath("forge", "sheets/copper")))
                                        .unlockedBy("anything", has(Items.AIR))
                                                .save(consumer, ExtraDelightTFC.modLoc(ExtraDelightItems.JUICER.getId().getPath()));


        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ExtraDelightItems.MIXING_BOWL.get())
                        .pattern("PGP")
                        .pattern(" B ")
                        .define('P', ItemTags.create(ResourceLocation.fromNamespaceAndPath("tfc", "lumber")))
                        .define('G', TFCItems.GLUE.get())
                        .define('B', Items.BOWL)
                        .unlockedBy("get_bowl", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BOWL))
                .save(consumer, ExtraDelightTFC.modLoc(ExtraDelightItems.MIXING_BOWL.getId().getPath()));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ExtraDelightItems.OVEN.get())
                .pattern("SHS")
                .pattern("B B")
                .pattern("HHH")
                .define('S', Blocks.BRICK_STAIRS)
                .define('H', Blocks.BRICK_SLAB)
                .define('B', Blocks.BRICKS)
                .unlockedBy("get_bricks", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BRICKS))
                .save(consumer, ExtraDelightTFC.modLoc(ExtraDelightItems.OVEN.getId().getPath()));


        slice(Food.ONION, ExtraDelightItems.SLICED_ONION, consumer);
        slice(Food.TOMATO, ExtraDelightItems.SLICED_TOMATO, consumer);
        slice(new Food[]{Food.RED_APPLE, Food.GREEN_APPLE}, ExtraDelightItems.SLICED_APPLE, consumer);
        slice(Food.POTATO, ExtraDelightItems.SLICED_POTATO, consumer);
        grate(Food.POTATO, ExtraDelightItems.GRATED_POTATO, consumer);
        grate(Food.CARROT, ExtraDelightItems.GRATED_CARROT, consumer);
        grate(Food.GARLIC, ExtraDelightItems.GRATED_GARLIC, consumer);
        grate(ItemTags.create(ResourceLocation.fromNamespaceAndPath("tfc", "foods/breads")), ExtraDelightItems.BREAD_CRUMBS, consumer);
    }

    private void evaporator(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ExtraDelightBlocks.EVAPORATOR.get())
                .pattern("R R").pattern("SSS")
                .define('R', BLACK_STEEL_RODS).define('S', BLACK_STEEL_SHEETS)
                .unlockedBy("has_black_steel_sheet", has(BLACK_STEEL_SHEETS))
                .save(consumer, ExtraDelightTFC.modLoc("crafting/evaporator"));
        consumer.accept(new EvaporatorResult());
    }

    private record EvaporatorResult() implements FinishedRecipe {
        @Override
        public void serializeRecipeData(JsonObject json) {
            JsonObject fluid = new JsonObject();
            fluid.addProperty("fluid", TFCFluids.SALT_WATER.source().getId().toString());
            fluid.addProperty("amount", 1000);
            json.add("fluid", fluid);
            json.addProperty("time", 10000);
            json.addProperty("loottable", EVAPORATOR_SALT_LOOT.toString());
            json.addProperty("display_block", ExtraDelightBlocks.SALT_BLOCK.getId().toString());
            JsonObject result = new JsonObject();
            result.addProperty("id", ForgeRegistries.ITEMS.getKey(TFCItems.POWDERS.get(Powder.SALT).get()).toString());
            result.addProperty("count", 1);
            json.add("outItem", result);
        }

        @Override public ResourceLocation getId() { return ExtraDelightTFC.modLoc("evaporator/salt_water"); }
        @Override public RecipeSerializer<?> getType() { return ExtraDelightRecipes.EVAPORATOR_SERIALIZER.get(); }
        @Override public @Nullable JsonObject serializeAdvancement() { return null; }
        @Override public @Nullable ResourceLocation getAdvancementId() { return null; }
    }

    private void grate(TagKey<Item> tag, RegistryObject<Item> grated, Consumer<FinishedRecipe> consumer){
        var knife = Ingredient.of(ExtraDelightItems.GRATER.get());
        cutting(Ingredient.of(tag), knife, grated, 4, consumer);
    }


    private void grate(Food food, RegistryObject<Item> grated, Consumer<FinishedRecipe> consumer){
        var knife = Ingredient.of(ExtraDelightItems.GRATER.get());
        cutting(Ingredient.of(FoodUtils.getFoodItem(food)), knife, grated, 4, consumer);
    }

    private void slice(Food[] food, RegistryObject<Item> sliced, Consumer<FinishedRecipe> consumer){
        var knife = Ingredient.of(CommonTags.Items.TOOLS_KNIVES);
        cutting(Ingredient.of(FoodUtils.getFoodItem(food)), knife, sliced, 4, consumer);
    }


    private void slice(Food food, RegistryObject<Item> sliced, Consumer<FinishedRecipe> consumer){
        var knife = Ingredient.of(CommonTags.Items.TOOLS_KNIVES);
        cutting(Ingredient.of(FoodUtils.getFoodItem(food)), knife, sliced, 4, consumer);
    }

    private void cutting(Ingredient ingredient, Ingredient tool, RegistryObject<Item> result, int count, Consumer<FinishedRecipe> consumer){
        SDCuttingRecipeBuilder
                .cutting()
                .notRotten(ingredient)
                .tool(tool)
                .result(SDJsonAdapters.writeISP(
                        ItemStackProvider.of(
                                new ItemStack(result.get(), count),
                                CopyFoodModifier.INSTANCE
                        )
                ))
                .build(consumer, ExtraDelightTFC.modLoc("cutting/" + result.getId().getPath()));

    }
}
