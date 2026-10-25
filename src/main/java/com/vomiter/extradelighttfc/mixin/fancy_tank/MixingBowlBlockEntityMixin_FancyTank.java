package com.vomiter.extradelighttfc.mixin.fancy_tank;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.vomiter.extradelight.common.complex.workstations.FancyTank;
import com.vomiter.extradelight.common.complex.workstations.mixingbowl.MixingBowlBlockEntity;
import com.vomiter.extradelight.util.BottleFluidRegistry;
import com.vomiter.extradelighttfc.adapter.FancyFluidHandlerAdapter;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = MixingBowlBlockEntity.class, remap = false)
public class MixingBowlBlockEntityMixin_FancyTank {

    @Shadow
    @Final
    private FancyTank fluids;
    private static final int LIQUID_IN_SLOT = 10;
    private static final int LIQUID_OUT_SLOT = 11;

    @WrapMethod(method = "createHandler")
    private ItemStackHandler edtfc$createHandler(Operation<ItemStackHandler> original){
        var self = (MixingBowlBlockEntity)(Object)this;
        
        return new ItemStackHandler(14) {
            private boolean transferring;
            public boolean isItemValid(int slot, ItemStack stack) {
                switch (slot) {
                    case LIQUID_IN_SLOT:
                        return stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent()
                                || stack.is(Items.BUCKET)
                                || !BottleFluidRegistry.getFluidFromBottle(stack).isEmpty();
                    case LIQUID_OUT_SLOT:
                        return stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent()
                                || stack.is(Items.BUCKET)
                                || FancyFluidHandlerAdapter.checkBottleAndSetContainer(stack, fluids);
                    case 12:
                    case 13:
                        return false;
                    default:
                        return true;
                }
            }

            protected void onContentsChanged(int slot) {
                if (slot == 12 || slot == 13) return;
                self.zeroProgress();
                self.updateInventory();
                if (slot != LIQUID_IN_SLOT && slot != LIQUID_OUT_SLOT) return;

                if (transferring || self.getLevel() == null || self.getLevel().isClientSide) return;
                transferring = true;
                try {
                    if (slot == LIQUID_IN_SLOT) {
                        new FancyFluidHandlerAdapter<>().fillInternal(self, self.getItemHandler(), self.getFluidTank(), LIQUID_IN_SLOT);
                    } else if (slot == LIQUID_OUT_SLOT) {
                        new FancyFluidHandlerAdapter<>().drainInternal(self, self.getItemHandler(), self.getFluidTank(), LIQUID_OUT_SLOT);
                    }
                } finally {
                    transferring = false;
                }
                self.updateInventory();

            }
        };
    }
}
