package com.vomiter.extradelighttfc.data;

import com.vomiter.extradelight.ExtraDelight;
import com.vomiter.extradelighttfc.ExtraDelightTFC;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

public class EDTFCTags {
    public static final TagKey<Item> COOKIE_BAKING_ITEMS = ItemTags.create(ExtraDelightTFC.modLoc("cookie_baking_items"));
    public static final TagKey<Item> EXTRA_NUTRIENT_AFTER_HEATING = ItemTags.create(ExtraDelightTFC.modLoc("extra_nutrient_after_heating"));
    public static final TagKey<Item> DRYABLE_FOODS = ItemTags.create(ExtraDelightTFC.modLoc("dryable_foods"));
    public static final TagKey<Item> FISH_FLAKES_INPUTS = ItemTags.create(ExtraDelightTFC.modLoc("fish_flakes_inputs"));
    public static final TagKey<Item> SEAWEED_PASTE_INPUTS = ItemTags.create(ExtraDelightTFC.modLoc("seaweed_paste_inputs"));

    static TagKey<Fluid> FLUID_INGREDIENT
            = FluidTags.create(
                    ResourceLocation.fromNamespaceAndPath("tfc", "ingredients")
    );

    static TagKey<Item> DYNAMIC_FOOD
            = ItemTags.create(
            ExtraDelightTFC.modLoc("dynamic_food")
    );

    static TagKey<Item> PROCESSED_ONION
            = ItemTags.create(
                    ResourceLocation
                            .fromNamespaceAndPath(ExtraDelight.MOD_ID, "processed/onion"));
    static TagKey<Item> PROCESSED_APPLE
            = ItemTags.create(
            ResourceLocation
                    .fromNamespaceAndPath(ExtraDelight.MOD_ID, "processed/apple"));
    static TagKey<Item> PROCESSED_POTATO
            = ItemTags.create(
            ResourceLocation
                    .fromNamespaceAndPath(ExtraDelight.MOD_ID, "processed/potato"));
    static TagKey<Item> PROCESSED_TOMATO
            = ItemTags.create(
            ResourceLocation
                    .fromNamespaceAndPath(ExtraDelight.MOD_ID, "processed/tomato"));



}
