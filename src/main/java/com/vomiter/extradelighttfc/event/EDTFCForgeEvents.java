package com.vomiter.extradelighttfc.event;

import com.vomiter.extradelight.util.BottleFluidRegistry;
import com.vomiter.extradelight.registry.ExtraDelightFluids;
import net.dries007.tfc.util.Drinkable;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.capabilities.food.TFCFoodData;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import vectorwing.farmersdelight.common.registry.ModItems;

public class EDTFCForgeEvents {
    public static void onFinishUse(LivingEntityUseItemEvent.Finish event){
        var item = event.getItem();
        if(item.is(ModItems.MELON_JUICE.get())) return;
        if (item.is(ModItems.APPLE_CIDER.get())) return;
        var fluid = BottleFluidRegistry.getFluidFromBottle(item);
        if(fluid == null) return;
        var drinkable = Drinkable.get(fluid.getFluid());
        if(drinkable == null) return;
        if(event.getEntity() instanceof Player player){
            // Food-capable bottled items already feed the player through TFC's food capability.
            // Only add hydration here, avoiding a second dose of Drinkable nutrients.
            if (FoodCapability.get(item) != null && (
                    fluid.getFluid() == ExtraDelightFluids.MAYO.FLUID.get()
                    || fluid.getFluid() == ExtraDelightFluids.EGG_MIX.FLUID.get()
                    || fluid.getFluid() == ExtraDelightFluids.WHIPPED_CREAM.FLUID.get()
                    || fluid.getFluid() == ExtraDelightFluids.EGG_WHITE.FLUID.get()
                    || fluid.getFluid() == ExtraDelightFluids.NUT_BUTTER.FLUID.get())) {
                if (player.getFoodData() instanceof TFCFoodData food) {
                    food.addThirst(drinkable.getThirst() * (fluid.getAmount() / 25f));
                }
            } else {
                drinkable.onDrink(player, fluid.getAmount());
            }
        }
    }
}
