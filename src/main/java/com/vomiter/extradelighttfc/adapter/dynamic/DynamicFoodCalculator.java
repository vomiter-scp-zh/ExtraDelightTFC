package com.vomiter.extradelighttfc.adapter.dynamic;

import com.vomiter.extradelighttfc.ExtraDelightTFC;
import com.vomiter.extradelighttfc.util.FoodDataBuilder;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.capabilities.food.FoodData;
import net.dries007.tfc.common.capabilities.food.FoodHandler;
import net.dries007.tfc.common.capabilities.food.IFood;
import net.dries007.tfc.util.Drinkable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public final class DynamicFoodCalculator {

    private DynamicFoodCalculator() {
    }

    public static <S, F, R extends Recipe<?>, C extends DynamicFoodContext<R>> ItemStack calculate(
            ItemStack result,
            S source,
            R recipe,
            F fluid,
            Level level,
            DynamicFoodAdapter<S, F, R, C> adapter,
            FoodModifierRegistry<C> modifierRegistry
    ) {
        adapter.applyContainer(
                result,
                source,
                recipe
        );

        if (result.isEmpty()) {
            return result;
        }

        IFood resultFood = FoodCapability.get(result);

        if (!(resultFood instanceof FoodHandler.Dynamic dynamic)) {
            return result;
        }

        List<ItemStack> inputs = normalizeInputs(adapter.getInputs(source, recipe));
        List<FluidStack> inputFluids = adapter.getInputFluids(fluid, recipe);

        List<ProcessedFood> processedFoods =
                collectProcessedFoods(
                        inputs,
                        inputFluids,
                        source,
                        fluid,
                        recipe,
                        level,
                        adapter,
                        modifierRegistry
                );

        FoodDataBuilder builder = aggregateFoods(
                processedFoods,
                inputFluids,
                source,
                recipe,
                adapter
        );

        int resultCount = Math.max(
                1,
                result.getCount()
        );

        builder.mul(1f / resultCount);

        C totalContext = adapter.createTotalContext(
                inputs,
                inputFluids,
                result.copy(),
                source,
                fluid,
                recipe
        );

        builder = applyModifiers(
                builder,
                totalContext,
                modifierRegistry.totalModifiers(),
                "Total food modifier returned null"
        );

        dynamic.setFood(builder.build());

        dynamic.setIngredients(
                processedFoods.stream()
                        .map(ProcessedFood::stack)
                        .map(ItemStack::copy)
                        .toList()
        );

        dynamic.setCreationDate(
                FoodCapability.getRoundedCreationDate()
        );

        return result;
    }

    private static List<ItemStack> normalizeInputs(
            List<ItemStack> inputs
    ) {
        if (inputs == null || inputs.isEmpty()) {
            return List.of();
        }

        return inputs.stream()
                .filter(Objects::nonNull)
                .filter(stack -> !stack.isEmpty())
                .map(stack -> stack.copyWithCount(1))
                .sorted(Comparator.comparing(
                        DynamicFoodCalculator::getItemId
                ))
                .toList();
    }

    private static <S, F, R extends Recipe<?>, C extends DynamicFoodContext<R>> List<ProcessedFood> collectProcessedFoods(
            List<ItemStack> inputs,
            List<FluidStack> inputFluids,
            S source,
            F fluid,
            R recipe,
            Level level,
            DynamicFoodAdapter<S, F, R, C> adapter,
            FoodModifierRegistry<C> modifierRegistry
    ) {
        List<ProcessedFood> processedFoods =
                new ArrayList<>();

        for (ItemStack input : inputs) {
            ItemStack transformed =
                    adapter.transformIngredient(
                            input.copy(),
                            source,
                            recipe,
                            level
                    );

            ItemStack foodStack =
                    transformed == null || transformed.isEmpty()
                            ? input.copy()
                            : transformed.copyWithCount(1);

            C context =
                    adapter.createIndividualContext(
                            inputs,
                            inputFluids,
                            foodStack.copy(),
                            source,
                            fluid,
                            recipe
                    );

            FoodData foodData = calculateIndividualFood(
                    foodStack,
                    context,
                    modifierRegistry
                            .individualModifiers()
            );

            if (foodData == null) {
                continue;
            }

            processedFoods.add(
                    new ProcessedFood(
                            foodStack.copy(),
                            foodData
                    )
            );
        }

        return processedFoods;
    }

    private static FoodDataBuilder translateDrinkable(FluidStack fluid){
        var drinkable = Drinkable.get(fluid.getFluid());
        if(drinkable == null) return null;
        if(drinkable.getFoodStats() == null) return null;
        return FoodDataBuilder.from(drinkable.getFoodStats()).mul((float) fluid.getAmount() / 25);
    }

    private static FoodDataBuilder aggregateDrinkables(List<FluidStack> fluids){
        var drinkables = fluids.stream().map(DynamicFoodCalculator::translateDrinkable).filter(Objects::nonNull);
        var food = new FoodDataBuilder();
        drinkables.forEach(food::addBuilder);

        return food;
    }

    private static <S, F, R extends Recipe<?>, C extends DynamicFoodContext<R>> FoodDataBuilder aggregateFoods(
            List<ProcessedFood> foods,
            List<FluidStack> inputFluids,
            S source,
            R recipe,
            DynamicFoodAdapter<S, F, R, C> adapter
    ) {
        FoodDataBuilder builder =
                FoodDataBuilder.create()
                        .decayModifier(
                                adapter.getDecayModifier(
                                        source,
                                        recipe
                                )
                        );

        float balanceMultiplier =
                adapter.getBalanceMultiplier(
                        foods.size(),
                        source,
                        recipe
                );

        for (ProcessedFood food : foods) {
            builder.addBuilder(
                    FoodDataBuilder.from(food.data())
                            .mul(balanceMultiplier)
            );
        }

        builder.addBuilder(aggregateDrinkables(inputFluids));

        return builder;
    }

    private static <R, C extends DynamicFoodContext<R>> FoodData calculateIndividualFood(
            ItemStack ingredient,
            C context,
            List<FoodModifierRegistry.FoodModifier<C>> modifiers
    ) {
        IFood food = FoodCapability.get(ingredient);

        FoodDataBuilder builder = food == null
                ? FoodDataBuilder.create()
                : FoodDataBuilder.from(food.getData());

        builder = applyModifiers(
                builder,
                context,
                modifiers,
                "Individual food modifier returned null"
        );

        return builder.isEmpty()
                ? null
                : builder.build();
    }

    private static <R, C extends DynamicFoodContext<R>> FoodDataBuilder applyModifiers(
            FoodDataBuilder builder,
            C context,
            List<FoodModifierRegistry.FoodModifier<C>> modifiers,
            String nullMessage
    ) {
        for (FoodModifierRegistry.FoodModifier<C> modifier
                : modifiers) {
            builder = Objects.requireNonNull(
                    modifier.modify(builder, context),
                    nullMessage
            );
        }

        return builder;
    }

    private static ResourceLocation getItemId(ItemStack stack) {
        return Objects.requireNonNull(
                ForgeRegistries.ITEMS.getKey(
                        stack.getItem()
                ),
                () -> "Unregistered item: "
                        + stack.getItem()
        );
    }

    private record ProcessedFood(ItemStack stack, FoodData data) {}
}