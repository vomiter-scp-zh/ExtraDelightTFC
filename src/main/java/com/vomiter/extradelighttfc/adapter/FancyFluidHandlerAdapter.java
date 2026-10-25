package com.vomiter.extradelighttfc.adapter;

import com.vomiter.extradelight.common.complex.workstations.FancyTank;
import com.vomiter.extradelight.util.BlockEntityUtils;
import com.vomiter.extradelight.util.BottleFluidRegistry;
import com.vomiter.survivorsdelight.common.food.FoodContainerExpansion;
import com.vomiter.survivorsdelight.util.FoodItemContainerApply;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.IItemHandler;

/** Transfers one container at a time, committing only the fluid that actually fits. */
public class FancyFluidHandlerAdapter<T extends BlockEntity> {
    public void fillInternal(T be, IItemHandler inventory, FancyTank tank, int slot) {
        if (be.getLevel() == null || be.getLevel().isClientSide) return;
        int limit = inventory.getStackInSlot(slot).getCount();
        for (int i = 0; i < limit; i++) {
            ItemStack input = inventory.getStackInSlot(slot).copyWithCount(1);
            if (input.isEmpty() || FoodCapability.isRotten(input)) return;
            ItemStack remainder;
            IFluidHandlerItem capability = input.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).orElse(null);
            if (capability != null) {
                FluidStack transferred = FluidUtil.tryFluidTransfer(tank, capability, tank.getVacancy(), true);
                if (transferred.isEmpty()) return;
                remainder = capability.getContainer();
            } else {
                FluidStack fluid = BottleFluidRegistry.getFluidFromBottle(input);
                if (fluid.isEmpty() || tank.fill(fluid, IFluidHandler.FluidAction.SIMULATE) != fluid.getAmount()) return;
                remainder = input.is(Items.POTION) ? new ItemStack(Items.GLASS_BOTTLE) : input.getCraftingRemainingItem();
                tank.fill(fluid, IFluidHandler.FluidAction.EXECUTE);
            }
            inventory.extractItem(slot, 1, false);
            give(be, remainder);
        }
    }

    public static boolean checkBottleAndSetContainer(ItemStack container, IFluidHandler tank) {
        FluidStack fluid = tank.drain(500, IFluidHandler.FluidAction.SIMULATE);
        return checkBottleAndSetContainer(container, BottleFluidRegistry.getBottleFromFluid(fluid));
    }

    public static boolean checkBottleAndSetContainer(ItemStack container, ItemStack bottle) {
        if (container.isEmpty() || bottle.isEmpty()) return false;
        ItemStack expected = bottle.is(Items.POTION) ? new ItemStack(Items.GLASS_BOTTLE) : bottle.getCraftingRemainingItem();
        if (expected.isEmpty()) return false;
        if (ItemStack.isSameItem(expected, container)) return true;
        if (FoodContainerExpansion.isExtraValid(expected.getItem(), container)) {
            FoodItemContainerApply.applyGeneral(bottle, container.copyWithCount(1));
            return true;
        }
        return false;
    }

    public void drainInternal(T be, IItemHandler inventory, FancyTank tank, int slot) {
        if (be.getLevel() == null || be.getLevel().isClientSide) return;
        int limit = inventory.getStackInSlot(slot).getCount();
        for (int i = 0; i < limit; i++) {
            ItemStack input = inventory.getStackInSlot(slot).copyWithCount(1);
            if (input.isEmpty()) return;
            IFluidHandlerItem capability = input.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).orElse(null);
            ItemStack output;
            if (capability != null) {
                FluidStack transferred = FluidUtil.tryFluidTransfer(capability, tank, Integer.MAX_VALUE, true);
                if (transferred.isEmpty()) return;
                output = capability.getContainer();
            } else {
                FluidStack fluid = tank.drain(500, IFluidHandler.FluidAction.SIMULATE);
                if (fluid.getAmount() != 500) return;
                output = BottleFluidRegistry.getBottleFromFluid(fluid);
                if (!checkBottleAndSetContainer(input, output)) return;
                if (tank.drain(fluid, IFluidHandler.FluidAction.EXECUTE).getAmount() != 500) return;
            }
            inventory.extractItem(slot, 1, false);
            give(be, output);
        }
    }

    private void give(T be, ItemStack stack) {
        if (!stack.isEmpty()) BlockEntityUtils.Inventory.dropItemInWorld(stack.copy(), be.getLevel(), be.getBlockPos());
    }
}
