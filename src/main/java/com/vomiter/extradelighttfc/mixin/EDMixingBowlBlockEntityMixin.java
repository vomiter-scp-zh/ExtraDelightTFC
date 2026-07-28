package com.vomiter.extradelighttfc.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.vomiter.extradelight.common.complex.workstations.FancyTank;
import com.vomiter.extradelight.common.complex.workstations.mixingbowl.MixingBowlBlockEntity;
import com.vomiter.extradelight.common.complex.workstations.mixingbowl.recipes.MixingBowlRecipe;
import com.vomiter.extradelight.common.complex.workstations.mixingbowl.recipes.MixingBowlRecipeWrapper;
import com.vomiter.extradelighttfc.adapter.EDMixingBowlAdapter;
import com.vomiter.survivorsdelight.common.food.FoodContainerExpansion;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(value = MixingBowlBlockEntity.class, remap = false)
public class EDMixingBowlBlockEntityMixin {
    @Shadow
    @Final
    private ItemStackHandler items;

    @Shadow
    MixingBowlRecipe curRecipe;

    @Shadow
    @Final
    private FancyTank fluids;

    @Shadow
    @Final
    public static int CONTAINER_SLOT;

    @WrapOperation(method = "mix", at = @At(value = "INVOKE", target = "Lcom/vomiter/extradelight/util/BlockEntityUtils$Inventory;givePlayerItemStack(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V"))
    private void edtfc$handleMixingOutput(ItemStack stack, Player player, Level level, BlockPos pos, Operation<Void> original){
        original.call(EDMixingBowlAdapter.calcDynamic(items, fluids, stack, curRecipe, level), player, level, pos);
    }

    @WrapOperation(
            method = "matchRecipe",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/crafting/RecipeManager;" +
                            "getRecipeFor(Lnet/minecraft/world/item/crafting/RecipeType;" +
                            "Lnet/minecraft/world/Container;" +
                            "Lnet/minecraft/world/level/Level;)" +
                            "Ljava/util/Optional;",
                    remap = true
            )
    )
    private Optional<Recipe<Container>> edtfc$acceptBowl(
            RecipeManager instance,
            RecipeType<?> recipeType,
            Container container,
            Level level,
            Operation<Optional<Recipe<Container>>> original){
        var originalResult = original.call(instance, recipeType, container, level);
        if (originalResult.isEmpty()){
            var handler = new ItemStackHandler(14);
            for (int i = 0; i < 14; i++) {
                if(i == CONTAINER_SLOT){
                    handler.setStackInSlot(i, FoodContainerExpansion.replaceStack(items.getStackInSlot(i)));
                }
                else handler.setStackInSlot(i, items.getStackInSlot(i));
            }
            var replacedWrapper = new MixingBowlRecipeWrapper(handler, this.fluids) {
                public int getContainerSize() {
                    return 9;
                }

                public boolean isEmpty() {
                    boolean res = true;
                    if (this.getTank().getTotalAmount() != 0) {
                        return false;
                    } else {
                        for(int i = 0; i < this.inv.getSlots(); ++i) {
                            if (!this.inv.getStackInSlot(i).isEmpty()) {
                                res = false;
                                break;
                            }
                        }

                        return res;
                    }
                }
            };
            return original.call(instance, recipeType, replacedWrapper, level);
        }

        return originalResult;
    }

}
