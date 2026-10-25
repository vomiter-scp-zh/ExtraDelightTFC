package com.vomiter.extradelighttfc.data;

import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.ModTags;
import net.dries007.tfc.common.TFCTags;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;

import com.vomiter.extradelight.registry.ExtraDelightItems;
import com.vomiter.extradelight.registry.SummerCitrus;
import com.vomiter.extradelight.registry.Fermentation;
import net.dries007.tfc.common.items.Powder;
import com.vomiter.extradelighttfc.ExtraDelightTFC;
import com.vomiter.extradelight.data.tags.ExtraDelightTags;
import com.vomiter.extradelighttfc.registry.EDTFCItems;
import com.vomiter.extradelighttfc.util.FoodUtils;
import net.dries007.tfc.common.items.Food;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.ResourceLocation;
import java.util.EnumSet;
import net.dries007.tfc.common.items.TFCItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
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
        copy(BlockTags.FENCES, ItemTags.FENCES);
        copy(BlockTags.WOODEN_FENCES, ItemTags.WOODEN_FENCES);
        copy(Tags.Blocks.FENCES_WOODEN, Tags.Items.FENCES_WOODEN);
        tag(ModTags.Items.KNIVES).addTag(TFCTags.Items.KNIVES);
        tag(EDTFCTags.FORGE_FRUITS).addTag(EDTFCTags.TFC_FRUITS);

        TagKey<Item> forgeSpoons = ItemTags.create(ResourceLocation.fromNamespaceAndPath("forge", "spoons"));
        tag(ExtraDelightTags.SPOONS).add(EDTFCItems.STONE_SPOON.get());
        tag(forgeSpoons).add(EDTFCItems.STONE_SPOON.get());
        EDTFCItems.METAL_SPOONS.values().forEach(spoon -> {
            tag(ExtraDelightTags.SPOONS).add(spoon.get());
            tag(forgeSpoons).add(spoon.get());
        });
        for (var cookie : DoughProcessingRecipes.COOKIES) {
            tag(EDTFCTags.DYNAMIC_FOOD).add(cookie.dough().get(), cookie.baked().get());
            tag(EDTFCTags.COOKIE_BAKING_ITEMS).add(cookie.dough().get(), cookie.baked().get());
            tag(EDTFCTags.EXTRA_NUTRIENT_AFTER_HEATING).add(cookie.dough().get());
        }
        tag(EDTFCTags.DYNAMIC_FOOD).add(ModItems.RAW_PASTA.get(), ExtraDelightItems.MACARONI.get(),
                ExtraDelightItems.PENNE.get(), ExtraDelightItems.LASAGNA_NOODLES.get());
        // Opt-in processing inputs, not their underlying raw ingredients.
        tag(EDTFCTags.EXTRA_NUTRIENT_AFTER_HEATING).add(
                ExtraDelightItems.APPLE_COOKIE_DOUGH.get(),
                ExtraDelightItems.GINGERBREAD_COOKIE_DOUGH.get(),
                ExtraDelightItems.NUT_BUTTER_COOKIE_DOUGH.get(),
                ExtraDelightItems.PUMPKIN_COOKIE_DOUGH.get(),
                ExtraDelightItems.SUGAR_COOKIE_DOUGH.get(),
                ExtraDelightItems.SWEET_BERRY_COOKIE_DOUGH.get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/flour"))).addTag(ItemTags.create(ResourceLocation.tryParse("tfc:foods/flour")));
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/sweetener"))).addTag(ItemTags.create(ResourceLocation.tryParse("tfc:sweetener")));
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/eggs"))).add(Items.EGG);
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/eggs"))).addOptionalTag(ResourceLocation.tryParse("firmalife:foods/raw_eggs"));
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/butter"))).add(ExtraDelightItems.BUTTER.get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/butter"))).addOptional(ResourceLocation.tryParse("firmalife:food/butter"));
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/berries"))).add(TFCItems.FOOD.get(Food.BLACKBERRY).get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/berries"))).add(TFCItems.FOOD.get(Food.RASPBERRY).get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/berries"))).add(TFCItems.FOOD.get(Food.BLUEBERRY).get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/berries"))).add(TFCItems.FOOD.get(Food.ELDERBERRY).get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/berries"))).add(TFCItems.FOOD.get(Food.BUNCHBERRY).get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/berries"))).add(TFCItems.FOOD.get(Food.GOOSEBERRY).get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/berries"))).add(TFCItems.FOOD.get(Food.SNOWBERRY).get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/berries"))).add(TFCItems.FOOD.get(Food.CLOUDBERRY).get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/berries"))).add(TFCItems.FOOD.get(Food.STRAWBERRY).get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/berries"))).add(TFCItems.FOOD.get(Food.WINTERGREEN_BERRY).get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/berries"))).add(TFCItems.FOOD.get(Food.CRANBERRY).get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/cooked_fish"))).add(TFCItems.FOOD.get(Food.COOKED_COD).get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/cooked_fish"))).add(TFCItems.FOOD.get(Food.COOKED_TROPICAL_FISH).get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/cooked_fish"))).add(TFCItems.FOOD.get(Food.COOKED_BLUEGILL).get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/cooked_fish"))).add(TFCItems.FOOD.get(Food.COOKED_CRAPPIE).get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/cooked_fish"))).add(TFCItems.FOOD.get(Food.COOKED_LAKE_TROUT).get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/cooked_fish"))).add(TFCItems.FOOD.get(Food.COOKED_LARGEMOUTH_BASS).get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/cooked_fish"))).add(TFCItems.FOOD.get(Food.COOKED_RAINBOW_TROUT).get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/cooked_fish"))).add(TFCItems.FOOD.get(Food.COOKED_SALMON).get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/cooked_fish"))).add(TFCItems.FOOD.get(Food.COOKED_SMALLMOUTH_BASS).get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/cooked_fish"))).add(TFCItems.FOOD.get(Food.COOKED_SHELLFISH).get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/cooked_fish"))).add(TFCItems.FOOD.get(Food.COOKED_CALAMARI).get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/cheese"))).add(TFCItems.FOOD.get(Food.CHEESE).get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/cheese"))).addOptionalTag(ResourceLocation.tryParse("firmalife:foods/cheeses"));
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/boiled_eggs"))).add(ExtraDelightItems.BOILED_EGG.get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/cooked_meat"))).addTag(ItemTags.create(ResourceLocation.tryParse("tfc:foods/cooked_meats")));
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/vegetables"))).addTag(ItemTags.create(ResourceLocation.tryParse("tfc:foods/vegetables")));
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/apple"))).add(TFCItems.FOOD.get(Food.RED_APPLE).get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/apple"))).add(TFCItems.FOOD.get(Food.GREEN_APPLE).get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/apple"))).add(ExtraDelightItems.SLICED_APPLE.get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/onion"))).add(TFCItems.FOOD.get(Food.ONION).get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/onion"))).add(ExtraDelightItems.SLICED_ONION.get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/tomato"))).add(TFCItems.FOOD.get(Food.TOMATO).get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/tomato"))).add(ExtraDelightItems.SLICED_TOMATO.get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/potato"))).add(TFCItems.FOOD.get(Food.POTATO).get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/potato"))).add(ExtraDelightItems.SLICED_POTATO.get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/potato"))).add(ExtraDelightItems.GRATED_POTATO.get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/carrot"))).add(TFCItems.FOOD.get(Food.CARROT).get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/carrot"))).add(ExtraDelightItems.GRATED_CARROT.get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/garlic"))).add(TFCItems.FOOD.get(Food.GARLIC).get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/garlic"))).add(ExtraDelightItems.GRATED_GARLIC.get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/cabbage"))).add(TFCItems.FOOD.get(Food.CABBAGE).get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/beetroot"))).add(TFCItems.FOOD.get(Food.BEET).get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/melon"))).add(TFCItems.FOOD.get(Food.MELON_SLICE).get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/orange"))).add(SummerCitrus.ORANGE.get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/lemon"))).add(TFCItems.FOOD.get(Food.LEMON).get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/lime"))).add(SummerCitrus.LIME.get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/grapefruit"))).add(SummerCitrus.GRAPEFRUIT.get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/cucumber"))).add(Fermentation.CUCUMBER.get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/ginger"))).add(ExtraDelightItems.GINGER.get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/ginger"))).add(ExtraDelightItems.GRATED_GINGER.get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/chili"))).add(ExtraDelightItems.CHILI.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(ExtraDelightItems.AGLIO_E_OLIO.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(ExtraDelightItems.AIOLI.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(ExtraDelightItems.APPLE_COOKIE_DOUGH.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(ExtraDelightItems.APPLE_SLAW.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(ExtraDelightItems.BEET_MINT_SALAD.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(ExtraDelightItems.BUTTERED_PASTA.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(ExtraDelightItems.CACTUS_SALAD.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(ExtraDelightItems.CARROT_SALAD.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(ExtraDelightItems.CHICKEN_ALFREDO.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(SummerCitrus.CITRUS_ONION_SALAD.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(ExtraDelightItems.COFFEE_JELLY.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(Fermentation.CUCUMBER_SALAD.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(SummerCitrus.DALGONA_COFFEE.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(ExtraDelightItems.EGG_SALAD.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(SummerCitrus.ETON_MESS.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(ExtraDelightItems.FISH_SALAD.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(ExtraDelightItems.FROSTING_BLACK.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(ExtraDelightItems.FROSTING_BLUE.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(ExtraDelightItems.FROSTING_BROWN.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(ExtraDelightItems.FROSTING_CYAN.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(ExtraDelightItems.FROSTING_GRAY.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(ExtraDelightItems.FROSTING_GREEN.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(ExtraDelightItems.FROSTING_LIGHT_BLUE.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(ExtraDelightItems.FROSTING_LIGHT_GRAY.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(ExtraDelightItems.FROSTING_LIME.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(ExtraDelightItems.FROSTING_MAGENTA.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(ExtraDelightItems.FROSTING_ORANGE.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(ExtraDelightItems.FROSTING_PINK.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(ExtraDelightItems.FROSTING_PURPLE.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(ExtraDelightItems.FROSTING_RED.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(ExtraDelightItems.FROSTING_WHITE.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(ExtraDelightItems.FROSTING_YELLOW.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(ExtraDelightItems.FURIKAKE.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(Fermentation.GAZPACHO.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(ExtraDelightItems.GINGERBREAD_COOKIE_DOUGH.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(SummerCitrus.GRAPEFRUIT_BEETROOT_SALAD.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(ExtraDelightItems.GUMMIES.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(Fermentation.HOT_WINGS.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(SummerCitrus.LEMONADE.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(SummerCitrus.LIMEADE.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(ExtraDelightItems.MARSHMALLOW.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(SummerCitrus.MELON_FRUIT_SALAD_ITEM.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(SummerCitrus.MELON_GAZPACHO.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(Fermentation.MORKOVCHA.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(ExtraDelightItems.NOUGAT.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(ExtraDelightItems.NUT_BUTTER_COOKIE_DOUGH.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(SummerCitrus.ORANGEADE.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(ExtraDelightItems.PASTA_ALFREDO.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(ExtraDelightItems.PASTA_TOMATO.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(ExtraDelightItems.POTATO_SALAD.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(ExtraDelightItems.PULLED_PORK_FEAST_ITEM.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(ExtraDelightItems.PUMPKIN_COOKIE_DOUGH.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(ExtraDelightItems.SALAD_FEAST_ITEM.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(Fermentation.SALAMI_MIX.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(ExtraDelightItems.SEAWEED_SALAD.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(Fermentation.SHIRAZI_SALAD.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(ExtraDelightItems.STIFF_PEAKS.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(ExtraDelightItems.SUGAR_COOKIE_DOUGH.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(ExtraDelightItems.SWEET_BERRY_COOKIE_DOUGH.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(SummerCitrus.THAI_MELON_SALAD.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(ExtraDelightItems.SALAD.get());
        tag(EDTFCTags.DYNAMIC_FOOD).add(SummerCitrus.MELON_FRUIT_SALAD_SERVING.get());

        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/boiled_eggs"))).add(TFCItems.FOOD.get(Food.BOILED_EGG).get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/egg_or_yolk"))).addTag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/eggs"))).add(ExtraDelightItems.EGG_YOLK.get());
        tag(ItemTags.create(ExtraDelightTFC.modLoc("mixing/orange"))).add(TFCItems.FOOD.get(Food.ORANGE).get());
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
