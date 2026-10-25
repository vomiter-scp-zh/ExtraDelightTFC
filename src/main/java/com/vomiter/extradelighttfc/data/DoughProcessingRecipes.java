package com.vomiter.extradelighttfc.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.vomiter.extradelight.registry.ExtraDelightItems;
import com.vomiter.extradelight.registry.ExtraDelightRecipes;
import com.vomiter.extradelighttfc.ExtraDelightTFC;
import com.eerussianguy.firmalife.common.recipes.FLRecipeSerializers;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.dries007.tfc.common.recipes.TFCRecipeSerializers;
import net.dries007.tfc.common.recipes.ingredients.NotRottenIngredient;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.registry.ModItems;

public final class DoughProcessingRecipes {
    public static final int COOKIES_PER_DOUGH = 4;
    public record Cookie(String name, Supplier<? extends Item> dough, Supplier<? extends Item> baked, int count, int time) {}
    public static final List<Cookie> COOKIES = List.of(
            new Cookie("apple", ExtraDelightItems.APPLE_COOKIE_DOUGH, ExtraDelightItems.APPLE_COOKIE, COOKIES_PER_DOUGH, 400),
            new Cookie("gingerbread", ExtraDelightItems.GINGERBREAD_COOKIE_DOUGH, ExtraDelightItems.GINGERBREAD_COOKIE, COOKIES_PER_DOUGH, 800),
            new Cookie("nut_butter", ExtraDelightItems.NUT_BUTTER_COOKIE_DOUGH, ExtraDelightItems.NUT_BUTTER_COOKIE, COOKIES_PER_DOUGH, 400),
            new Cookie("pumpkin", ExtraDelightItems.PUMPKIN_COOKIE_DOUGH, ExtraDelightItems.PUMPKIN_COOKIE, COOKIES_PER_DOUGH, 400),
            new Cookie("sugar", ExtraDelightItems.SUGAR_COOKIE_DOUGH, ExtraDelightItems.SUGAR_COOKIE, COOKIES_PER_DOUGH, 800),
            new Cookie("sweet_berry", ExtraDelightItems.SWEET_BERRY_COOKIE_DOUGH, ModItems.SWEET_BERRY_COOKIE, COOKIES_PER_DOUGH, 400),
            new Cookie("sugar_cookie_steve", ExtraDelightItems.RAW_SUGAR_COOKIE_STEVE, ExtraDelightItems.SUGAR_COOKIE_STEVE, 1, 800),
            new Cookie("sugar_cookie_alex", ExtraDelightItems.RAW_SUGAR_COOKIE_ALEX, ExtraDelightItems.SUGAR_COOKIE_ALEX, 1, 800),
            new Cookie("sugar_cookie_villager", ExtraDelightItems.RAW_SUGAR_COOKIE_VILLAGER, ExtraDelightItems.SUGAR_COOKIE_VILLAGER, 1, 800),
            new Cookie("sugar_cookie_creeper", ExtraDelightItems.RAW_SUGAR_COOKIE_CREEPER, ExtraDelightItems.SUGAR_COOKIE_CREEPER, 1, 800),
            new Cookie("sugar_cookie_sword", ExtraDelightItems.RAW_SUGAR_COOKIE_SWORD, ExtraDelightItems.SUGAR_COOKIE_SWORD, 1, 800),
            new Cookie("sugar_cookie_pickaxe", ExtraDelightItems.RAW_SUGAR_COOKIE_PICKAXE, ExtraDelightItems.SUGAR_COOKIE_PICKAXE, 1, 800),
            new Cookie("sugar_cookie_diamond", ExtraDelightItems.RAW_SUGAR_COOKIE_DIAMOND, ExtraDelightItems.SUGAR_COOKIE_DIAMOND, 1, 800),
            new Cookie("sugar_cookie_emerald", ExtraDelightItems.RAW_SUGAR_COOKIE_EMERALD, ExtraDelightItems.SUGAR_COOKIE_EMERALD, 1, 800),
            new Cookie("gingerbread_steve", ExtraDelightItems.RAW_GINGERBREAD_STEVE, ExtraDelightItems.GINGERBREAD_STEVE, 1, 800),
            new Cookie("gingerbread_alex", ExtraDelightItems.RAW_GINGERBREAD_ALEX, ExtraDelightItems.GINGERBREAD_ALEX, 1, 800),
            new Cookie("gingerbread_villager", ExtraDelightItems.RAW_GINGERBREAD_VILLAGER, ExtraDelightItems.GINGERBREAD_VILLAGER, 1, 800),
            new Cookie("gingerbread_creeper", ExtraDelightItems.RAW_GINGERBREAD_CREEPER, ExtraDelightItems.GINGERBREAD_CREEPER, 1, 800),
            new Cookie("gingerbread_sword", ExtraDelightItems.RAW_GINGERBREAD_SWORD, ExtraDelightItems.GINGERBREAD_SWORD, 1, 800),
            new Cookie("gingerbread_pickaxe", ExtraDelightItems.RAW_GINGERBREAD_PICKAXE, ExtraDelightItems.GINGERBREAD_PICKAXE, 1, 800),
            new Cookie("gingerbread_diamond", ExtraDelightItems.RAW_GINGERBREAD_DIAMOND, ExtraDelightItems.GINGERBREAD_DIAMOND, 1, 800),
            new Cookie("gingerbread_emerald", ExtraDelightItems.RAW_GINGERBREAD_EMERALD, ExtraDelightItems.GINGERBREAD_EMERALD, 1, 800));

