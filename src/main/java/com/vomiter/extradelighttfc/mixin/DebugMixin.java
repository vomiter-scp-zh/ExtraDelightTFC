package com.vomiter.extradelighttfc.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.vomiter.extradelight.client.ExtraDelightClientEvents;
import net.minecraftforge.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ExtraDelightClientEvents.class, remap = false)
public class DebugMixin {
    @WrapOperation(method = "addToolTip", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/ModList;isLoaded(Ljava/lang/String;)Z"))
    private static boolean edtfc$debug(ModList instance, String modTarget, Operation<Boolean> original){
        return false;
    }
}
