package com.vomiter.extradelighttfc.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.vomiter.extradelight.common.complex.workstations.juicer.JuicerBlock;
import com.vomiter.extradelight.common.complex.workstations.juicer.JuicerBlockEntity;
import com.vomiter.extradelight.util.BottleFluidRegistry;
import com.vomiter.extradelighttfc.adapter.FancyFluidHandlerAdapter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(JuicerBlock.class)
public class JuicerBlockMixin {
    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lcom/vomiter/extradelight/util/BottleFluidRegistry;getBottleFromFluid(Lnet/minecraftforge/fluids/FluidStack;)Lnet/minecraft/world/item/ItemStack;", shift = At.Shift.AFTER, remap = false), cancellable = true)
    private void edtfc$use(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hitResult,
            CallbackInfoReturnable<InteractionResult> cir,
            @Local(name = "juicer") JuicerBlockEntity juicer
            )
    {
        ItemStack bottleResult = BottleFluidRegistry.getBottleFromFluid(juicer.getFluidTank().getFluid());
        if(bottleResult.isEmpty()) return;
        var input = player.getItemInHand(hand).copyWithCount(1);
        if (FancyFluidHandlerAdapter.checkBottleAndSetContainer(input, bottleResult)){
            player.getItemInHand(hand).shrink(1);
            player.addItem(bottleResult);
            juicer.getFluidTank().drain(500, IFluidHandler.FluidAction.EXECUTE);
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}
