package com.vomiter.extradelighttfc.util;

import net.minecraft.world.item.ItemStack;

public class EDTFCThreadLocals {
    public static ThreadLocal<ItemStack> REPLACED_CONTAINER = ThreadLocal.withInitial(() -> ItemStack.EMPTY);
    public static ThreadLocal<Boolean> HAS_APPLIED_CONTAINER = ThreadLocal.withInitial(() -> false);
}
