package com.vomiter.extradelighttfc.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.vomiter.extradelight.common.complex.workstations.dryingrack.DryingRackBlockEntity;
import com.vomiter.extradelight.common.complex.workstations.dryingrack.DryingRackRecipe;
import com.vomiter.extradelighttfc.common.IDryingRackProgress;
import com.vomiter.extradelighttfc.compat.EDTFCFirmaLifeCompat;
import java.util.Arrays;
import java.util.Optional;
import net.dries007.tfc.util.calendar.Calendars;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DryingRackBlockEntity.class)
public abstract class EDDryingRackBlockEntityMixin implements IDryingRackProgress {
    @Shadow(remap = false) private int[] cookingTime;
    @Shadow(remap = false) private int[] cookingProgress;
    @Unique private static final String EDTFC_CALENDAR_TAG = "edtfcDryingCalendarTicks";
    @Unique private static final long EDTFC_JUMP_THRESHOLD = 600;
    @Unique private long[] edtfc$lastCalendarTicks = edtfc$newCalendarTicks();

    @Unique
    private static long[] edtfc$newCalendarTicks() {
        long[] ticks = new long[DryingRackBlockEntity.NUM_SLOTS];
        Arrays.fill(ticks, -1);
        return ticks;
    }

    @Override
    public void edtfc$resetCalendar(int slot) {
        DryingRackBlockEntity rack = (DryingRackBlockEntity) (Object) this;
        Level level = rack.getLevel();
        edtfc$lastCalendarTicks[slot] = level == null || level.isClientSide ? -1 : Calendars.get(level).getTicks();
    }

    @Override
    public void edtfc$checkCalendarUpdate() {
        DryingRackBlockEntity rack = (DryingRackBlockEntity) (Object) this;
        Level level = rack.getLevel();
        if (level == null || level.isClientSide) return;
        long now = Calendars.get(level).getTicks();
        for (int slot = 0; slot < DryingRackBlockEntity.NUM_SLOTS; slot++) {
            long previous = edtfc$lastCalendarTicks[slot];
            edtfc$lastCalendarTicks[slot] = now;
            if (cookingTime[slot] <= 0) continue;
            // Leave the current native tick to finish the batch, including Integer.MAX_VALUE recipes.
            cookingProgress[slot] = Math.min(cookingProgress[slot], cookingTime[slot] - 1);
            if (previous == -1 || now < previous) continue;
            long elapsed = now - previous;
            if (elapsed < EDTFC_JUMP_THRESHOLD || rack.matchRecipe(rack.getItemHandler().getStackInSlot(slot)).isEmpty()) continue;
            long remaining = Math.max(0L, (long) cookingTime[slot] - cookingProgress[slot] - 1);
            cookingProgress[slot] += (int) Math.min(remaining, elapsed - 1);
        }
    }

    @Inject(method = "tick", at = @At("HEAD"), remap = false)
    private static void edtfc$calendarTick(Level level, BlockPos pos, BlockState state, BlockEntity be, CallbackInfo ci) {
        ((IDryingRackProgress) be).edtfc$checkCalendarUpdate();
    }

    @Inject(method = "saveAdditional", at = @At("TAIL"))
    private void edtfc$saveCalendar(CompoundTag tag, CallbackInfo ci) {
        tag.putLongArray(EDTFC_CALENDAR_TAG, edtfc$lastCalendarTicks);
    }

    @Inject(method = "load", at = @At("TAIL"))
    private void edtfc$loadCalendar(CompoundTag tag, CallbackInfo ci) {
        long[] ticks = tag.getLongArray(EDTFC_CALENDAR_TAG);
        edtfc$lastCalendarTicks = ticks.length == DryingRackBlockEntity.NUM_SLOTS ? ticks : edtfc$newCalendarTicks();
    }

    @Inject(method = "matchRecipe", at = @At("RETURN"), cancellable = true, remap = false)
    private void edtfc$firmalifeFallback(ItemStack input, CallbackInfoReturnable<Optional<DryingRackRecipe>> cir) {
        DryingRackBlockEntity rack = (DryingRackBlockEntity) (Object) this;
        if (cir.getReturnValue().isEmpty() && rack.getLevel() != null && ModList.get().isLoaded("firmalife")) {
            cir.setReturnValue(EDTFCFirmaLifeCompat.findDryingRecipe(rack.getLevel(), input));
        }
    }

    @Override
    public int edtfc$getDryingTime(int slot) { return cookingTime[slot]; }

    @Override
    public void edtfc$finishDrying(int slot) { cookingTime[slot] = 0; }

    @WrapOperation(method = "tick", remap = false, at = @At(value = "INVOKE", remap = true,
            target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z", ordinal = 1))
    private static boolean edtfc$validateResult(ItemStack cached, Operation<Boolean> original,
            @Local(name = "pBlockEntity") DryingRackBlockEntity rack,
            @Local(name = "itemstack") ItemStack input, @Local(name = "i") int slot) {
        return rack.getLevel().isClientSide || ((IDryingRackProgress) rack).edtfc$getDryingTime(slot) <= 0 || rack.matchRecipe(input).isEmpty();
    }

    @WrapOperation(method = "tick", remap = false, at = @At(value = "INVOKE", remap = true,
            target = "Lnet/minecraft/world/item/ItemStack;copy()Lnet/minecraft/world/item/ItemStack;"))
    private static ItemStack edtfc$assembleResult(ItemStack cached, Operation<ItemStack> original,
            @Local(name = "pBlockEntity") DryingRackBlockEntity rack,
            @Local(name = "itemstack") ItemStack input, @Local(name = "i") int slot) {
        DryingRackRecipe recipe = rack.matchRecipe(input).orElseThrow();
        ItemStack result = recipe.assemble(new SimpleContainer(input), rack.getLevel().registryAccess());
        ((IDryingRackProgress) rack).edtfc$finishDrying(slot);
        return result;
    }
}
