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
