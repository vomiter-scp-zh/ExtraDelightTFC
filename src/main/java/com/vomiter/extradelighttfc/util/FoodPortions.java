package com.vomiter.extradelighttfc.util;

import java.util.List;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.capabilities.food.FoodHandler;
import net.minecraft.world.item.ItemStack;

public final class FoodPortions {
    private FoodPortions() {}

    /** One consumed input item -> result.getCount() individually edible portions. */
    public static ItemStack transfer(ItemStack input, ItemStack result) {
        var source = FoodCapability.get(input);
        if (source == null || result.isEmpty() || !(FoodCapability.get(result) instanceof FoodHandler.Dynamic output)) return result;
        if (source.isRotten()) return ItemStack.EMPTY;
        float factor = 1f / result.getCount();
        output.setFood(FoodDataBuilder.from(source.getData()).mul(factor).build());
        output.setIngredients(source instanceof FoodHandler.Dynamic dynamic && !dynamic.getIngredients().isEmpty()
                ? dynamic.getIngredients().stream().map(ItemStack::copy).toList()
                : List.of(input.copyWithCount(1)));
        ExtraNutrientsAfterHeating.transferScale(input, result, factor);
        // Data (including decay modifier) is inherited unchanged except for portion size,
        // so preserve the exact creation date instead of rounding it again.
        output.getTraits().clear();
        output.getTraits().addAll(source.getTraits());
        output.setCreationDate(source.getCreationDate());
        return result;
    }
}
