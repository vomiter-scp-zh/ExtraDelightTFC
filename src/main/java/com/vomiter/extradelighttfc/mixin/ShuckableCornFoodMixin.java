package com.vomiter.extradelighttfc.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.vomiter.extradelight.common.items.corn.ShuckableCorn;
import com.vomiter.extradelight.registry.ExtraDelightItems;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.capabilities.food.IFood;
import net.dries007.tfc.common.items.Food;
import net.dries007.tfc.common.items.TFCItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ShuckableCorn.class)
public abstract class ShuckableCornFoodMixin {
    @WrapOperation(method = "finishUsingItem", at = @At(value = "INVOKE", target =
            "Lnet/minecraft/world/level/storage/loot/LootTable;getRandomItems(Lnet/minecraft/world/level/storage/loot/LootParams;)Lit/unimi/dsi/fastutil/objects/ObjectArrayList;"))
    private ObjectArrayList<ItemStack> edtfc$inheritMaizeFood(
            LootTable table, LootParams params, Operation<ObjectArrayList<ItemStack>> original,
            @Local(argsOnly = true) ItemStack input) {
        ObjectArrayList<ItemStack> drops = original.call(table, params);
        IFood source = FoodCapability.get(input);
        if (input.is(ExtraDelightItems.UNSHUCKED_CORN.get()) && source != null) {
            long creationDate = source.getCreationDate();
            for (ItemStack drop : drops) {
                if (drop.is(TFCItems.FOOD.get(Food.MAIZE).get())) {
                    FoodCapability.updateFoodFromPrevious(input, drop);
                    IFood result = FoodCapability.get(drop);
                    if (result != null) {
                        result.setCreationDate(creationDate);
                    }
                }
            }
        }
        return drops;
    }
}
