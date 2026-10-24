package com.vomiter.extradelighttfc.data;

import com.vomiter.extradelight.registry.ExtraDelightItems;
import com.vomiter.extradelight.data.tags.ExtraDelightTags;
import com.vomiter.extradelighttfc.registry.EDTFCItems;
import com.vomiter.extradelighttfc.util.FoodUtils;
import net.dries007.tfc.common.items.Food;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import java.util.EnumSet;
import net.dries007.tfc.common.items.TFCItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class EDTFCItemTagProvider extends ItemTagsProvider {


    public EDTFCItemTagProvider(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_, CompletableFuture<TagLookup<Block>> p_275322_, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_275343_, p_275729_, p_275322_, modId, existingFileHelper);
    }

    private void addDynamicFood(RegistryObject<Item> item){
        tag(EDTFCTags.DYNAMIC_FOOD).add(item.get());
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider p_256380_) {
        var fish = EnumSet.of(Food.COOKED_COD, Food.COOKED_TROPICAL_FISH, Food.COOKED_BLUEGILL,
                Food.COOKED_CRAPPIE, Food.COOKED_LAKE_TROUT, Food.COOKED_LARGEMOUTH_BASS,
                Food.COOKED_RAINBOW_TROUT, Food.COOKED_SALMON, Food.COOKED_SMALLMOUTH_BASS);
        var cookedMeats = EnumSet.range(Food.COOKED_BEEF, Food.COOKED_SMALLMOUTH_BASS);
        cookedMeats.removeAll(fish);
        tag(EDTFCTags.DRYABLE_FOODS).addTag(ItemTags.create(ResourceLocation.fromNamespaceAndPath("tfc", "foods/fruits"))).addTag(ItemTags.create(ResourceLocation.fromNamespaceAndPath("tfc", "foods/raw_meats")));
        cookedMeats.forEach(food -> tag(EDTFCTags.DRYABLE_FOODS).add(TFCItems.FOOD.get(food).get()));
        fish.forEach(food -> tag(EDTFCTags.FISH_FLAKES_INPUTS).add(TFCItems.FOOD.get(food).get()));
        tag(EDTFCTags.SEAWEED_PASTE_INPUTS).add(TFCItems.FOOD.get(Food.DRIED_SEAWEED).get(),
                TFCItems.FOOD.get(Food.DRIED_KELP).get());

        tag(ExtraDelightTags.PESTLES).add(EDTFCItems.STONE_PESTLE.get());
        EDTFCItems.METAL_PESTLES.values().forEach(pestle -> tag(ExtraDelightTags.PESTLES).add(pestle.get()));
        tag(EDTFCTags.PROCESSED_ONION).add(FoodUtils.getFoodItem(Food.ONION));
        tag(EDTFCTags.PROCESSED_TOMATO).add(FoodUtils.getFoodItem(Food.TOMATO));
        tag(EDTFCTags.PROCESSED_POTATO).add(FoodUtils.getFoodItem(Food.POTATO));
        tag(EDTFCTags.PROCESSED_APPLE).add(FoodUtils.getFoodItem(Food.RED_APPLE, Food.GREEN_APPLE));

        addDynamicFood(ExtraDelightItems.SAUSAGE_ROLL);
        addDynamicFood(ExtraDelightItems.LIVER_ONIONS);
        addDynamicFood(ExtraDelightItems.STUFFED_HEART);
        addDynamicFood(ExtraDelightItems.FRIED_BRAINS);
        addDynamicFood(ExtraDelightItems.OXTAIL_SOUP);
        addDynamicFood(ExtraDelightItems.BBQ_RIBS_FEAST_ITEM);
        addDynamicFood(ExtraDelightItems.BBQ_RIBS);
        addDynamicFood(ExtraDelightItems.RACK_LAMB);
        addDynamicFood(ExtraDelightItems.RACK_LAMB_FEAST_ITEM);
        addDynamicFood(ExtraDelightItems.HAGGIS_FEAST_ITEM);
        addDynamicFood(ExtraDelightItems.HAGGIS);
        addDynamicFood(ExtraDelightItems.BEEF_WELLINGTON_FEAST_ITEM);
        addDynamicFood(ExtraDelightItems.BEEF_WELLINGTON);
        addDynamicFood(ExtraDelightItems.MINT_LAMB_FEAST);
        addDynamicFood(ExtraDelightItems.MINT_LAMB);
        addDynamicFood(ExtraDelightItems.CURRYWURST);
        addDynamicFood(ExtraDelightItems.PULLED_PORK_FEAST_ITEM);
        addDynamicFood(ExtraDelightItems.PULLED_PORK_SANDWICH);
        addDynamicFood(ExtraDelightItems.PORK_AND_APPLES);
        addDynamicFood(ExtraDelightItems.PORK_AND_APPLES_FEAST);

    }
}
