package com.vomiter.extradelighttfc.mixin;

import com.vomiter.extradelight.common.ExtraDelightEvents;
import com.vomiter.extradelight.common.items.ToolTipConsumableItem;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ExtraDelightEvents.class, remap = false)
public class EDForgeEventMixin {
    @Inject(method = "onFinishUsing", at = @At("HEAD"), cancellable = true)
    private static void edtfc$onFinishUsing(LivingEntityUseItemEvent.Finish event, CallbackInfo ci){
        if (event.getItem().getItem() instanceof ToolTipConsumableItem item){
            if (event.getItem().getTag() == null && event.getEntity() instanceof Player player){
                player.addItem(item.getCraftingRemainingItem(event.getItem()));
            }
            ci.cancel();
        }
    }

}
