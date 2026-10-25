package com.vomiter.extradelighttfc.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.vomiter.extradelighttfc.ExtraDelightTFC;
import com.vomiter.extradelighttfc.registry.EDTFCItems;
import java.util.Arrays;
import java.util.Locale;
import java.util.function.Consumer;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.rock.RockCategory;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.common.capabilities.forge.ForgeRule;
import net.dries007.tfc.common.recipes.TFCRecipeSerializers;
import net.dries007.tfc.util.Metal;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

public final class EDTFCSpoonRecipes {
    private EDTFCSpoonRecipes() {}

    public static void build(Consumer<FinishedRecipe> out) {
        Item[] headItems = Arrays.stream(RockCategory.values())
                .map(category -> TFCItems.ROCK_TOOLS.get(category).get(RockCategory.ItemType.SHOVEL_HEAD).get()).toArray(Item[]::new);
        Ingredient heads = Ingredient.of(headItems);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, EDTFCItems.STONE_SPOON_HEAD.get())
                .requires(heads).requires(TFCTags.Items.HAMMERS)
                .unlockedBy("has_shovel_head", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ItemPredicate.Builder.item().of(headItems).build()))
                .save(recipe -> out.accept(new DamageInputsResult(recipe)), ExtraDelightTFC.modLoc("crafting/spoon_head_stone"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, EDTFCItems.STONE_SPOON.get())
                .requires(EDTFCItems.STONE_SPOON_HEAD.get()).requires(Items.STICK)
                .unlockedBy("has_spoon_head", InventoryChangeTrigger.TriggerInstance.hasItems(EDTFCItems.STONE_SPOON_HEAD.get()))
                .save(out, ExtraDelightTFC.modLoc("crafting/spoon_stone"));
        EDTFCItems.METAL_SPOONS.forEach((metal, item) -> out.accept(new AnvilResult(metal, item)));
    }

    private record DamageInputsResult(FinishedRecipe recipe) implements FinishedRecipe {
        @Override public void serializeRecipeData(JsonObject json) { json.add("recipe", recipe.serializeRecipe()); }
        @Override public ResourceLocation getId() { return recipe.getId(); }
        @Override public RecipeSerializer<?> getType() { return TFCRecipeSerializers.DAMAGE_INPUTS_SHAPELESS_CRAFTING.get(); }
        @Override public @Nullable JsonObject serializeAdvancement() { return recipe.serializeAdvancement(); }
        @Override public @Nullable ResourceLocation getAdvancementId() { return recipe.getAdvancementId(); }
    }

    private record AnvilResult(Metal.Default metal, RegistryObject<Item> item) implements FinishedRecipe {
        @Override public void serializeRecipeData(JsonObject json) {
            TagKey<Item> rods = ItemTags.create(ResourceLocation.fromNamespaceAndPath("forge", "rods/" + metal.getSerializedName()));
            json.add("input", Ingredient.of(rods).toJson());
            JsonObject result = new JsonObject(); result.addProperty("item", item.getId().toString());
            json.add("result", result);
            json.addProperty("tier", metal.metalTier().ordinal());
            JsonArray rules = new JsonArray();
            rules.add(ForgeRule.PUNCH_LAST.name().toLowerCase(Locale.ROOT));
            rules.add(ForgeRule.SHRINK_NOT_LAST.name().toLowerCase(Locale.ROOT));
            json.add("rules", rules);
            json.addProperty("apply_forging_bonus", true);
        }
        @Override public ResourceLocation getId() { return ExtraDelightTFC.modLoc("anvil/spoon/" + metal.getSerializedName()); }
        @Override public RecipeSerializer<?> getType() { return TFCRecipeSerializers.ANVIL.get(); }
        @Override public @Nullable JsonObject serializeAdvancement() { return null; }
        @Override public @Nullable ResourceLocation getAdvancementId() { return null; }
    }
}
