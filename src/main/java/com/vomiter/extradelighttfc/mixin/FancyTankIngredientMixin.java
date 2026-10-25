package com.vomiter.extradelighttfc.mixin;

import com.vomiter.extradelight.common.complex.workstations.FancyTank;
import com.vomiter.extradelight.common.recipes.ingredients.SizedFluidIngredient;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FancyTank.class, remap = false)
public class FancyTankIngredientMixin {
    @Inject(method = "drain(Lcom/vomiter/extradelight/common/recipes/ingredients/SizedFluidIngredient;Lnet/minecraftforge/fluids/capability/IFluidHandler$FluidAction;)Lnet/minecraftforge/fluids/FluidStack;",
            at = @At("HEAD"), cancellable = true)
    private void edtfc$drainActualIngredient(SizedFluidIngredient ingredient, IFluidHandler.FluidAction action,
                                           CallbackInfoReturnable<FluidStack> cir) {
        FancyTank self = (FancyTank) (Object) this;
        for (int i = 0; i < self.getTanks(); i++) {
            FluidStack stored = self.getFluidInTank(i);
            if (ingredient.test(stored)) {
                FluidStack request = stored.copy();
                request.setAmount(ingredient.getAmount());
                cir.setReturnValue(self.drain(request, action));
                return;
            }
        }
        cir.setReturnValue(FluidStack.EMPTY);
    }
}
