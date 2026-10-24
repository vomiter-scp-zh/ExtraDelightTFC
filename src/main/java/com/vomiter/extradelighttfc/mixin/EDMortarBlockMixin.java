package com.vomiter.extradelighttfc.mixin;

import com.vomiter.extradelight.common.complex.workstations.mortar.MortarBlock;
import com.vomiter.extradelight.common.complex.workstations.mortar.MortarBlockEntity;
import com.vomiter.extradelight.util.BlockEntityUtils;
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
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MortarBlock.class)
public abstract class EDMortarBlockMixin {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void edtfc$bottleRegisteredVolume(BlockState state, Level level, BlockPos pos, Player player,
                                             InteractionHand hand, BlockHitResult hit,
                                             CallbackInfoReturnable<InteractionResult> cir) {
        if (!(level.getBlockEntity(pos) instanceof MortarBlockEntity mortar)) return;
        // Inventory and fluid mutations belong to the server, including ED's native branches.
        if (level.isClientSide) {
            cir.setReturnValue(InteractionResult.SUCCESS);
            return;
        }
        if (player.getItemInHand(hand).getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent()) return;

        ItemStack bottle = BottleFluidRegistry.getBottleFromFluid(mortar.getFluidTank().getFluid());
        if (bottle.isEmpty()) return;
        FluidStack registered = BottleFluidRegistry.getFluidFromBottle(bottle);
        if (registered.isEmpty()) return;
        InteractionHand[] hands = hand == InteractionHand.MAIN_HAND
                ? new InteractionHand[]{InteractionHand.MAIN_HAND, InteractionHand.OFF_HAND}
                : new InteractionHand[]{InteractionHand.OFF_HAND};
        for (InteractionHand candidate : hands) {
            ItemStack container = player.getItemInHand(candidate);
            ItemStack output = bottle.copy();
            if (container.isEmpty() || !FancyFluidHandlerAdapter.checkBottleAndSetContainer(container, output)) continue;
            cir.setReturnValue(InteractionResult.SUCCESS);
            FluidStack simulated = mortar.getFluidTank().drain(registered, IFluidHandler.FluidAction.SIMULATE);
            if (!simulated.isFluidEqual(registered) || simulated.getAmount() != registered.getAmount()) return;
            mortar.getFluidTank().drain(registered, IFluidHandler.FluidAction.EXECUTE);
            container.shrink(1);
            BlockEntityUtils.Inventory.givePlayerItemStack(output, player, level, pos);
            mortar.updateInventory();
            return;
        }
    }
}
