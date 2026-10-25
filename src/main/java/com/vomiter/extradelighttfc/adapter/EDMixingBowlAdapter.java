package com.vomiter.extradelighttfc.adapter;

import com.vomiter.extradelight.common.complex.workstations.mixingbowl.recipes.MixingBowlRecipe;
import com.vomiter.extradelighttfc.adapter.dynamic.DynamicFoodCalculator;
import com.vomiter.extradelighttfc.adapter.dynamic.DynamicFoodContext;
import com.vomiter.extradelighttfc.adapter.dynamic.FoodModifierRegistry;
import com.vomiter.extradelighttfc.adapter.dynamic.workstation.MixingDynamicFoodAdapter;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.capabilities.food.FoodHandler;
import net.dries007.tfc.common.capabilities.food.IFood;
import net.dries007.tfc.util.calendar.Calendars;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

public final class EDMixingBowlAdapter {

    private static final MixingDynamicFoodAdapter ADAPTER = new MixingDynamicFoodAdapter();

    private static final FoodModifierRegistry<DynamicFoodContext<MixingBowlRecipe>> MODIFIERS = new FoodModifierRegistry<>();

    private EDMixingBowlAdapter() {
    }

    public static void registerIndividual(
            FoodModifierRegistry.FoodModifier<DynamicFoodContext<MixingBowlRecipe>> modifier
    ) {
        MODIFIERS.registerIndividual(modifier);
    }

    public static void registerTotal(
            FoodModifierRegistry.FoodModifier<DynamicFoodContext<MixingBowlRecipe>> modifier
    ) {
        MODIFIERS.registerTotal(modifier);
    }

    public static ItemStack calcDynamic(
            ItemStackHandler inventory,
            IFluidHandler fluidHandler,
            ItemStack result,
            MixingBowlRecipe recipe,
            Level level
    ) {
        ItemStack output = DynamicFoodCalculator.calculate(
                result,
                inventory,
                recipe,
                fluidHandler,
                level,
                ADAPTER,
                MODIFIERS
        );
        return inheritAge(output, ADAPTER.getInputs(inventory, recipe));
    }

    public static ItemStack inheritAge(ItemStack result, List<ItemStack> sources) {
        IFood output = FoodCapability.get(result);
        if (output == null) return result;
        // Preserve the most-decayed ingredient's fraction, rather than refreshing a mixed meal.
        long now = Calendars.get().getTicks();
        double fraction = 0;
        for (ItemStack source : sources) {
            IFood food = FoodCapability.get(source);
            if (food == null) continue;
            if (food.isRotten()) return FoodCapability.setRotten(result);
            long life = food.getRottenDate() - food.getCreationDate();
            if (food.getCreationDate() >= 0 && life > 0 && food.getRottenDate() != FoodHandler.NEVER_DECAY_DATE) {
                fraction = Math.max(fraction, Math.max(0d, (double) (now - food.getCreationDate()) / life));
            }
        }
        FoodCapability.updateFoodDecayOnCreate(result);
        long life = output.getRottenDate() - output.getCreationDate();
        if (life > 0 && output.getRottenDate() != FoodHandler.NEVER_DECAY_DATE) {
            output.setCreationDate(now - (long) Math.ceil(fraction * life));
        }
        return result;
    }
}