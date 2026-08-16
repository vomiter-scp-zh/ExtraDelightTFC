package com.vomiter.extradelighttfc.data;

import com.vomiter.extradelight.registry.ExtraDelightItems;
import com.vomiter.extradelighttfc.ExtraDelightTFC;
import com.vomiter.extradelighttfc.util.FoodUtils;
import com.vomiter.survivorsdelight.data.recipe.SDCuttingRecipe;
import com.vomiter.survivorsdelight.data.recipe.builder.SDCuttingRecipeBuilder;
import com.vomiter.survivorsdelight.data.recipe.builder.SDJsonAdapters;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.items.Food;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.common.tag.CommonTags;

import java.util.function.Consumer;

public class EDTFCRecipeProvider extends RecipeProvider {
    public EDTFCRecipeProvider(PackOutput p_248933_) {
        super(p_248933_);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
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
