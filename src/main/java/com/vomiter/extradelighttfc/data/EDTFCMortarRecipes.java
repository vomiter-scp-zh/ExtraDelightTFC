package com.vomiter.extradelighttfc.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.vomiter.extradelight.registry.ExtraDelightFluids;
import com.vomiter.extradelight.registry.ExtraDelightItems;
import com.vomiter.extradelighttfc.ExtraDelightTFC;
import com.vomiter.extradelighttfc.registry.EDTFCItems;
import com.vomiter.extradelighttfc.registry.EDTFCRecipeSerializers;
import com.vomiter.survivorsdelight.data.recipe.builder.SDJsonAdapters;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.crop.Crop;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.capabilities.forge.ForgeRule;
import net.dries007.tfc.common.fluids.SimpleFluid;
import net.dries007.tfc.common.fluids.TFCFluids;
import net.dries007.tfc.common.items.Food;
import net.dries007.tfc.common.items.Powder;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.common.recipes.TFCRecipeSerializers;
import net.dries007.tfc.common.recipes.ingredients.NotRottenIngredient;
import net.dries007.tfc.common.recipes.outputs.CopyFoodModifier;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.dries007.tfc.util.Metal;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Locale;
import java.util.function.Consumer;

public final class EDTFCMortarRecipes {
    private static final TagKey<Item> HARDENED_ROCK = ItemTags.create(
            ResourceLocation.fromNamespaceAndPath("tfc", "rock/hardened"));
    private static final Crop[] GRAINS = {Crop.BARLEY, Crop.MAIZE, Crop.OAT, Crop.RYE, Crop.RICE, Crop.WHEAT};

    private EDTFCMortarRecipes() {}

    public static void build(Consumer<FinishedRecipe> consumer) {
        InventoryChangeTrigger.TriggerInstance hasRock = InventoryChangeTrigger.TriggerInstance.hasItems(
                ItemPredicate.Builder.item().of(TFCTags.Items.ROCK_KNAPPING).build());
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ExtraDelightItems.MORTAR_STONE.get())
                .pattern("R R").pattern(" R ")
                .define('R', HARDENED_ROCK)
                .unlockedBy("has_rock", hasRock)
                .save(consumer, ExtraDelightTFC.modLoc("crafting/mortar_stone"));
        pestles(consumer);

        for (Crop crop : GRAINS) {
            String name = crop.name().toLowerCase(Locale.ROOT);
            save(consumer, "oil/" + name, Ingredient.of(TFCItems.CROP_SEEDS.get(crop).get()),
                    ItemStackProvider.empty(), new FluidStack(ExtraDelightFluids.OIL.FLUID.get(), 50), 4);
            Food grain = Food.valueOf(crop.name() + "_GRAIN");
            Food flour = Food.valueOf(crop.name() + "_FLOUR");
            save(consumer, "flour/" + name,
                    NotRottenIngredient.of(Ingredient.of(TFCItems.FOOD.get(grain).get())),
                    ItemStackProvider.of(new ItemStack(TFCItems.FOOD.get(flour).get()), CopyFoodModifier.INSTANCE),
                    FluidStack.EMPTY, 8);
        }
        save(consumer, "olive", NotRottenIngredient.of(Ingredient.of(TFCItems.FOOD.get(Food.OLIVE).get())),
                ItemStackProvider.of(new ItemStack(TFCItems.OLIVE_PASTE.get(), 2)),
                new FluidStack(TFCFluids.SIMPLE_FLUIDS.get(SimpleFluid.OLIVE_OIL_WATER).source().get(), 100), 4);
        save(consumer, "salt", Ingredient.of(TFCItems.ORES.get(Ore.HALITE).get()),
                ItemStackProvider.of(new ItemStack(TFCItems.POWDERS.get(Powder.SALT).get(), 4)), FluidStack.EMPTY, 8);
        save(consumer, "bone_meal", Ingredient.of(Items.BONE),
                ItemStackProvider.of(new ItemStack(Items.BONE_MEAL, 3)), FluidStack.EMPTY, 8);
        save(consumer, "blaze_powder", Ingredient.of(Items.BLAZE_ROD),
                ItemStackProvider.of(new ItemStack(Items.BLAZE_POWDER, 3)), FluidStack.EMPTY, 8);

