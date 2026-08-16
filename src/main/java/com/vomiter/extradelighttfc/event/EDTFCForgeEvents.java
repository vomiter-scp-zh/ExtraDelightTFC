package com.vomiter.extradelighttfc.event;

import com.vomiter.extradelight.util.BottleFluidRegistry;
import net.dries007.tfc.util.Drinkable;
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
            drinkable.onDrink(player, 500);
        }
    }
}
