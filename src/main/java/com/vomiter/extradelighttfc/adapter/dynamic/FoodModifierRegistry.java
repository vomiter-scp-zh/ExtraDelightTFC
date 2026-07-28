package com.vomiter.extradelighttfc.adapter.dynamic;

import com.vomiter.extradelighttfc.util.FoodDataBuilder;
import net.minecraft.world.item.crafting.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class FoodModifierRegistry<C extends DynamicFoodContext<? extends Recipe<?>>> {

    @FunctionalInterface
    public interface FoodModifier<C extends DynamicFoodContext> {

        FoodDataBuilder modify(
                FoodDataBuilder food,
                C context
        );
    }

    private final List<FoodModifier<C>> individualModifiers =
            new ArrayList<>();

    private final List<FoodModifier<C>> totalModifiers =
            new ArrayList<>();

    public void registerIndividual(FoodModifier<C> modifier) {
        individualModifiers.add(
                Objects.requireNonNull(modifier)
        );
    }

    public void registerTotal(FoodModifier<C> modifier) {
        totalModifiers.add(
                Objects.requireNonNull(modifier)
        );
    }

    public List<FoodModifier<C>> individualModifiers() {
        return List.copyOf(individualModifiers);
    }

    public List<FoodModifier<C>> totalModifiers() {
        return List.copyOf(totalModifiers);
    }
}