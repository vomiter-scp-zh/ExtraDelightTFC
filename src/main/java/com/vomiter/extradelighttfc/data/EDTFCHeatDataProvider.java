package com.vomiter.extradelighttfc.data;

import com.google.gson.JsonObject;
import com.vomiter.extradelight.registry.ExtraDelightItems;
import com.vomiter.extradelighttfc.ExtraDelightTFC;
import java.util.concurrent.CompletableFuture;
import net.dries007.tfc.common.items.Food;
import net.dries007.tfc.common.items.TFCItems;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.crafting.Ingredient;

public final class EDTFCHeatDataProvider implements DataProvider {
    private final PackOutput.PathProvider paths;

    public EDTFCHeatDataProvider(PackOutput output) {
        paths = output.createPathProvider(PackOutput.Target.DATA_PACK, "tfc/item_heats");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        JsonObject corn = new JsonObject();
        corn.add("ingredient", Ingredient.of(TFCItems.FOOD.get(Food.MAIZE_GRAIN).get(),
                ExtraDelightItems.POPCORN.get(), ExtraDelightItems.COOKED_CORN.get()).toJson());
        corn.addProperty("heat_capacity", 1.0F);
        return DataProvider.saveStable(cache, corn, paths.json(ExtraDelightTFC.modLoc("corn")));
    }

    @Override public String getName() { return "EDTFC item heat data"; }
}
