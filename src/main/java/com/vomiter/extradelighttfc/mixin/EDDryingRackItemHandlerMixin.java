package com.vomiter.extradelighttfc.mixin;

import com.vomiter.extradelight.common.complex.workstations.BlockEntityItemHandler;
import com.vomiter.extradelighttfc.common.IDryingRackProgress;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "com.vomiter.extradelight.common.complex.workstations.dryingrack.DryingRackBlockEntity$1", remap = false)
public abstract class EDDryingRackItemHandlerMixin {

    @Inject(method = "insertItem", at = @At("RETURN"))
    private void edtfc$startCalendar(int slot, ItemStack input, boolean simulate, CallbackInfoReturnable<ItemStack> cir) {
        if (!simulate && cir.getReturnValue().getCount() < input.getCount()) {
            ((IDryingRackProgress) ((BlockEntityItemHandler<?>) (Object) this).getBlockEntity()).edtfc$resetCalendar(slot);
        }
    }

    @Inject(method = "extractItem", at = @At("RETURN"))
    private void edtfc$endCalendar(int slot, int amount, boolean simulate, CallbackInfoReturnable<ItemStack> cir) {
        if (!simulate && !cir.getReturnValue().isEmpty()) {
            ((IDryingRackProgress) ((BlockEntityItemHandler<?>) (Object) this).getBlockEntity()).edtfc$resetCalendar(slot);
        }
    }
}
