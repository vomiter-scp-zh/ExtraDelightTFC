package com.vomiter.extradelighttfc.compat;

import com.eerussianguy.firmalife.common.items.FLFoodTraits;
import com.eerussianguy.firmalife.common.recipes.DryingRecipe;
import com.eerussianguy.firmalife.common.recipes.FLRecipeTypes;
import com.eerussianguy.firmalife.common.recipes.OvenRecipe;
import com.eerussianguy.firmalife.config.FLConfig;
import com.vomiter.extradelight.common.complex.workstations.dryingrack.DryingRackRecipe;
import com.vomiter.extradelight.common.complex.workstations.oven.OvenBlock;
import com.vomiter.extradelight.common.complex.workstations.oven.OvenBlockEntity;
import java.util.Arrays;
import java.util.Optional;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.capabilities.heat.HeatCapability;
import net.dries007.tfc.common.capabilities.heat.IHeat;
import net.dries007.tfc.common.recipes.inventory.ItemStackInventory;
import net.dries007.tfc.common.recipes.outputs.AddRemoveTraitModifier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.common.utility.ItemUtils;

public class EDTFCFirmaLifeCompat {
    private static boolean addsDried(DryingRecipe recipe) {
        return Arrays.stream(recipe.getResult().modifiers()).anyMatch(modifier ->
                modifier instanceof AddRemoveTraitModifier trait && trait.add() && trait.trait() == FLFoodTraits.DRIED);
    }

    public static boolean canDryFood(Item item) {
        // Item-level eligibility ignores transient lacks-trait/not-rotten checks on saved food.
        return DryingRecipe.CACHE.getAll(item).stream().anyMatch(EDTFCFirmaLifeCompat::addsDried);
    }

    public static ItemStack driedFoodOutput(ItemStack input, RegistryAccess access) {
        for (DryingRecipe recipe : DryingRecipe.CACHE.getAll(input.getItem())) {
            if (addsDried(recipe) && recipe.getIngredient().test(input)) {
                ItemStack result = recipe.assemble(new ItemStackInventory(input.copyWithCount(1)), access);
                if (result.is(input.getItem()) && FoodCapability.hasTrait(result, FLFoodTraits.DRIED)) return result;
            }
        }
        return ItemStack.EMPTY;
    }

    public static Optional<DryingRackRecipe> findDryingRecipe(Level level, ItemStack input) {
        if (input.isEmpty()) return Optional.empty();
        DryingRecipe recipe = DryingRecipe.getRecipe(level, new ItemStackInventory(input));
        return recipe == null ? Optional.empty() : Optional.of(new DryingBridgeRecipe(recipe));
    }

    private static final class DryingBridgeRecipe extends DryingRackRecipe {
        private final DryingRecipe delegate;

        private DryingBridgeRecipe(DryingRecipe recipe) {
            super(recipe.getId(), "firmalife", recipe.getIngredient(), ItemStack.EMPTY, 0,
                    FLConfig.SERVER.dryingTicks.get());
            this.delegate = recipe;
        }

        @Override
        public boolean matches(SimpleContainer input, Level level) {
            return delegate.matches(new ItemStackInventory(input.getItem(0)), level);
        }

        @Override
        public ItemStack assemble(SimpleContainer input, RegistryAccess access) {
            return delegate.assemble(new ItemStackInventory(input.getItem(0).copyWithCount(1)), access);
        }
    }

    public static ItemStack getOvenResult(ItemStack ingredient, Level level){
        var recipe = level.getRecipeManager()
                        .getRecipeFor(
                                FLRecipeTypes.OVEN.get(),
                                new ItemStackInventory(ingredient),
                                level
                        );
        return recipe
                .map(ovenRecipe -> ovenRecipe.assemble(new ItemStackInventory(ingredient), level.registryAccess()))
                .orElse(ItemStack.EMPTY);
    }

    public static void ovenCooking(Level level, BlockPos pos, BlockState state, OvenBlockEntity oven){
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
            Optional<OvenRecipe> fLOvenRecipe
                    = level.getRecipeManager().getRecipeFor(FLRecipeTypes.OVEN.get(), new ItemStackInventory(stack0), level);
            IHeat heat0 = HeatCapability.get(stack0);

            if(fLOvenRecipe.isPresent() && heat0 != null){
                for (int i = 0; i < 9; i++) {
                    var stack = inventory.getStackInSlot(i);
                    if(stack.isEmpty()) continue;
                    IHeat heat = HeatCapability.get(stack);
                    if(heat == null) continue;
                    HeatCapability.addTemp(heat, fLOvenRecipe.get().getTemperature() + 10, 2);
                    if (heat.getTemperature() < fLOvenRecipe.get().getTemperature()) continue;
                    var result = fLOvenRecipe.get().assemble(new ItemStackInventory(stack.copyWithCount(1)), level.registryAccess());
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
