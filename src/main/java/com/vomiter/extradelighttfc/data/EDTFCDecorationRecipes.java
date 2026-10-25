package com.vomiter.extradelighttfc.data;

import com.google.gson.JsonObject;
import com.vomiter.extradelight.registry.ExtraDelightItems;
import com.vomiter.extradelighttfc.ExtraDelightTFC;
import com.vomiter.extradelighttfc.registry.EDTFCDecorations;
import com.vomiter.extradelighttfc.registry.EDTFCDecorations.Kind;
import com.vomiter.survivorsdelight.registry.SDBlocks;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.common.recipes.TFCRecipeSerializers;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public final class EDTFCDecorationRecipes {
    private static final RecipeCategory CATEGORY = RecipeCategory.BUILDING_BLOCKS;
    private EDTFCDecorationRecipes() {}

    public static void build(Consumer<FinishedRecipe> out) {
        for (Wood wood : Wood.values()) {
            Block slab = wood.getBlock(Wood.BlockType.SLAB).get();
            Block cabinet = SDBlocks.CABINETS.get(wood).get();
            var blocks = EDTFCDecorations.WOODS.get(wood);
            for (Kind kind : Kind.values()) {
                Block block = blocks.get(kind).get();
                switch (kind) {
                    case STEP_STOOL -> save(out, wood, block, ShapedRecipeBuilder.shaped(CATEGORY, block)
                            .pattern(" w ").pattern("s s").pattern("s s").define('w', slab).define('s', Tags.Items.RODS_WOODEN));
                    case KNIFE_BLOCK -> save(recipe -> out.accept(new DamageInputsRecipe(recipe)), wood, block,
                            ShapedRecipeBuilder.shaped(CATEGORY, block).pattern("wkw").define('w', slab).define('k', TFCTags.Items.KNIVES));
                    case FOOD_DISPLAY -> save(out, wood, block, ShapedRecipeBuilder.shaped(CATEGORY, block)
                            .pattern(" s ").pattern(" w ").pattern(" s ").define('w', slab).define('s', Tags.Items.RODS_WOODEN));
                    case FRUIT_BOWL -> save(out, wood, block, ShapedRecipeBuilder.shaped(CATEGORY, block)
                            .pattern("w w").pattern(" w ").pattern(" s ").define('w', slab).define('s', Tags.Items.RODS_WOODEN));
                    case DRIED_CORN_FENCE -> save(out, wood, block, ShapelessRecipeBuilder.shapeless(CATEGORY, block)
                            .requires(wood.getBlock(Wood.BlockType.FENCE).get()).requires(ExtraDelightItems.DRIED_CORN_HUSK.get()));
                    case HALF_CABINET, COUNTER_CABINET -> {
                        save(out, wood, block, ShapelessRecipeBuilder.shapeless(CATEGORY, block).requires(cabinet));
                        Kind other = kind == Kind.HALF_CABINET ? Kind.COUNTER_CABINET : Kind.HALF_CABINET;
                        save(out, wood, ShapelessRecipeBuilder.shapeless(CATEGORY, block).requires(blocks.get(other).get()),
                                recipeId(block).withSuffix("_from_" + other.path));
                    }
                    case SINK -> { /* Intentionally uncraftable. */ }
                }
            }
            EDTFCDecorations.WALLPAPERS.get(wood).forEach((color, handle) -> save(out, wood, handle.get(),
                    ShapelessRecipeBuilder.shapeless(CATEGORY, handle.get(), 4).requires(Items.PAPER, 4)
                            .requires(color.getTag()).requires(slab)));
        }
    }

    private static ResourceLocation recipeId(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block).withPrefix("decorations/");
    }

    private static void save(Consumer<FinishedRecipe> out, Wood wood, Block block, RecipeBuilder builder) {
        save(out, wood, builder, recipeId(block));
    }

    private static void save(Consumer<FinishedRecipe> out, Wood wood, RecipeBuilder builder, ResourceLocation id) {
        builder.unlockedBy("has_planks", InventoryChangeTrigger.TriggerInstance.hasItems(wood.getBlock(Wood.BlockType.PLANKS).get()))
                .save(recipe -> out.accept(new DecorationRecipe(recipe)), id);
    }

    /** Let the standard builders serialize the recipe, but retain existing advancement IDs. */
    private record DecorationRecipe(FinishedRecipe delegate) implements FinishedRecipe {
        @Override public void serializeRecipeData(JsonObject json) { delegate.serializeRecipeData(json); }
        @Override public ResourceLocation getId() { return delegate.getId(); }
        @Override public RecipeSerializer<?> getType() { return delegate.getType(); }
        @Override @Nullable public JsonObject serializeAdvancement() { return delegate.serializeAdvancement(); }
        @Override public ResourceLocation getAdvancementId() { return getId().withPrefix("recipes/"); }
    }

    /** TFC's delegate serializer needs only the builder-generated inner recipe. */
    private record DamageInputsRecipe(FinishedRecipe delegate) implements FinishedRecipe {
        @Override public void serializeRecipeData(JsonObject json) { json.add("recipe", delegate.serializeRecipe()); }
        @Override public ResourceLocation getId() { return delegate.getId(); }
        @Override public RecipeSerializer<?> getType() { return TFCRecipeSerializers.DAMAGE_INPUT_SHAPED_CRAFTING.get(); }
        @Override @Nullable public JsonObject serializeAdvancement() { return delegate.serializeAdvancement(); }
        @Override public ResourceLocation getAdvancementId() { return delegate.getAdvancementId(); }
    }
}
