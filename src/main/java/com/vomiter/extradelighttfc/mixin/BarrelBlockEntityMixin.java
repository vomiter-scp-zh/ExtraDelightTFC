package com.vomiter.extradelighttfc.mixin;

import com.vomiter.extradelight.util.BottleFluidRegistry;
import com.vomiter.extradelighttfc.adapter.FancyFluidHandlerAdapter;
import net.dries007.tfc.common.blockentities.BarrelBlockEntity;
import net.dries007.tfc.common.blockentities.InventoryBlockEntity;
import net.dries007.tfc.common.capabilities.SidedHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BarrelBlockEntity.class, remap = false)
public abstract class BarrelBlockEntityMixin extends InventoryBlockEntity {
    @Shadow
    @Final
    private SidedHandler.Builder<IFluidHandler> sidedFluidInventory;

    public BarrelBlockEntityMixin(BlockEntityType type, BlockPos pos, BlockState state, InventoryFactory inventoryFactory, Component defaultName) {
        super(type, pos, state, inventoryFactory, defaultName);
    }

    @Inject(method = "isItemValid", at = @At("RETURN"), cancellable = true)
    private void edtfc$setExtraValid(int slot, ItemStack stack, CallbackInfoReturnable<Boolean> cir){
        if (cir.getReturnValue()) return;
        if (!BottleFluidRegistry.getFluidFromBottle(stack).isEmpty()){
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "updateFluidIOSlots", at = @At("TAIL"))
    private void edtfc$updateFluid(CallbackInfo ci) {
        if (!getCapability(ForgeCapabilities.FLUID_HANDLER).isPresent()) return;
        IFluidHandler fluidHandler = getCapability(ForgeCapabilities.FLUID_HANDLER).orElse(null);
        if(!(inventory instanceof BarrelBlockEntity.BarrelInventory barrelInventory)){
            return;
        }
        if(!inventory.getStackInSlot(1).isEmpty()) return;

        ItemStack input = inventory.getStackInSlot(0);
        FluidStack fluidStack = BottleFluidRegistry.getFluidFromBottle(input);
        if (!fluidStack.isEmpty()){
            if (fluidHandler.getFluidInTank(0).isEmpty() || fluidHandler.getFluidInTank(0).isFluidEqual(fluidStack)){
                int remainCapacity = barrelInventory.getTankCapacity(0) - barrelInventory.getFluidInTank(0).getAmount();
                int countToConsume = Math.min(remainCapacity / fluidStack.getAmount(), input.getCount());
                ItemStack inputRemainder = input.getCraftingRemainingItem().copyWithCount(countToConsume);
                for (int i = 0; i < countToConsume; i++) {
                    barrelInventory.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                    input.shrink(1);
                }
                inventory.setStackInSlot(1, inputRemainder);
            }
        } else if (!fluidHandler.getFluidInTank(0).isEmpty()){
            FluidStack simFluidStack = fluidHandler.drain(500, IFluidHandler.FluidAction.SIMULATE);
            ItemStack bottleStack = BottleFluidRegistry.getBottleFromFluid(simFluidStack);
            if (FancyFluidHandlerAdapter.checkBottleAndSetContainer(input, bottleStack)){
                int countToConsume = Math.min(input.getCount(), fluidHandler.getFluidInTank(0).getAmount() / 500);
                for (int i = 0; i < countToConsume; i++) {
                    barrelInventory.drain(simFluidStack, IFluidHandler.FluidAction.EXECUTE);
                    input.shrink(1);
                }
                inventory.setStackInSlot(1, bottleStack.copyWithCount(countToConsume));
            }

        }


    }
}
