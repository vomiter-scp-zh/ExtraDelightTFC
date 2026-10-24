package com.vomiter.extradelighttfc.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.vomiter.extradelighttfc.registry.EDTFCFoodTraits;
import net.dries007.tfc.common.capabilities.ItemStackCapabilitySync;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = ItemStackCapabilitySync.class, remap = false)
public abstract class NetworkFoodLoadMixin {
    @WrapMethod(method = "readFromNetwork")
    private static void edtfc$identifyNetworkFood(ItemStack stack, CompoundTag tag, Operation<Void> original) {
        Item previous = EDTFCFoodTraits.pushLoadingItem(stack.getItem());
        try {
            original.call(stack, tag);
        } finally {
            EDTFCFoodTraits.restoreLoadingItem(previous);
        }
    }
}
