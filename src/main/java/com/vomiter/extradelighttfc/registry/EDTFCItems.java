package com.vomiter.extradelighttfc.registry;

import com.vomiter.extradelight.registry.ExtraDelightTabs;
import com.vomiter.extradelighttfc.ExtraDelightTFC;
import net.dries007.tfc.common.TFCTiers;
import net.dries007.tfc.util.Metal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TieredItem;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public final class EDTFCItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ExtraDelightTFC.MOD_ID);
    public static final RegistryObject<Item> STONE_PESTLE = ITEMS.register("pestle/stone",
            () -> new TieredItem(TFCTiers.IGNEOUS_INTRUSIVE, new Item.Properties()));
    public static final Map<Metal.Default, RegistryObject<Item>> METAL_PESTLES = registerMetalPestles();

    private EDTFCItems() {}

    private static Map<Metal.Default, RegistryObject<Item>> registerMetalPestles() {
        Map<Metal.Default, RegistryObject<Item>> items = new EnumMap<>(Metal.Default.class);
        for (Metal.Default metal : Metal.Default.values()) {
            if (metal.hasTools()) {
                items.put(metal, ITEMS.register("pestle/" + metal.getSerializedName(),
                        () -> new TieredItem(metal.toolTier(), new Item.Properties().rarity(metal.getRarity()))));
            }
        }
        return Collections.unmodifiableMap(items);
    }

    public static void addCreativeItems(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == ExtraDelightTabs.TAB.get()) {
            event.accept(STONE_PESTLE.get());
            METAL_PESTLES.values().forEach(pestle -> event.accept(pestle.get()));
        }
    }
}
