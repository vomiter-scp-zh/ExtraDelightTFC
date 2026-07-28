package com.vomiter.extradelighttfc.adapter;

import com.vomiter.extradelight.common.complex.workstations.FancyTank;
import com.vomiter.extradelight.util.BlockEntityUtils;
import com.vomiter.extradelight.util.BottleFluidRegistry;
import com.vomiter.extradelighttfc.ExtraDelightTFC;
import com.vomiter.survivorsdelight.common.food.FoodContainerExpansion;
import com.vomiter.survivorsdelight.util.FoodItemContainerApply;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.IItemHandler;

public class FancyFluidHandlerAdapter <T extends BlockEntity>{
    public void fillInternal(T be, IItemHandler itemHandler, FancyTank fluidTank, int fluidSlot) {
        ItemStack inputItem = itemHandler.getStackInSlot(fluidSlot);
        if (!inputItem.isEmpty()) {
            if (inputItem.getItem() instanceof BucketItem filledBucket) {
                int filled = fluidTank.fill(new FluidStack(filledBucket.getFluid(), FluidType.BUCKET_VOLUME),
                        IFluidHandler.FluidAction.SIMULATE);
                if (filled == FluidType.BUCKET_VOLUME) {
                    fluidTank.fill(new FluidStack(filledBucket.getFluid(), FluidType.BUCKET_VOLUME),
                            IFluidHandler.FluidAction.EXECUTE);
                    BlockEntityUtils.Inventory.dropItemInWorld(Items.BUCKET.getDefaultInstance().copy(), be.getLevel(),
                            be.getBlockPos());
                    
                    itemHandler.extractItem(fluidSlot, 1, false);
                }
            } else if (inputItem.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent()) {
                IFluidHandlerItem fluidHandlerItem = inputItem.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).orElse(null);
                int filled = FluidUtil
                        .tryFluidTransfer(fluidTank, fluidHandlerItem, fluidTank.getVacancy(), true)
                        .getAmount();
                if (filled > 0) {
                    BlockEntityUtils.Inventory.dropItemInWorld(fluidHandlerItem.getContainer().copy(), be.getLevel(),
                            be.getBlockPos());
                    itemHandler.extractItem(fluidSlot, 1, false);
                }
            } else {
                FluidStack f = BottleFluidRegistry.getFluidFromBottle(inputItem);
                if (!f.isEmpty()) {
                    if (fluidTank.fill(f, IFluidHandler.FluidAction.SIMULATE) == 500) {
                        int sz = inputItem.getCount();
                        for (int j = 0; j < sz; j++)
                            fluidTank.fill(f.copy(), IFluidHandler.FluidAction.EXECUTE);

                        if (inputItem.is(Items.POTION)) {
                            // Because the blasted water bottle has no craftRemainder
                            BlockEntityUtils.Inventory.dropItemInWorld(Items.GLASS_BOTTLE.getDefaultInstance(),
                                    be.getLevel(), be.getBlockPos());

                        } else {
                            BlockEntityUtils.Inventory.dropItemInWorld(
                                    inputItem.getCraftingRemainingItem().copyWithCount(sz), be.getLevel(),
                                    be.getBlockPos());
                        }

                        itemHandler.extractItem(fluidSlot, sz, false);
                    }
                }
            }
        }
    }

    private void drainForBucket(T be, FancyTank fluidTank, ItemStack inputItem , int sz){
        FluidStack stack = fluidTank.drain(FluidType.BUCKET_VOLUME, IFluidHandler.FluidAction.SIMULATE);
        var item = stack.getFluid().getBucket().getDefaultInstance();
        int oitr = Math.min(sz, fluidTank.getFluidAmount(0) / FluidType.BUCKET_VOLUME);
        int itr = oitr;
        while (inputItem.getItem() == Items.BUCKET && stack.getAmount() == FluidType.BUCKET_VOLUME && itr-- >= 0) {
            fluidTank.drain(FluidType.BUCKET_VOLUME, IFluidHandler.FluidAction.EXECUTE);
            BlockEntityUtils.Inventory.dropItemInWorld(item.copy(), be.getLevel(), be.getBlockPos());
            inputItem.shrink(1);
            stack = fluidTank.drain(FluidType.BUCKET_VOLUME, IFluidHandler.FluidAction.SIMULATE);
        }
    }

    private void drainForCapItem(T be, FancyTank fluidTank, ItemStack inputItem , int sz){
        IFluidHandlerItem fluidHandlerItem = inputItem.copyWithCount(1)
                .getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).orElse(null);
        // not really the actual capacity of fluidHandlerItem
        // this might cause mod compat issues later on
        int maxFill = Math.min(fluidTank.getFluidAmount(0),
                (fluidHandlerItem.getTankCapacity(0) - fluidHandlerItem.getFluidInTank(0).getAmount()) * sz);
        int filled = FluidUtil.tryFluidTransfer(fluidHandlerItem, fluidTank, maxFill, true).getAmount();
        maxFill -= filled;
        while (filled > 0 && maxFill >= 0) {
            filled = FluidUtil.tryFluidTransfer(fluidHandlerItem, fluidTank, maxFill, true).getAmount();
            BlockEntityUtils.Inventory.dropItemInWorld(fluidHandlerItem.getContainer().copy(), be.getLevel(),
                    be.getBlockPos());
            inputItem.shrink(1);
        }
    }

    public static boolean checkBottleAndSetContainer(ItemStack container, IFluidHandler fluidTank){
        FluidStack stack = fluidTank.drain(500, IFluidHandler.FluidAction.SIMULATE);
        ItemStack bottle = BottleFluidRegistry.getBottleFromFluid(stack);
        if(ItemStack.isSameItem(bottle.getCraftingRemainingItem(), container)){
            return true;
        }

        if(FoodContainerExpansion.isExtraValid(
                bottle.getCraftingRemainingItem().getItem(),
                container
        )){
            FoodItemContainerApply.applyGeneral(bottle, container.copy());
            return true;
        }
        return false;
    }


    public static boolean checkBottleAndSetContainer(ItemStack container, ItemStack bottle){
        if(ItemStack.isSameItem(bottle.getCraftingRemainingItem(), container)){
            return true;
        }
        if(FoodContainerExpansion.isExtraValid(
                bottle.getCraftingRemainingItem().getItem(),
                container
        )){
            FoodItemContainerApply.applyGeneral(bottle, container.copy());
            return true;
        }
        return false;
    }

    private void drainForBottle(T be, FancyTank fluidTank, ItemStack inputItem, int sz){
        FluidStack stack = fluidTank.drain(500, IFluidHandler.FluidAction.SIMULATE);
        ItemStack bottleItem = BottleFluidRegistry.getBottleFromFluid(stack);
        if (!bottleItem.isEmpty() && checkBottleAndSetContainer(inputItem, bottleItem)) {
            for (int j = 0; j < sz; j++) fluidTank.drain(stack, IFluidHandler.FluidAction.EXECUTE);
            BlockEntityUtils.Inventory.dropItemInWorld(bottleItem.copyWithCount(sz), be.getLevel(),
                    be.getBlockPos());
        }
        if (bottleItem.getItem() == Items.POTION && inputItem.getItem() == Items.GLASS_BOTTLE) {
            FluidStack stack1 = fluidTank.drain(500, IFluidHandler.FluidAction.SIMULATE);
            for (int j = 0; j < sz; j++) {
                fluidTank.drain(stack1, IFluidHandler.FluidAction.EXECUTE);
                BlockEntityUtils.Inventory.dropItemInWorld(bottleItem, be.getLevel(), be.getBlockPos());
            }
        }
        inputItem.shrink(sz);
    }


    public void drainInternal(T be, IItemHandler itemHandler, FancyTank fluidTank, int fluidOutSlot) {
        ItemStack inputItem = itemHandler.getStackInSlot(fluidOutSlot);
        if (!inputItem.isEmpty()) {
            int sz = inputItem.getCount();
            if (inputItem.getItem() == Items.BUCKET) {
                drainForBucket(be, fluidTank, inputItem, sz);
            } else if (fluidTank.getFluid() != null && checkBottleAndSetContainer(
                    inputItem,
                    fluidTank
            )){
                drainForBottle(be, fluidTank, inputItem, sz);
            } else if (inputItem.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent()) {
                drainForCapItem(be, fluidTank, inputItem, sz);
            }
        }
    }

}
