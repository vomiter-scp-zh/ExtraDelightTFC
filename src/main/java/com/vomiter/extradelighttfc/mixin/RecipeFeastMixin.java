package com.vomiter.extradelighttfc.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.vomiter.extradelight.common.blocks.RecipeFeastBlock;
import com.vomiter.extradelight.common.recipes.SimpleRecipeWrapper;
import com.vomiter.extradelighttfc.common.RecipeFeastBlockEntity;
import com.vomiter.extradelighttfc.registry.EDTFCBlockEntityTypes;
import com.vomiter.extradelighttfc.util.EDTFCThreadLocals;
import com.vomiter.survivorsdelight.SDConfig;
import com.vomiter.survivorsdelight.common.food.FoodContainerExpansion;
import com.vomiter.survivorsdelight.common.food.block.DecayFoodTransfer;
import com.vomiter.survivorsdelight.common.food.block.ISDDecayingBlock;
import com.vomiter.survivorsdelight.util.FoodItemContainerApply;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.capabilities.food.IFood;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(RecipeFeastBlock.class)
public abstract class RecipeFeastMixin extends Block implements ISDDecayingBlock, EntityBlock {
    public RecipeFeastMixin(Properties p_49795_) {
        super(p_49795_);
    }

    @Shadow
    public abstract IntegerProperty getServingsProperty();

    @Shadow
    public abstract int getMaxServings();

    @Override
    public int sdtfc$getServingCount(BlockState blockState) {
        return blockState.getValue(this.getServingsProperty());
    }

    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new RecipeFeastBlockEntity(pos, state);
    }

    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return type == EDTFCBlockEntityTypes.DECAY_RECIPE_FEAST.get() ? (l, p, st, be) -> RecipeFeastBlockEntity.serverTick(l, p, st, (RecipeFeastBlockEntity)be) : null;
    }

    @WrapOperation(method = "takeServing", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/crafting/RecipeManager;" +
                    "getRecipeFor(Lnet/minecraft/world/item/crafting/RecipeType;" +
                    "Lnet/minecraft/world/Container;" +
                    "Lnet/minecraft/world/level/Level;)" +
                    "Ljava/util/Optional;",
                    remap = true),
            remap = false
    )
    private Optional<Object> edtfc$acceptAltContainer(
            RecipeManager instance,
            RecipeType<?> recipeType,
            Container container,
            Level level,
            Operation<Optional<Object>> original,
            @Local(argsOnly = true)ItemStack usedStack){
        var originalResult = original.call(instance, recipeType, container, level);
        if (originalResult.isEmpty()){
            var blockItem = asItem();
            var replaced = FoodContainerExpansion.replaceStack(usedStack);
            if(!ItemStack.isSameItem(replaced, usedStack)){
                EDTFCThreadLocals.REPLACED_CONTAINER.set(usedStack);
            }
            return original.call(instance, recipeType, new SimpleRecipeWrapper(blockItem.getDefaultInstance(), replaced), level);
        }
        return originalResult;
    }

    @WrapOperation(
            method = "takeServing",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Inventory;add(Lnet/minecraft/world/item/ItemStack;)Z", remap = true),
            remap = false
    )
    private boolean edtfc$addResult(
            Inventory instance,
            ItemStack serving,
            Operation<Boolean> original,
            @Local(argsOnly = true) Level level,
            @Local(argsOnly = true) BlockPos pos,
            @Local(argsOnly = true) Player player,
            @Local(argsOnly = true) InteractionHand hand
            ){
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof RecipeFeastBlockEntity decayingFeastBlockEntity)) return original.call(instance, serving);

        ItemStack src = decayingFeastBlockEntity.getStack();
        float factor;
        if (SDConfig.REBALANCING_FEAST) factor = 1f / (float) getMaxServings();
        else factor = 1f;
        DecayFoodTransfer.copyFoodState(src, serving, true, factor);
        if (!EDTFCThreadLocals.REPLACED_CONTAINER.get().isEmpty()) {
            FoodItemContainerApply.applyGeneral(serving, EDTFCThreadLocals.REPLACED_CONTAINER.get().copyWithCount(1));
        }
        IFood containerFood = FoodCapability.get(player.getItemInHand(hand));
        if(containerFood != null){
            FoodItemContainerApply.applyGeneral(serving, player.getItemInHand(hand).copyWithCount(1));
        }

        return original.call(instance, serving);
    }

    @WrapMethod(method = "takeServing", remap = false)
    private InteractionResult edtfc$takeServing(ItemStack stack, Level level, BlockPos pos, BlockState state, Player player, InteractionHand hand, Operation<InteractionResult> original){
        try {
            return original.call(stack, level, pos, state, player, hand);
        } finally {
            EDTFCThreadLocals.REPLACED_CONTAINER.remove();
        }
    }

}
