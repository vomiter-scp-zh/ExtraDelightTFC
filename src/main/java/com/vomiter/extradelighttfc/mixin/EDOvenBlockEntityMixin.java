package com.vomiter.extradelighttfc.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.vomiter.extradelight.common.complex.workstations.oven.OvenBlockEntity;
import com.vomiter.extradelight.common.complex.workstations.oven.recipes.OvenRecipe;
import com.vomiter.extradelighttfc.adapter.EDOvenCanCookAdapter;
import com.vomiter.extradelighttfc.adapter.EDOvenCookingAdapter;
import com.vomiter.extradelighttfc.compat.EDTFCFirmaLifeCompat;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(value = OvenBlockEntity.class, remap = false)
public abstract class EDOvenBlockEntityMixin extends BlockEntity {


    @Shadow
    @Final
    public static int OUTPUT_SLOT;

    @Shadow
    @Final
    private ItemStackHandler inventory;

    @Shadow
    protected abstract boolean hasInput();

    @Shadow
    protected abstract Optional<OvenRecipe> getMatchingRecipe(RecipeWrapper inventoryWrapper);

    public EDOvenBlockEntityMixin(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
    }

    @WrapOperation(method = "processCooking", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/items/ItemStackHandler;setStackInSlot(ILnet/minecraft/world/item/ItemStack;)V"))
    private void edtfc$handleOvenOutput(
            ItemStackHandler instance,
            int slot,
            ItemStack stack,
            Operation<Void> original,
            @Local(argsOnly = true) OvenRecipe recipe
    ){
        if (slot == OUTPUT_SLOT && level instanceof ServerLevel serverLevel){
            original.call(
                    instance,
                    slot,
                    EDOvenCookingAdapter.calcDynamic(inventory, stack, recipe, serverLevel)
            );
        }
        else original.call(instance, slot, stack);
    }


    @WrapMethod(method = "canCook")
    private boolean edtfc$canCook(OvenRecipe recipe, Operation<Boolean> original){
        if(hasInput()){
            var self = (OvenBlockEntity)(Object)this;
            return EDOvenCanCookAdapter.canCook(self, recipe);
        }
        return false;
    }

    @Inject(method = "cookingTick", at = @At("TAIL"))
    private static void edtfc$cookingTick(
            Level level, BlockPos pos, BlockState state, OvenBlockEntity oven, CallbackInfo ci,
            @Local(name = "recipe") Optional<OvenRecipe> recipe){
        if(recipe.isPresent()) return;
        if(ModList.get().isLoaded("firmalife")){
            EDTFCFirmaLifeCompat.ovenCooking(level, pos, state, oven);
        }
        EDOvenCookingAdapter.heatCooking(level, pos, state, oven);
    }
}