        save(consumer, "agar_agar", Ingredient.of(ExtraDelightItems.AGAR_SHEETS.get()),
                ItemStackProvider.of(new ItemStack(ExtraDelightItems.AGAR_AGAR.get())), FluidStack.EMPTY, 8);

        // These are exactly the thirteen plant-dye inputs defined by TFC's quern.
        for (DyeColor color : EnumSet.complementOf(EnumSet.of(DyeColor.BLACK, DyeColor.CYAN, DyeColor.GRAY))) {
            save(consumer, "dye/" + color.getName(),
                    Ingredient.of(ItemTags.create(ResourceLocation.fromNamespaceAndPath("tfc", "makes_" + color.getName() + "_dye"))),
                    ItemStackProvider.of(new ItemStack(DyeItem.byColor(color), 2)), FluidStack.EMPTY, 4);
        }
    }

    private static void pestles(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, EDTFCItems.STONE_PESTLE.get())
                .pattern("R ").pattern(" S")
                .define('R', HARDENED_ROCK).define('S', Items.STICK)
                .unlockedBy("has_hardened_rock", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ItemPredicate.Builder.item().of(HARDENED_ROCK).build()))
                .save(consumer, ExtraDelightTFC.modLoc("crafting/pestle_stone"));
        EDTFCItems.METAL_PESTLES.forEach((metal, item) -> consumer.accept(new AnvilResult(metal, item)));
    }

    private record AnvilResult(Metal.Default metal, RegistryObject<Item> item) implements FinishedRecipe {
        @Override
        public void serializeRecipeData(JsonObject json) {
            TagKey<Item> ingots = ItemTags.create(ResourceLocation.fromNamespaceAndPath("forge", "ingots/" + metal.getSerializedName()));
            json.add("input", Ingredient.of(ingots).toJson());
            JsonObject result = new JsonObject();
            result.addProperty("item", item.getId().toString());
            json.add("result", result);
            json.addProperty("tier", metal.metalTier().ordinal());
            JsonArray rules = new JsonArray();
            rules.add(ForgeRule.PUNCH_LAST.name().toLowerCase(Locale.ROOT));
            rules.add(ForgeRule.SHRINK_NOT_LAST.name().toLowerCase(Locale.ROOT));
            json.add("rules", rules);
            json.addProperty("apply_forging_bonus", true);
        }

        @Override public ResourceLocation getId() { return ExtraDelightTFC.modLoc("anvil/pestle/" + metal.getSerializedName()); }
        @Override public RecipeSerializer<?> getType() { return TFCRecipeSerializers.ANVIL.get(); }
        @Override public @Nullable JsonObject serializeAdvancement() { return null; }
        @Override public @Nullable ResourceLocation getAdvancementId() { return null; }
    }

    private static void save(Consumer<FinishedRecipe> consumer, String name, Ingredient ingredient,
                             ItemStackProvider output, FluidStack fluid, int grinds) {
        consumer.accept(new Result(ExtraDelightTFC.modLoc("mortar/" + name), ingredient, output, fluid, grinds));
    }

    private record Result(ResourceLocation id, Ingredient ingredient, ItemStackProvider output,
                          FluidStack fluid, int grinds) implements FinishedRecipe {
        @Override
        public void serializeRecipeData(JsonObject json) {
            json.add("ingredient", ingredient.toJson());
            if (!output.stack().get().isEmpty() || output.modifiers().length > 0) {
                json.add("result", SDJsonAdapters.writeISP(output));
            }
            if (!fluid.isEmpty()) {
                JsonObject fluidJson = new JsonObject();
                fluidJson.addProperty("fluid", ForgeRegistries.FLUIDS.getKey(fluid.getFluid()).toString());
                fluidJson.addProperty("amount", fluid.getAmount());
                json.add("fluid_output", fluidJson);
            }
            json.addProperty("grinds", grinds);
        }

        @Override public ResourceLocation getId() { return id; }
        @Override public RecipeSerializer<?> getType() { return EDTFCRecipeSerializers.MORTAR.get(); }
        @Override public @Nullable JsonObject serializeAdvancement() { return null; }
        @Override public @Nullable ResourceLocation getAdvancementId() { return null; }
    }
}
