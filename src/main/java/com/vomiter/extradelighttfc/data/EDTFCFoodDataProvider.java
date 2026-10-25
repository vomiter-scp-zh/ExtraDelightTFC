package com.vomiter.extradelighttfc.data;

import com.vomiter.extradelight.registry.ExtraDelightItems;
import com.vomiter.extradelight.registry.SummerCitrus;
import com.vomiter.extradelight.registry.Fermentation;
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
        newBuilder("unshucked_corn").item(ExtraDelightItems.UNSHUCKED_CORN.get()).from(Food.MAIZE).save();
        newBuilder("cooked_corn").item(ExtraDelightItems.COOKED_CORN.get()).from(Food.MAIZE_BREAD).save();
        newBuilder("popcorn").item(ExtraDelightItems.POPCORN.get()).from(Food.MAIZE_BREAD).save();
        // Fixed intermediates: one item corresponds to 500 mB in BottleFluidRegistry.
        newBuilder("mixing_ingredients/mayo").item(ExtraDelightItems.MAYO.get()).setHunger(4).setSaturation(0.6).setProtein(1.6).setVegetables(0).setDairy(0.2).setDecay(4.5).save();
        newBuilder("mixing_ingredients/egg_mix").item(ExtraDelightItems.EGG_MIX.get()).setHunger(8).setSaturation(4).setProtein(3).setVegetables(0).setDairy(10.5).setDecay(4.5).save();
        newBuilder("mixing_ingredients/whipped_cream").item(ExtraDelightItems.WHIPPED_CREAM.get()).setHunger(0).setSaturation(0).setProtein(0).setVegetables(0).setDairy(10).setDecay(4.5).save();
        newBuilder("butter").item(ExtraDelightItems.BUTTER.get()).setDecay(4.5).setHunger(3).setSaturation(6).setDairy(1).save();
        newBuilder("mixing_ingredients/peanuts").item(ExtraDelightItems.PEANUTS.get()).setHunger(4).setSaturation(3).setProtein(1.0).setVegetables(0).setDecay(2).save();
        newBuilder("mixing_ingredients/roasted_peanuts").item(ExtraDelightItems.ROASTED_PEANUTS.get()).setHunger(4).setSaturation(3).setProtein(1.0).setVegetables(0).setDecay(1).save();
        newBuilder("mixing_ingredients/hazelnuts").item(ExtraDelightItems.HAZELNUTS.get()).setHunger(4).setSaturation(3).setProtein(1.0).setVegetables(0).setDecay(2).save();
        newBuilder("mixing_ingredients/roasted_hazelnuts").item(ExtraDelightItems.ROASTED_HAZELNUTS.get()).setHunger(4).setSaturation(3).setProtein(1.0).setVegetables(0).setDecay(1).save();
        newBuilder("mixing_ingredients/peanut_butter_bottle").item(ExtraDelightItems.PEANUT_BUTTER_BOTTLE.get()).setHunger(4).setSaturation(3).setProtein(1.0).setVegetables(0).setDecay(2).save();
        newBuilder("mixing_ingredients/mint").item(ExtraDelightItems.MINT.get()).setHunger(0).setSaturation(0).setProtein(0).setVegetables(0.1).setDecay(2).save();
        newBuilder("mixing_ingredients/mallow_root").item(ExtraDelightItems.MALLOW_ROOT.get()).setHunger(2).setSaturation(0.5).setProtein(0).setVegetables(0.5).setDecay(2).save();
        newBuilder("mixing_ingredients/mallow_powder").item(ExtraDelightItems.MALLOW_POWDER.get()).setHunger(2).setSaturation(0.5).setProtein(0).setVegetables(0.5).setDecay(1).save();
        newBuilder("mixing_ingredients/cooked_cactus").item(ExtraDelightItems.COOKED_CACTUS.get()).setHunger(4).setSaturation(1).setProtein(0).setVegetables(1).setDecay(2).save();
        newBuilder("mixing_ingredients/orange").item(SummerCitrus.ORANGE.get()).slicedFrom(Food.ORANGE, 1).save();
        newBuilder("mixing_ingredients/lime").item(SummerCitrus.LIME.get()).slicedFrom(Food.LEMON, 1).save();
        newBuilder("mixing_ingredients/grapefruit").item(SummerCitrus.GRAPEFRUIT.get()).slicedFrom(Food.ORANGE, 1).save();
        newBuilder("mixing_ingredients/cucumber").item(Fermentation.CUCUMBER.get()).slicedFrom(Food.GREEN_BELL_PEPPER, 1).save();
        newBuilder("mixing_ingredients/ginger").item(ExtraDelightItems.GINGER.get()).slicedFrom(Food.GARLIC, 1).save();
        newBuilder("mixing_ingredients/grated_ginger").item(ExtraDelightItems.GRATED_GINGER.get()).slicedFrom(Food.GARLIC, 4).save();
        newBuilder("mixing_ingredients/roasted_garlic").item(ExtraDelightItems.ROASTED_GARLIC.get()).slicedFrom(Food.GARLIC, 1).save();
        newBuilder("mixing_ingredients/boiled_egg").item(ExtraDelightItems.BOILED_EGG.get()).slicedFrom(Food.BOILED_EGG, 1).save();
        newBuilder("mixing_ingredients/cooked_pasta").item(ExtraDelightItems.COOKED_PASTA.get()).slicedFrom(Food.WHEAT_BREAD, 1).save();
        newBuilder("mixing_ingredients/croutons").item(ExtraDelightItems.CROUTONS.get()).slicedFrom(Food.WHEAT_BREAD, 1).save();
        newBuilder("mixing_ingredients/egg_yolk").item(ExtraDelightItems.EGG_YOLK.get()).slicedFrom(Food.COOKED_EGG, 2).save();
        newBuilder("mixing_ingredients/egg_white").item(ExtraDelightItems.EGG_WHITE.get()).slicedFrom(Food.COOKED_EGG, 2).save();
        cut4(ExtraDelightItems.SLICED_APPLE, Food.RED_APPLE);
        cut4(ExtraDelightItems.SLICED_ONION, Food.ONION);
        cut4(ExtraDelightItems.SLICED_POTATO, Food.POTATO);
        cut4(ExtraDelightItems.SLICED_TOMATO, Food.TOMATO);
        cut4(ExtraDelightItems.GRATED_POTATO, Food.POTATO);
        cut4(ExtraDelightItems.GRATED_CARROT, Food.CARROT);
        cut4(ExtraDelightItems.GRATED_GARLIC, Food.GARLIC);
        cut4(ExtraDelightItems.POTATO_STICKS, Food.POTATO);
        cut4(ExtraDelightItems.BREAD_CRUMBS, Food.BARLEY_BREAD);
        newBuilder("fish_flakes")
                .item(ExtraDelightItems.FISH_FLAKES.get())
                .slicedFrom(Food.COOKED_COD, 1)
                .save();
        newBuilder("dried_chili")
                .item(ExtraDelightItems.DRIED_CHILI.get())
                .setHunger(4).setSaturation(1).setVegetables(1).setDecay(0.5)
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
