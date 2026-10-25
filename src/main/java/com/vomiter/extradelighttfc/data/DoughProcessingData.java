package com.vomiter.extradelighttfc.data;

import com.google.gson.JsonObject;
import java.util.concurrent.CompletableFuture;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.common.registry.ModItems;

/** Explicit compatibility override plus heat capability for the complete cookie chain. */
public final class DoughProcessingData implements DataProvider {
    private final PackOutput output;
    public DoughProcessingData(PackOutput output) { this.output = output; }
    @Override public CompletableFuture<?> run(CachedOutput cache) {
        JsonObject pasta = new JsonObject();
        JsonObject pastaIngredient = new JsonObject(); pastaIngredient.addProperty("item", ForgeRegistries.ITEMS.getKey(ModItems.RAW_PASTA.get()).toString());
        pasta.add("ingredient", pastaIngredient); pasta.addProperty("type", "dynamic");
        JsonObject heat = new JsonObject();
        JsonObject heatIngredient = new JsonObject(); heatIngredient.addProperty("tag", EDTFCTags.COOKIE_BAKING_ITEMS.location().toString());
        heat.add("ingredient", heatIngredient); heat.addProperty("heat_capacity", 1.0);
        return CompletableFuture.allOf(
                DataProvider.saveStable(cache, pasta, output.getOutputFolder().resolve("data/survivorsdelight/tfc/food_items/cut/raw_pasta.json")),
                DataProvider.saveStable(cache, heat, output.getOutputFolder().resolve("data/extradelighttfc/tfc/item_heats/cookies.json")));
    }
    @Override public String getName() { return "EDTFC dough food and heat compatibility"; }
}
