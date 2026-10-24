package com.vomiter.extradelighttfc.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.vomiter.extradelighttfc.registry.EDTFCFoodTraits;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = CapabilityProvider.class, remap = false)
public abstract class ItemStackFoodLoadMixin {
    @WrapMethod(method = "deserializeCaps")
    private void edtfc$identifyFoodItem(CompoundTag tag, Operation<Void> original) {
        if ((Object) this instanceof ItemStack stack) {
            Item previous = EDTFCFoodTraits.pushLoadingItem(stack.getItem());
            try {
                original.call(tag);
            } finally {
                EDTFCFoodTraits.restoreLoadingItem(previous);
            }
        } else original.call(tag);
    }
}
