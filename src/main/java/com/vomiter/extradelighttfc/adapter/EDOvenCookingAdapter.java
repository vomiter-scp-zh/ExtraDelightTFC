package com.vomiter.extradelighttfc.adapter;

import com.vomiter.extradelight.common.complex.workstations.oven.OvenBlock;
import com.vomiter.extradelight.common.complex.workstations.oven.OvenBlockEntity;
import com.vomiter.extradelight.common.complex.workstations.oven.recipes.OvenRecipe;
import com.vomiter.extradelighttfc.adapter.dynamic.DynamicFoodCalculator;
import com.vomiter.extradelighttfc.adapter.dynamic.DynamicFoodContext;
import com.vomiter.extradelighttfc.adapter.dynamic.FoodModifierRegistry;
import com.vomiter.extradelighttfc.adapter.dynamic.workstation.OvenDynamicFoodAdapter;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.capabilities.heat.HeatCapability;
import net.dries007.tfc.common.capabilities.heat.IHeat;
import net.dries007.tfc.common.recipes.HeatingRecipe;
import net.dries007.tfc.common.recipes.TFCRecipeTypes;
import net.dries007.tfc.common.recipes.inventory.ItemStackInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import vectorwing.farmersdelight.common.utility.ItemUtils;

import java.util.Optional;

public final class EDOvenCookingAdapter {

    private static final OvenDynamicFoodAdapter ADAPTER = new OvenDynamicFoodAdapter();

    private static final FoodModifierRegistry<DynamicFoodContext<OvenRecipe>> MODIFIERS = new FoodModifierRegistry<>();

    private EDOvenCookingAdapter() {
    }

    public static void registerIndividual(
            FoodModifierRegistry.FoodModifier<DynamicFoodContext<OvenRecipe>> modifier
    ) {
        MODIFIERS.registerIndividual(modifier);
    }

    public static void registerTotal(
            FoodModifierRegistry.FoodModifier<DynamicFoodContext<OvenRecipe>> modifier
    ) {
        MODIFIERS.registerTotal(modifier);
    }

    public static ItemStack calcDynamic(
            ItemStackHandler inventory,
            ItemStack result,
            OvenRecipe recipe,
            Level level
    ) {
        return DynamicFoodCalculator.calculate(
                result,
                inventory,
                recipe,
                null,
                level,
                ADAPTER,
                MODIFIERS
        );
    }

    public static void heatCooking(Level level, BlockPos pos, BlockState state, OvenBlockEntity oven){
        ItemStack stack0 = ItemStack.EMPTY;
        final int OUTPUT_SLOT = 11;

        boolean bulkOvenCooking = true;
        var inventory = oven.getInventory();
        for (int i = 0; i < 9; i++) {
            if(!bulkOvenCooking) continue;
            if(inventory.getStackInSlot(i).isEmpty()) continue;
            if(stack0.isEmpty()) stack0 = inventory.getStackInSlot(i);
            if(!ItemStack.isSameItem(stack0, inventory.getStackInSlot(i))){
                bulkOvenCooking = false;
            }
        }
        if(bulkOvenCooking){
            Optional<HeatingRecipe> heatingRecipe
                    = level.getRecipeManager().getRecipeFor(TFCRecipeTypes.HEATING.get(), new ItemStackInventory(stack0), level);
            IHeat heat0 = HeatCapability.get(stack0);

            if(heatingRecipe.isPresent() && heat0 != null){
                for (int i = 0; i < 9; i++) {
                    var stack = inventory.getStackInSlot(i);
                    if(stack.isEmpty()) continue;
                    IHeat heat = HeatCapability.get(stack);
                    if(heat == null) continue;
                    HeatCapability.addTemp(heat, Math.min(heatingRecipe.get().getTemperature() + 10, 400), 2);
                    if (heat.getTemperature() < heatingRecipe.get().getTemperature()) continue;
                    var result = heatingRecipe.get().assemble(new ItemStackInventory(stack.copyWithCount(1)), level.registryAccess());
                    boolean outputFinished = false;
                    if(inventory.getStackInSlot(OUTPUT_SLOT).isEmpty()) {
                        inventory.setStackInSlot(OUTPUT_SLOT, result);
                        outputFinished = true;
                    }
                    else {
                        Optional.ofNullable(HeatCapability.get(inventory.getStackInSlot(OUTPUT_SLOT))).ifPresent(
                                heatResult -> heatResult.setTemperature(HeatCapability.getTemperature(result)));
                    }
                    if (!outputFinished && inventory.getStackInSlot(OUTPUT_SLOT).getCount() < inventory.getStackInSlot(OUTPUT_SLOT).getMaxStackSize() && FoodCapability.areStacksStackableExceptCreationDate(inventory.getStackInSlot(OUTPUT_SLOT), result)){
                        inventory.getStackInSlot(OUTPUT_SLOT).grow(1);
                        outputFinished = true;
                    } else if (!outputFinished){
                        Direction direction = (state.getValue(OvenBlock.FACING)).getCounterClockWise();
                        double x = (double)pos.getX() + (double)0.5F + (double)direction.getStepX() * (double)0.25F;
                        double y = (double)pos.getY() + 0.7;
                        double z = (double)pos.getZ() + (double)0.5F + (double)direction.getStepZ() * (double)0.25F;
                        ItemUtils.spawnItemEntity(level, result, x, y, z, (double)((float)direction.getStepX() * 0.08F), (double)0.25F, (double)((float)direction.getStepZ() * 0.08F));
                    }
                    stack.shrink(1);
                    if(!stack.isEmpty()) {
                        heat.setTemperature(0);
                    }
                }
            }
        }
    }

}