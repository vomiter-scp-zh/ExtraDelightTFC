package com.vomiter.extradelighttfc.mixin;

import com.vomiter.extradelight.common.complex.workstations.evaporator.EvaporatorBlockEntity;
import com.vomiter.extradelight.common.complex.workstations.evaporator.recipes.EvaporatorRecipe;
import com.vomiter.extradelighttfc.common.IEvaporatorCalendar;
import net.dries007.tfc.util.calendar.Calendars;
import net.dries007.tfc.util.climate.Climate;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EvaporatorBlockEntity.class)
public abstract class EDEvaporatorBlockEntityMixin implements IEvaporatorCalendar {
    @Unique private static final String EDTFC_LAST_CALENDAR_TICK = "edtfcLastCalendarTick";
    @Unique private static final long EDTFC_CALENDAR_JUMP_THRESHOLD = 600;
    @Unique private long edtfc$lastCalendarTick = -1;

    @Shadow(remap = false) private int cookTime;

    // ED stores the biome temperature in its first (and only) float local.
    @ModifyVariable(method = "tick", at = @At("STORE"), remap = false, name = "temp")
    private static float edtfc$climateTemperature(float temp, Level level, BlockPos pos,
                                                  BlockState state, BlockEntity blockEntity) {
        return Climate.getVanillaBiomeTemperature(level, pos);
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true, remap = false)
    private static void edtfc$waitForHarvest(Level level, BlockPos pos, BlockState state,
                                            BlockEntity blockEntity, CallbackInfo ci) {
        EvaporatorBlockEntity evaporator = (EvaporatorBlockEntity) blockEntity;
        if (level.isClientSide) {
            ci.cancel();
            return;
        }
        ((IEvaporatorCalendar) blockEntity).edtfc$checkCalendarUpdate();
        if (!evaporator.isInventoryEmpty()) {
            ci.cancel();
        }
    }

    @Override
    public void edtfc$checkCalendarUpdate() {
        EvaporatorBlockEntity evaporator = (EvaporatorBlockEntity) (Object) this;
        Level level = evaporator.getLevel();
        if (level == null || level.isClientSide) return;

        long now = Calendars.get(level).getTicks();
        long previous = edtfc$lastCalendarTick;
        edtfc$lastCalendarTick = now;
        if (previous == -1 || now < previous) {
            evaporator.setChanged();
            return;
        }

        long elapsed = now - previous;
        if (elapsed < EDTFC_CALENDAR_JUMP_THRESHOLD) return;
        // Persist even discarded gaps, e.g. when cold or waiting for harvest.
        evaporator.setChanged();
        if (!evaporator.isInventoryEmpty()) return;
        EvaporatorRecipe recipe = evaporator.matchRecipe().orElse(null);
        if (recipe == null) return;

        float temperature = Climate.getVanillaBiomeTemperature(level, evaporator.getBlockPos());
        if (!(temperature > 0.05D)) return;

        // The original tick still handles this tick's random progress/completion.
        // Catch up at the current climate's expected rate without replaying ticks.
        int remaining = Math.max(0, recipe.getCookTime() - cookTime);
        int bonus = (int) Math.min(remaining, Math.floor((elapsed - 1) * (double) Math.min(1F, temperature)));
        if (bonus > 0) cookTime += bonus;
    }

    @Inject(method = "zeroProgress", at = @At("TAIL"), remap = false)
    private void edtfc$resetCalendar(CallbackInfo ci) {
        EvaporatorBlockEntity evaporator = (EvaporatorBlockEntity) (Object) this;
        Level level = evaporator.getLevel();
        edtfc$lastCalendarTick = level != null && !level.isClientSide ? Calendars.get(level).getTicks() : -1;
    }

    @Inject(method = "saveAdditional", at = @At("TAIL"))
    private void edtfc$saveCalendar(CompoundTag tag, CallbackInfo ci) {
        tag.putLong(EDTFC_LAST_CALENDAR_TICK, edtfc$lastCalendarTick);
    }

    @Inject(method = "load", at = @At("TAIL"))
    private void edtfc$loadCalendar(CompoundTag tag, CallbackInfo ci) {
        edtfc$lastCalendarTick = tag.contains(EDTFC_LAST_CALENDAR_TICK, Tag.TAG_LONG)
                ? tag.getLong(EDTFC_LAST_CALENDAR_TICK) : -1;
    }
}
