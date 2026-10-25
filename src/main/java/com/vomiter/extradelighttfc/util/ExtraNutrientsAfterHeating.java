package com.vomiter.extradelighttfc.util;

import com.vomiter.extradelighttfc.compat.EDTFCFirmaLifeCompat;
import com.vomiter.extradelighttfc.data.EDTFCTags;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.capabilities.food.FoodData;
import net.dries007.tfc.common.capabilities.food.FoodHandler;
import net.dries007.tfc.common.capabilities.food.IFood;
import net.dries007.tfc.common.items.Food;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.common.recipes.HeatingRecipe;
import net.dries007.tfc.common.recipes.inventory.ItemStackInventory;
import net.minecraft.core.RegistryAccess;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;

/** Adds only cooked-minus-raw nutrient deltas; hunger, water, saturation and dates are unchanged. */
public final class ExtraNutrientsAfterHeating {
    @FunctionalInterface
    public interface CookedRule {
        ItemStack cook(ItemStack ingredient, RegistryAccess access);
    }

    private record Rule(Predicate<ItemStack> matches, CookedRule cooked) {}
    public record HeatedFood(ItemStack stack, FoodData data) {}
    private record Portion(ItemStack stack, float[] delta, boolean changed) {}
    private static final String SCALE = "extradelighttfc:heating_ingredient_scale";
    private static final String APPLIED = "extradelighttfc:heating_nutrients_applied";
    private static final TagKey<Item> GRAINS = ItemTags.create(ResourceLocation.fromNamespaceAndPath("tfc", "foods/grains"));
    private static final List<Rule> RULES = new ArrayList<>();
    private static final ThreadLocal<Boolean> REFERENCE_ASSEMBLY = ThreadLocal.withInitial(() -> false);

    private ExtraNutrientsAfterHeating() {}

    /** Register during common setup. Later rules override earlier rules; empty means try the next rule. */
    public static void register(Predicate<ItemStack> matches, CookedRule cooked) {
        RULES.add(new Rule(Objects.requireNonNull(matches), Objects.requireNonNull(cooked)));
    }

    public static void registerDefaults() {
        registerBread(Food.BARLEY_FLOUR, Food.BARLEY_BREAD);
        registerBread(Food.MAIZE_FLOUR, Food.MAIZE_BREAD);
        registerBread(Food.OAT_FLOUR, Food.OAT_BREAD);
        registerBread(Food.RICE_FLOUR, Food.RICE_BREAD);
        registerBread(Food.RYE_FLOUR, Food.RYE_BREAD);
        registerBread(Food.WHEAT_FLOUR, Food.WHEAT_BREAD);
        registerBread(Food.BARLEY_GRAIN, Food.BARLEY_BREAD);
        registerBread(Food.MAIZE_GRAIN, Food.MAIZE_BREAD);
        registerBread(Food.OAT_GRAIN, Food.OAT_BREAD);
        registerBread(Food.RICE_GRAIN, Food.RICE_BREAD);
        registerBread(Food.RYE_GRAIN, Food.RYE_BREAD);
        registerBread(Food.WHEAT_GRAIN, Food.WHEAT_BREAD);

    }

    private static void registerBread(Food ingredient, Food bread) {
        register(stack -> stack.is(TFCItems.FOOD.get(ingredient).get()),
                (stack, access) -> new ItemStack(TFCItems.FOOD.get(bread).get()));
    }

