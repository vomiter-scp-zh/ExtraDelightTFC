package com.vomiter.extradelighttfc.data;

import com.google.gson.JsonObject;
import com.vomiter.extradelight.registry.ExtraDelightFluids;
import com.vomiter.extradelighttfc.ExtraDelightTFC;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;

/** Fixed nutrition, expressed per TFC's reference serving of 25 mB. */
public final class EDTFCDrinkableProvider implements DataProvider {
    private final PackOutput.PathProvider paths;

    public EDTFCDrinkableProvider(PackOutput output) {
        paths = output.createPathProvider(PackOutput.Target.DATA_PACK, "tfc/drinkables");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        List<CompletableFuture<?>> writes = new ArrayList<>();
        // Totals per 500 mB bottle. Mayo uses two yolks (the registered egg-yolk food data).
        writes.add(save(output, "mayo", ExtraDelightFluids.MAYO.FLUID.get(), 500, 5, 0.6f, 0, 1.6f, 0.2f));
        // Two eggs (boiled-egg fallback) plus 250 mB milk: 3 protein + 10.5 dairy.
        writes.add(save(output, "egg_mix", ExtraDelightFluids.EGG_MIX.FLUID.get(), 500, 6, 4, 0, 3, 10.5f));
        // A 250 mB milk/cream input yields one 500 mB whipped-cream portion.
        writes.add(save(output, "whipped_cream", ExtraDelightFluids.WHIPPED_CREAM.FLUID.get(), 500, 5, 0, 0, 0, 10));
        // Egg separation: half a cooked egg becomes 500 mB egg white.
        writes.add(save(output, "egg_white", ExtraDelightFluids.EGG_WHITE.FLUID.get(), 500, 0, 0.3f, 0, 0.8f, 0.1f));
        // Replaces the former bottle-food fallback used by nut-butter cookie dough.
        writes.add(save(output, "peanut_butter", ExtraDelightFluids.NUT_BUTTER.FLUID.get(), 500, 0, 3, 0, 1, 0));
        // FL cream is made from milk at 1:1 volume. Optional tag avoids a hard FL dependency.
        JsonObject cream = definition(25, 10, 0, 0, 0, 1);
        JsonObject ingredient = new JsonObject();
        ingredient.addProperty("tag", "extradelighttfc:mixing_cream");
        cream.add("ingredient", ingredient);
        writes.add(DataProvider.saveStable(output, cream, paths.json(ExtraDelightTFC.modLoc("mixing_cream"))));
        return CompletableFuture.allOf(writes.toArray(CompletableFuture[]::new));
    }

    private CompletableFuture<?> save(CachedOutput output, String name, Fluid fluid, int volume,
                                       int thirst, float saturation, float vegetables, float protein, float dairy) {
        JsonObject json = definition(volume, thirst, saturation, vegetables, protein, dairy);
        json.addProperty("ingredient", ForgeRegistries.FLUIDS.getKey(fluid).toString());
        return DataProvider.saveStable(output, json, paths.json(ExtraDelightTFC.modLoc(name)));
    }

    private JsonObject definition(int volume, int thirst, float saturation, float vegetables, float protein, float dairy) {
        float scale = 25f / volume;
        JsonObject json = new JsonObject();
        json.addProperty("thirst", thirst);
        json.addProperty("consume_chance", 0);
        json.addProperty("may_drink_when_full", false);
        JsonObject food = new JsonObject();
        // Fractional hunger cannot be represented by Drinkable; follow TFC milk's zero-hunger convention.
        food.addProperty("hunger", 0);
        food.addProperty("saturation", saturation * scale);
        food.addProperty("vegetables", vegetables * scale);
        food.addProperty("protein", protein * scale);
        food.addProperty("dairy", dairy * scale);
        json.add("food", food);
        return json;
    }

    @Override
    public String getName() {
        return "Extra Delight TFC drinkables";
    }
}
