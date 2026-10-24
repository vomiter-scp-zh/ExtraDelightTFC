package com.vomiter.extradelighttfc.registry;

import com.vomiter.extradelighttfc.ExtraDelightTFC;
import com.vomiter.extradelighttfc.compat.EDTFCFirmaLifeCompat;
import java.util.HashSet;
import java.util.Set;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.capabilities.food.FoodTrait;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.registries.ForgeRegistries;

public final class EDTFCFoodTraits {
    public static final ResourceLocation DRIED_ID = ExtraDelightTFC.modLoc("dried");
    public static final ResourceLocation FIRMALIFE_DRIED_ID = ResourceLocation.fromNamespaceAndPath("firmalife", "dried");
    public static final FoodTrait DRIED = FoodTrait.register(DRIED_ID,
            new FoodTrait(0.5F, "extradelighttfc.tooltip.food_trait.dried"));

    private static volatile Set<Item> firmalifeDryableFoods = Set.of();
    private static final ThreadLocal<Item> LOADING_ITEM = new ThreadLocal<>();

    private EDTFCFoodTraits() {}

    public static Item pushLoadingItem(Item item) {
        Item previous = LOADING_ITEM.get();
        LOADING_ITEM.set(item);
        return previous;
    }

    public static void restoreLoadingItem(Item item) {
        if (item == null) LOADING_ITEM.remove();
        else LOADING_ITEM.set(item);
    }

    public static void reloadDryingFoods(TagsUpdatedEvent event) {
        Set<Item> eligible = new HashSet<>();
        if (FoodTrait.getTrait(FIRMALIFE_DRIED_ID) != null) {
            for (Item item : ForgeRegistries.ITEMS) {
                if (EDTFCFirmaLifeCompat.canDryFood(item)) eligible.add(item);
            }
        }
        firmalifeDryableFoods = Set.copyOf(eligible);
    }

    public static boolean canUseFirmalifeDried(Item item) {
        return item != null && firmalifeDryableFoods.contains(item);
    }

    public static FoodTrait dried(Item item) {
        return canUseFirmalifeDried(item) ? dried() : DRIED;
    }

    public static void init() {}

    // Query the shared registry after mod construction; no optional classes are linked.
    public static FoodTrait dried() {
        FoodTrait firmalife = FoodTrait.getTrait(FIRMALIFE_DRIED_ID);
        return firmalife != null ? firmalife : DRIED;
    }

    public static boolean isDried(ItemStack stack) {
        return FoodCapability.hasTrait(stack, DRIED) || FoodCapability.hasTrait(stack, dried());
    }

    public static ResourceLocation resolveSavedId(ResourceLocation id) {
        return DRIED_ID.equals(id) ? FoodTrait.getId(dried(LOADING_ITEM.get())) : id;
    }
}