    public static ItemStack getRegisteredCooked(ItemStack ingredient, RegistryAccess access) {
        for (int i = RULES.size() - 1; i >= 0; i--) {
            Rule rule = RULES.get(i);
            if (rule.matches.test(ingredient)) {
                ItemStack cooked = rule.cooked.cook(ingredient.copyWithCount(1), access);
                if (!cooked.isEmpty()) return cooked;
            }
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack getCooked(ItemStack ingredient, RegistryAccess access) {
        boolean previous = REFERENCE_ASSEMBLY.get();
        REFERENCE_ASSEMBLY.set(true);
        try {
            ItemStack custom = getRegisteredCooked(ingredient, access);
            if (!custom.isEmpty()) return custom;
            if (ModList.get().isLoaded("firmalife")) {
                ItemStack cooked = EDTFCFirmaLifeCompat.getOvenReferenceResult(ingredient, access);
                if (!cooked.isEmpty()) return cooked;
            }
            ItemStackInventory inventory = new ItemStackInventory(ingredient.copyWithCount(1));
            HeatingRecipe recipe = HeatingRecipe.getRecipe(inventory);
            return recipe == null ? ItemStack.EMPTY : recipe.assemble(inventory, access);
        } finally {
            if (previous) REFERENCE_ASSEMBLY.set(true);
            else REFERENCE_ASSEMBLY.remove();
        }
    }

    /** Store each displayed ingredient's contribution to ONE output item. */
    public static void recordScale(ItemStack result, float scale) {
        result.getOrCreateTag().putFloat(SCALE, scale);
        result.getOrCreateTag().remove(APPLIED);
    }

    /** Used by pure reshaping/baking: do not reset the existing proportional contribution. */
    public static void transferScale(ItemStack input, ItemStack result, float factor) {
        recordScale(result, (FoodCapability.get(input) instanceof FoodHandler.Dynamic ? scale(input) : 1f) * factor);
        if (input.hasTag() && input.getTag().getBoolean(APPLIED)) result.getOrCreateTag().putBoolean(APPLIED, true);
    }

    private static float scale(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains(SCALE) ? stack.getTag().getFloat(SCALE) : 1f;
    }

    /** ED Oven uses the same rules without replacing hunger/saturation with bread's values. */
    public static HeatedFood heatForCalculation(ItemStack ingredient, FoodData data, RegistryAccess access) {
        // Marked composite ingredients opt in. Grains retain ED Oven's pre-existing special rule.
        // Other basic inputs do not acquire a new generic nutrition pass here.
        if (!ingredient.is(EDTFCTags.EXTRA_NUTRIENT_AFTER_HEATING) && !ingredient.is(GRAINS)) {
            return new HeatedFood(ingredient.copy(), data);
        }
        Portion portion = cookPortion(ingredient, access);
        FoodData before = data == null ? FoodData.EMPTY : data;
        return new HeatedFood(portion.stack, portion.changed ? withDelta(before, portion.delta, 1f) : data);
    }

    /** One input item is consumed by assemble, irrespective of its inventory stack size. */
    public static ItemStack apply(ItemStack input, ItemStack result, RegistryAccess access) {
        return apply(input, result, access, 1f / Math.max(1, result.getCount()));
    }

    private static ItemStack apply(ItemStack input, ItemStack result, RegistryAccess access, float outputScale) {
        if (REFERENCE_ASSEMBLY.get() || result.isEmpty()
                || !input.is(EDTFCTags.EXTRA_NUTRIENT_AFTER_HEATING)) return result;
        if (!(FoodCapability.get(result) instanceof FoodHandler.Dynamic output)) return result;
        // The tag belongs to the item being heated, NOT its underlying raw materials.
        if (!(FoodCapability.get(input) instanceof FoodHandler.Dynamic source) || source.getIngredients().isEmpty()) return result;
        Portion portion = cookPortion(input.copyWithCount(1), access);
        if (portion.changed) {
            output.setFood(withDelta(output.getData(), portion.delta, outputScale));
            IFood cooked = FoodCapability.get(portion.stack);
            if (cooked instanceof FoodHandler.Dynamic dynamic) output.setIngredients(dynamic.getIngredients());
            else output.setIngredients(List.of(portion.stack));
        }
        // Preserve the processed state even if another recipe copies the original ingredient list.
        if (portion.changed || input.hasTag() && input.getTag().getBoolean(APPLIED)) {
            result.getOrCreateTag().putBoolean(APPLIED, true);
        }
        return result;
    }

    private static Portion cookPortion(ItemStack ingredient, RegistryAccess access) {
        IFood food = FoodCapability.get(ingredient);
        if (ingredient.hasTag() && ingredient.getTag().getBoolean(APPLIED)) {
            return new Portion(ingredient.copy(), new float[5], false);
        }
        if (food instanceof FoodHandler.Dynamic dynamic && !dynamic.getIngredients().isEmpty()) {
            float[] delta = new float[5];
            List<ItemStack> children = new ArrayList<>();
            boolean changed = false;
            for (ItemStack child : dynamic.getIngredients()) {
                Portion portion = cookPortion(child, access);
                children.add(portion.stack);
                for (int i = 0; i < delta.length; i++) delta[i] += portion.delta[i] * scale(ingredient);
                changed |= portion.changed;
            }
            ItemStack copy = ingredient.copy();
            if (changed && FoodCapability.get(copy) instanceof FoodHandler.Dynamic copiedFood) {
                copiedFood.setFood(withDelta(copiedFood.getData(), delta, 1f));
                copiedFood.setIngredients(children);
                copy.getOrCreateTag().putBoolean(APPLIED, true);
            }
            for (int i = 0; i < delta.length; i++) delta[i] *= ingredient.getCount();
            return new Portion(copy, delta, changed);
        }
        {
            ItemStack cooked = getCooked(ingredient, access);
            IFood cookedFood = FoodCapability.get(cooked);
            if (cookedFood != null) {
                float[] raw = food == null ? new float[5] : food.getData().nutrients();
                float[] delta = cookedFood.getData().nutrients();
                // A reference recipe may yield several items for one raw ingredient.
                for (int i = 0; i < delta.length; i++) {
                    delta[i] = (delta[i] * cooked.getCount() - raw[i]) * ingredient.getCount();
                }
                cooked.getOrCreateTag().putBoolean(APPLIED, true);
                return new Portion(cooked.copyWithCount(ingredient.getCount()), delta, true);
            }
        }
        return new Portion(ingredient.copy(), new float[5], false);
    }

    private static FoodData withDelta(FoodData before, float[] delta, float scale) {
        float[] nutrients = before.nutrients();
        for (int i = 0; i < nutrients.length; i++) nutrients[i] += delta[i] * scale;
        return FoodData.create(before.hunger(), before.water(), before.saturation(), nutrients, before.decayModifier());
    }
}
