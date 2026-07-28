package com.vomiter.extradelighttfc.data;

import com.vomiter.extradelight.registry.ExtraDelightItems;
import com.vomiter.extradelighttfc.ExtraDelightTFC;
import com.vomiter.survivorsdelight.data.food.SDFoodDataProvider;
import net.dries007.tfc.common.items.Food;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public class EDTFCFoodDataProvider extends SDFoodDataProvider {
    public EDTFCFoodDataProvider(PackOutput packOutput) {
        super(packOutput, ExtraDelightTFC.MOD_ID);
    }

    public void save(){
        cut4(ExtraDelightItems.SLICED_APPLE, Food.RED_APPLE);
        cut4(ExtraDelightItems.SLICED_ONION, Food.ONION);
        cut4(ExtraDelightItems.SLICED_POTATO, Food.POTATO);
        cut4(ExtraDelightItems.SLICED_TOMATO, Food.TOMATO);
        cut4(ExtraDelightItems.GRATED_POTATO, Food.POTATO);
        cut4(ExtraDelightItems.GRATED_CARROT, Food.CARROT);
        cut4(ExtraDelightItems.GRATED_GARLIC, Food.GARLIC);
        cut4(ExtraDelightItems.POTATO_STICKS, Food.POTATO);
        cut4(ExtraDelightItems.BREAD_CRUMBS, Food.BARLEY_BREAD);
        newBuilder("butter")
                .item(ExtraDelightItems.BUTTER.get())
                .setDecay(4.5)
                .setHunger(3)
                .setSaturation(6)
                .save();

        newBuilder("bbq_sauce")
                .item(ExtraDelightItems.BBQ_SAUCE.get())
                .setDecay(0.2)
                .setSaturation(3)
                .setHunger(2)
                .setVegetables(1.5)
                .save();
    }

    private void cut4(RegistryObject<Item> result, Food source){
        newBuilder(result.getId().getPath())
                .item(result.get())
                .slicedFrom(source, 4)
                .save();
    }


}
