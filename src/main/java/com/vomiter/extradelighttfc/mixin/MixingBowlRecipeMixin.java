package com.vomiter.extradelighttfc.mixin;

import com.vomiter.extradelight.common.complex.workstations.mixingbowl.recipes.MixingBowlRecipe;
import com.vomiter.extradelight.common.complex.workstations.mixingbowl.recipes.MixingBowlRecipeWrapper;
import java.util.ArrayList;
import java.util.List;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MixingBowlRecipe.class, remap = false)
public class MixingBowlRecipeMixin {
    @Inject(method = "matches(Lcom/vomiter/extradelight/common/complex/workstations/mixingbowl/recipes/MixingBowlRecipeWrapper;Lnet/minecraft/world/level/Level;)Z",
            at = @At("HEAD"), cancellable = true)
    private void edtfc$matchWithoutExpandingEmptyTags(MixingBowlRecipeWrapper input, Level level,
                                                     CallbackInfoReturnable<Boolean> cir) {
        MixingBowlRecipe recipe = (MixingBowlRecipe) (Object) this;
        if (recipe.getUtensil().getItems().length == 0) { cir.setReturnValue(false); return; }
        List<ItemStack> items = new ArrayList<>();
        for (int slot = 0; slot < 9; slot++) {
            ItemStack stack = input.getItem(slot);
            if (FoodCapability.isRotten(stack)) { cir.setReturnValue(false); return; }
            if (!stack.isEmpty()) items.add(stack);
        }
        List<Ingredient> ingredients = recipe.getIngredients();
        boolean legacyEmpty = ingredients.size() == 1 && ingredients.get(0) == Ingredient.EMPTY;
        boolean matches = ingredients.isEmpty() || legacyEmpty ? items.isEmpty()
                : items.size() == ingredients.size() && RecipeMatcher.findMatches(items, ingredients) != null;
        if (!matches || !ItemStack.isSameItem(recipe.getContainer(), input.getItem(9))
                || input.getItem(9).getCount() < recipe.getContainer().getCount()) {
            cir.setReturnValue(false);
            return;
        }
        List<FluidStack> fluids = input.getTank().getAsList();
        int[] remaining = fluids.stream().mapToInt(FluidStack::getAmount).toArray();
        for (var ingredient : recipe.getFluids()) {
            boolean found = false;
            for (int tank = 0; tank < fluids.size(); tank++) {
                FluidStack available = fluids.get(tank).copy();
                available.setAmount(remaining[tank]);
                if (ingredient.test(available)) {
                    remaining[tank] -= ingredient.getAmount();
                    found = true;
                    break;
                }
            }
            if (!found) { cir.setReturnValue(false); return; }
        }
        cir.setReturnValue(true);
    }
}