    private DoughProcessingRecipes() {}
    public static boolean isCookieBaking(ResourceLocation id) {
        return id.getNamespace().equals(ExtraDelightTFC.MOD_ID) && id.getPath().startsWith("baking/ed_oven/");
    }
    public static void build(Consumer<FinishedRecipe> out) {
        for (Cookie cookie : COOKIES) {
            if (cookie.count == 1) {
                Supplier<? extends Item> parent = cookie.name.startsWith("sugar_cookie_")
                        ? ExtraDelightItems.SUGAR_COOKIE_DOUGH : ExtraDelightItems.GINGERBREAD_COOKIE_DOUGH;
                shape(out, cookie.name, Ingredient.of(parent.get()), new ItemStack(cookie.dough.get(), COOKIES_PER_DOUGH));
            }
            bake(out, cookie, false);
            bake(out, cookie, true);
            for (int n = 1; n <= Math.min(9, 64 / cookie.count); n++) oven(out, cookie, n);
        }
        Ingredient dough = Ingredient.of(ItemTags.create(ResourceLocation.fromNamespaceAndPath("tfc", "foods/dough")));
        shape(out, "raw_pasta", dough, new ItemStack(ModItems.RAW_PASTA.get()));
        shape(out, "macaroni", dough, new ItemStack(ExtraDelightItems.MACARONI.get()));
        shape(out, "penne", dough, new ItemStack(ExtraDelightItems.PENNE.get()));
        shape(out, "lasagna_noodles", dough, new ItemStack(ExtraDelightItems.LASAGNA_NOODLES.get()));
    }
    private static void shape(Consumer<FinishedRecipe> out, String name, Ingredient input, ItemStack result) {
        JsonObject json = new JsonObject();
        json.add("ingredient", NotRottenIngredient.of(input).toJson());
        json.add("result", stack(result));
        out.accept(new Result(ExtraDelightTFC.modLoc("dough_shaping/" + name), ExtraDelightRecipes.DOUGH_SHAPING_SERIALIZER.get(), json));
    }
    private static void bake(Consumer<FinishedRecipe> out, Cookie cookie, boolean fl) {
        JsonObject json = new JsonObject();
        json.add("ingredient", NotRottenIngredient.of(Ingredient.of(cookie.dough.get())).toJson());
        JsonObject provider = new JsonObject();
        provider.add("stack", stack(new ItemStack(cookie.baked.get(), cookie.count)));
        JsonArray modifiers = new JsonArray(); modifiers.add(ExtraDelightTFC.modLoc("portion_food").toString());
        provider.add("modifiers", modifiers);
        json.add("result_item", provider);
        json.addProperty("temperature", 200);
        if (fl) {
            json.addProperty("duration", cookie.time);
            JsonObject condition = new JsonObject();
            condition.addProperty("type", "forge:mod_loaded");
            condition.addProperty("modid", "firmalife");
            JsonArray conditions = new JsonArray(); conditions.add(condition); json.add("conditions", conditions);
        }
        out.accept(new Result(ExtraDelightTFC.modLoc("baking/" + (fl ? "fl_oven/" : "heating/") + cookie.name),
                fl ? FLRecipeSerializers.OVEN.get() : TFCRecipeSerializers.HEATING.get(), json));
    }
    private static void oven(Consumer<FinishedRecipe> out, Cookie cookie, int count) {
        JsonObject json = new JsonObject();
        json.addProperty("consumeContainer", false);
        json.add("container", stack(new ItemStack(ExtraDelightItems.SHEET.get())));
        json.addProperty("cookingtime", cookie.time);
        json.addProperty("experience", 0.1);
        JsonArray ingredients = new JsonArray();
        for (int i = 0; i < count; i++) ingredients.add(NotRottenIngredient.of(Ingredient.of(cookie.dough.get())).toJson());
        json.add("ingredients", ingredients);
        json.add("result", stack(new ItemStack(cookie.baked.get(), cookie.count * count)));
        out.accept(new Result(ExtraDelightTFC.modLoc("baking/ed_oven/" + cookie.name + "_" + count),
                ExtraDelightRecipes.OVEN_SERIALIZER.get(), json));
    }
    private static JsonObject stack(ItemStack stack) {
        JsonObject json = new JsonObject();
        json.addProperty("item", ForgeRegistries.ITEMS.getKey(stack.getItem()).toString());
        json.addProperty("count", stack.getCount());
        return json;
    }
    private record Result(ResourceLocation id, RecipeSerializer<?> type, JsonObject json) implements FinishedRecipe {
        @Override public void serializeRecipeData(JsonObject target) { json.entrySet().forEach(entry -> target.add(entry.getKey(), entry.getValue())); }
        @Override public ResourceLocation getId() { return id; }
        @Override public RecipeSerializer<?> getType() { return type; }
        @Override public @Nullable JsonObject serializeAdvancement() { return null; }
        @Override public @Nullable ResourceLocation getAdvancementId() { return null; }
    }
}
