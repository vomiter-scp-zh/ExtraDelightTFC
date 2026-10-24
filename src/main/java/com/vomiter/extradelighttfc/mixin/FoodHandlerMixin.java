package com.vomiter.extradelighttfc.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.vomiter.extradelighttfc.registry.EDTFCFoodTraits;
import net.dries007.tfc.common.capabilities.food.FoodHandler;
import net.dries007.tfc.common.capabilities.food.FoodTrait;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = FoodHandler.class, remap = false)
public abstract class FoodHandlerMixin {
    @Shadow @Final protected List<FoodTrait> foodTraits;

    @WrapOperation(method = "deserializeNBT(Lnet/minecraft/nbt/CompoundTag;)V", at = @At(value = "INVOKE",
            target = "Lnet/dries007/tfc/common/capabilities/food/FoodTrait;getTrait(Lnet/minecraft/resources/ResourceLocation;)Lnet/dries007/tfc/common/capabilities/food/FoodTrait;"))
    private FoodTrait edtfc$loadCanonicalDried(ResourceLocation id, Operation<FoodTrait> original) {
        return original.call(EDTFCFoodTraits.resolveSavedId(id));
    }

    @Inject(method = "deserializeNBT(Lnet/minecraft/nbt/CompoundTag;)V", at = @At("TAIL"))
    private void edtfc$deduplicateDried(CompoundTag tag, CallbackInfo ci) {
        FoodTrait own = EDTFCFoodTraits.DRIED;
        FoodTrait firmalife = EDTFCFoodTraits.dried();
        boolean foundOwn = false;
        boolean foundFirmalife = false;
        for (var iterator = foodTraits.iterator(); iterator.hasNext();) {
            FoodTrait trait = iterator.next();
            if (trait == own) {
                if (foundOwn) iterator.remove();
                foundOwn = true;
            } else if (trait == firmalife) {
                if (foundFirmalife) iterator.remove();
                foundFirmalife = true;
            }
        }
    }
}
