package com.vomiter.extradelighttfc.mixin;

import com.vomiter.extradelight.common.complex.workstations.mortar.MortarBlockEntity;
import com.vomiter.extradelight.common.complex.workstations.mortar.recipes.MortarRecipe;
import com.vomiter.extradelight.registry.ExtraDelightRecipes;
import com.vomiter.extradelight.util.BlockEntityUtils;
import com.vomiter.extradelighttfc.common.recipe.TFCMortarRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(value = MortarBlockEntity.class, remap = false)
public abstract class EDMortarBlockEntityMixin {
    @Shadow private int grinds;

    @Inject(method = "matchRecipe", at = @At("HEAD"), cancellable = true)
    private void edtfc$preferTFCRecipes(ItemStack input, CallbackInfoReturnable<Optional<MortarRecipe>> cir) {
        MortarBlockEntity mortar = (MortarBlockEntity) (Object) this;
        if (mortar.getLevel() == null || input.isEmpty()) return;
        SimpleContainer inventory = new SimpleContainer(input);
        for (MortarRecipe recipe : mortar.getLevel().getRecipeManager().getAllRecipesFor(ExtraDelightRecipes.MORTAR.get())) {
            if (recipe instanceof TFCMortarRecipe && recipe.matches(inventory, mortar.getLevel())) {
                cir.setReturnValue(Optional.of(recipe));
                return;
            }
        }
    }

    @Inject(method = "grind", at = @At("HEAD"), cancellable = true)
    private void edtfc$grindWithModifiers(Player player, CallbackInfoReturnable<InteractionResult> cir) {
        MortarBlockEntity mortar = (MortarBlockEntity) (Object) this;
        if (!(mortar.getLevel() instanceof ServerLevel level)) {
            cir.setReturnValue(InteractionResult.SUCCESS);
            return;
        }
        ItemStack input = mortar.getInsertedItem();
        MortarRecipe matching = mortar.matchRecipe(input).orElse(null);
        if (!(matching instanceof TFCMortarRecipe recipe)) return;
        cir.setReturnValue(InteractionResult.SUCCESS);
        if (input.isEmpty()) return;

        FluidTank tank = mortar.getFluidTank();
        FluidStack fluid = recipe.getFluid().copy();
        if (!fluid.isEmpty()) {
            long amount = (long) fluid.getAmount() * input.getCount();
            if (amount > tank.getCapacity()) return;
            fluid.setAmount((int) amount);
            // Check the entire batch, including fluid identity and NBT, before progressing.
            if (tank.fill(fluid, IFluidHandler.FluidAction.SIMULATE) != amount) return;
        }

        BlockPos pos = mortar.getBlockPos();
        if (grinds + 1 < recipe.getGrinds()) {
            grinds++;
            level.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, input),
                    pos.getX() + 0.5, pos.getY() + 0.4, pos.getZ() + 0.5,
                    3, 0.15, 0.1, 0.15, 0);
            level.playSound(null, pos, SoundEvents.STONE_HIT, SoundSource.BLOCKS, 1, 1);
        } else {
            // Evaluate all providers before committing inventory or fluid changes.
            List<ItemStack> outputs = new ArrayList<>();
            for (int i = 0; i < input.getCount(); i++) {
                outputs.add(recipe.createOutput(input.copyWithCount(1)));
            }
            if (!fluid.isEmpty()) tank.fill(fluid, IFluidHandler.FluidAction.EXECUTE);
            mortar.getItemHandler().setStackInSlot(0, ItemStack.EMPTY);
            grinds = 0;
            for (ItemStack output : outputs) {
                if (!output.isEmpty()) BlockEntityUtils.Inventory.dropItemInWorld(output, level, pos);
            }
        }
        mortar.updateInventory();
    }
}
