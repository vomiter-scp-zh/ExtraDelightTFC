package com.vomiter.extradelighttfc.mixin.fancy_tank;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.vomiter.extradelight.common.complex.workstations.FancyTank;
import com.vomiter.extradelight.gui.FancyTankInSlot;
import com.vomiter.extradelight.gui.FancyTankOutSlot;
import com.vomiter.extradelight.util.BottleFluidRegistry;
import com.vomiter.extradelighttfc.adapter.FancyFluidHandlerAdapter;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = FancyTankOutSlot.class, remap = false)
public class FancyTankOutSlotMixin {
    @Shadow
    private FancyTank tank;

    @WrapMethod(method = "calcFluidOutSlotSize")
    private int edtfc$cal(ItemStack pSlot, Operation<Integer> original){
        if (pSlot.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent() && !pSlot.is(Items.BUCKET)
                && !pSlot.getCraftingRemainingItem().is(Items.BUCKET))
            return 1;
        else {
            if (pSlot.getItem() == Items.BUCKET)
                return tank.getFluidAmount(0) / 1000;
            else if (FancyFluidHandlerAdapter.checkBottleAndSetContainer(pSlot, tank))
                return tank.getFluidAmount(0) / 500;
            else if (tank.drain(500, IFluidHandler.FluidAction.SIMULATE).getFluid().isSame(Fluids.WATER) && pSlot.is(Items.GLASS_BOTTLE))
                return tank.getFluidAmount(0) / 500;
            return 0;
        }

    }

}
