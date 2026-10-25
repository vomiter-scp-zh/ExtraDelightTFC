package com.vomiter.extradelighttfc.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.vomiter.extradelight.registry.ExtraDelightItems;
import com.vomiter.extradelight.registry.ExtraDelightFluids;
import com.vomiter.extradelight.registry.ExtraDelightRecipes;
import com.vomiter.extradelight.registry.SummerCitrus;
import com.vomiter.extradelight.registry.Fermentation;
import com.vomiter.extradelighttfc.ExtraDelightTFC;
import java.util.List;
import java.util.function.Consumer;
import net.dries007.tfc.common.fluids.SimpleFluid;
import net.dries007.tfc.common.fluids.TFCFluids;
import net.dries007.tfc.common.items.Food;
import net.dries007.tfc.common.items.Powder;
import net.dries007.tfc.common.items.TFCItems;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.registry.ModItems;

/** Fixed ED dishes, with TFC ingredients; deliberately excludes chocolate and frozen desserts. */
public final class EDTFCMixingRecipes {
    private static final TagKey<Item> TAG_MIXING_EGG_OR_YOLK = ItemTags.create(ExtraDelightTFC.modLoc("mixing/egg_or_yolk"));
    private static final TagKey<Item> TAG_EXTRADELIGHTTFC_MIXING_APPLE = ItemTags.create(ResourceLocation.tryParse("extradelighttfc:mixing/apple"));
    private static final TagKey<Item> TAG_EXTRADELIGHTTFC_MIXING_BEETROOT = ItemTags.create(ResourceLocation.tryParse("extradelighttfc:mixing/beetroot"));
    private static final TagKey<Item> TAG_EXTRADELIGHTTFC_MIXING_BERRIES = ItemTags.create(ResourceLocation.tryParse("extradelighttfc:mixing/berries"));
    private static final TagKey<Item> TAG_EXTRADELIGHTTFC_MIXING_BOILED_EGGS = ItemTags.create(ResourceLocation.tryParse("extradelighttfc:mixing/boiled_eggs"));
    private static final TagKey<Item> TAG_EXTRADELIGHTTFC_MIXING_BUTTER = ItemTags.create(ResourceLocation.tryParse("extradelighttfc:mixing/butter"));
    private static final TagKey<Item> TAG_EXTRADELIGHTTFC_MIXING_CABBAGE = ItemTags.create(ResourceLocation.tryParse("extradelighttfc:mixing/cabbage"));
    private static final TagKey<Item> TAG_EXTRADELIGHTTFC_MIXING_CARROT = ItemTags.create(ResourceLocation.tryParse("extradelighttfc:mixing/carrot"));
    private static final TagKey<Item> TAG_EXTRADELIGHTTFC_MIXING_CHEESE = ItemTags.create(ResourceLocation.tryParse("extradelighttfc:mixing/cheese"));
    private static final TagKey<Item> TAG_EXTRADELIGHTTFC_MIXING_CHILI = ItemTags.create(ResourceLocation.tryParse("extradelighttfc:mixing/chili"));
    private static final TagKey<Item> TAG_EXTRADELIGHTTFC_MIXING_COOKED_FISH = ItemTags.create(ResourceLocation.tryParse("extradelighttfc:mixing/cooked_fish"));
    private static final TagKey<Item> TAG_EXTRADELIGHTTFC_MIXING_COOKED_MEAT = ItemTags.create(ResourceLocation.tryParse("extradelighttfc:mixing/cooked_meat"));
    private static final TagKey<Item> TAG_EXTRADELIGHTTFC_MIXING_CUCUMBER = ItemTags.create(ResourceLocation.tryParse("extradelighttfc:mixing/cucumber"));
    private static final TagKey<Item> TAG_EXTRADELIGHTTFC_MIXING_EGGS = ItemTags.create(ResourceLocation.tryParse("extradelighttfc:mixing/eggs"));
    private static final TagKey<Item> TAG_EXTRADELIGHTTFC_MIXING_FLOUR = ItemTags.create(ResourceLocation.tryParse("extradelighttfc:mixing/flour"));
    private static final TagKey<Item> TAG_EXTRADELIGHTTFC_MIXING_GARLIC = ItemTags.create(ResourceLocation.tryParse("extradelighttfc:mixing/garlic"));
    private static final TagKey<Item> TAG_EXTRADELIGHTTFC_MIXING_GINGER = ItemTags.create(ResourceLocation.tryParse("extradelighttfc:mixing/ginger"));
    private static final TagKey<Item> TAG_EXTRADELIGHTTFC_MIXING_GRAPEFRUIT = ItemTags.create(ResourceLocation.tryParse("extradelighttfc:mixing/grapefruit"));
    private static final TagKey<Item> TAG_EXTRADELIGHTTFC_MIXING_LEMON = ItemTags.create(ResourceLocation.tryParse("extradelighttfc:mixing/lemon"));
    private static final TagKey<Item> TAG_EXTRADELIGHTTFC_MIXING_LIME = ItemTags.create(ResourceLocation.tryParse("extradelighttfc:mixing/lime"));
    private static final TagKey<Item> TAG_EXTRADELIGHTTFC_MIXING_MELON = ItemTags.create(ResourceLocation.tryParse("extradelighttfc:mixing/melon"));
    private static final TagKey<Item> TAG_EXTRADELIGHTTFC_MIXING_ONION = ItemTags.create(ResourceLocation.tryParse("extradelighttfc:mixing/onion"));
    private static final TagKey<Item> TAG_EXTRADELIGHTTFC_MIXING_ORANGE = ItemTags.create(ResourceLocation.tryParse("extradelighttfc:mixing/orange"));
    private static final TagKey<Item> TAG_EXTRADELIGHTTFC_MIXING_SWEETENER = ItemTags.create(ResourceLocation.tryParse("extradelighttfc:mixing/sweetener"));
    private static final TagKey<Item> TAG_EXTRADELIGHTTFC_MIXING_TOMATO = ItemTags.create(ResourceLocation.tryParse("extradelighttfc:mixing/tomato"));
    private static final TagKey<Item> TAG_EXTRADELIGHTTFC_MIXING_VEGETABLES = ItemTags.create(ResourceLocation.tryParse("extradelighttfc:mixing/vegetables"));
    private static final TagKey<Item> TAG_FORGE_ALFREDO_SAUCE = ItemTags.create(ResourceLocation.tryParse("forge:alfredo_sauce"));
    private static final TagKey<Item> TAG_FORGE_BEEF_GROUND = ItemTags.create(ResourceLocation.tryParse("forge:beef/ground"));
    private static final TagKey<Item> TAG_FORGE_CACTUS_COOKED = ItemTags.create(ResourceLocation.tryParse("forge:cactus/cooked"));
    private static final TagKey<Item> TAG_FORGE_CHICKEN_WING_COOKED = ItemTags.create(ResourceLocation.tryParse("forge:chicken/wing/cooked"));
    private static final TagKey<Item> TAG_FORGE_CHILI_POWDER = ItemTags.create(ResourceLocation.tryParse("forge:chili/powder"));
    private static final TagKey<Item> TAG_FORGE_CINNAMON_GROUND = ItemTags.create(ResourceLocation.tryParse("forge:cinnamon/ground"));
    private static final TagKey<Item> TAG_FORGE_CONDIMENTS_HOT_SAUCE = ItemTags.create(ResourceLocation.tryParse("forge:condiments/hot_sauce"));
    private static final TagKey<Item> TAG_FORGE_CONDIMENTS_SOY_SAUCE = ItemTags.create(ResourceLocation.tryParse("forge:condiments/soy_sauce"));
    private static final TagKey<Item> TAG_FORGE_CROPS_COFFEE_GROUND = ItemTags.create(ResourceLocation.tryParse("forge:crops/coffee/ground"));
    private static final TagKey<Item> TAG_FORGE_CROPS_PEANUT_COOKED = ItemTags.create(ResourceLocation.tryParse("forge:crops/peanut/cooked"));
    private static final TagKey<Item> TAG_FORGE_CROUTONS = ItemTags.create(ResourceLocation.tryParse("forge:croutons"));
    private static final TagKey<Item> TAG_FORGE_DYES = ItemTags.create(ResourceLocation.tryParse("forge:dyes"));
    private static final TagKey<Item> TAG_FORGE_DYES_BLACK = ItemTags.create(ResourceLocation.tryParse("forge:dyes/black"));
    private static final TagKey<Item> TAG_FORGE_DYES_BLUE = ItemTags.create(ResourceLocation.tryParse("forge:dyes/blue"));
    private static final TagKey<Item> TAG_FORGE_DYES_BROWN = ItemTags.create(ResourceLocation.tryParse("forge:dyes/brown"));
    private static final TagKey<Item> TAG_FORGE_DYES_CYAN = ItemTags.create(ResourceLocation.tryParse("forge:dyes/cyan"));
    private static final TagKey<Item> TAG_FORGE_DYES_GRAY = ItemTags.create(ResourceLocation.tryParse("forge:dyes/gray"));
    private static final TagKey<Item> TAG_FORGE_DYES_GREEN = ItemTags.create(ResourceLocation.tryParse("forge:dyes/green"));
    private static final TagKey<Item> TAG_FORGE_DYES_LIGHT_BLUE = ItemTags.create(ResourceLocation.tryParse("forge:dyes/light_blue"));
    private static final TagKey<Item> TAG_FORGE_DYES_LIGHT_GRAY = ItemTags.create(ResourceLocation.tryParse("forge:dyes/light_gray"));
    private static final TagKey<Item> TAG_FORGE_DYES_LIME = ItemTags.create(ResourceLocation.tryParse("forge:dyes/lime"));
    private static final TagKey<Item> TAG_FORGE_DYES_MAGENTA = ItemTags.create(ResourceLocation.tryParse("forge:dyes/magenta"));
    private static final TagKey<Item> TAG_FORGE_DYES_ORANGE = ItemTags.create(ResourceLocation.tryParse("forge:dyes/orange"));
    private static final TagKey<Item> TAG_FORGE_DYES_PINK = ItemTags.create(ResourceLocation.tryParse("forge:dyes/pink"));
    private static final TagKey<Item> TAG_FORGE_DYES_PURPLE = ItemTags.create(ResourceLocation.tryParse("forge:dyes/purple"));
    private static final TagKey<Item> TAG_FORGE_DYES_RED = ItemTags.create(ResourceLocation.tryParse("forge:dyes/red"));
    private static final TagKey<Item> TAG_FORGE_DYES_WHITE = ItemTags.create(ResourceLocation.tryParse("forge:dyes/white"));
    private static final TagKey<Item> TAG_FORGE_DYES_YELLOW = ItemTags.create(ResourceLocation.tryParse("forge:dyes/yellow"));
    private static final TagKey<Item> TAG_FORGE_FAT = ItemTags.create(ResourceLocation.tryParse("forge:fat"));
    private static final TagKey<Item> TAG_FORGE_FISH_FLAKES = ItemTags.create(ResourceLocation.tryParse("forge:fish_flakes"));
    private static final TagKey<Item> TAG_FORGE_FISH_SAUCE = ItemTags.create(ResourceLocation.tryParse("forge:fish_sauce"));
    private static final TagKey<Item> TAG_FORGE_FOODS_COOKED_CHICKEN = ItemTags.create(ResourceLocation.tryParse("forge:foods/cooked_chicken"));
    private static final TagKey<Item> TAG_FORGE_GELATIN = ItemTags.create(ResourceLocation.tryParse("forge:gelatin"));
    private static final TagKey<Item> TAG_FORGE_MALLOWROOT_POWDER = ItemTags.create(ResourceLocation.tryParse("forge:mallowroot/powder"));
    private static final TagKey<Item> TAG_FORGE_MINT = ItemTags.create(ResourceLocation.tryParse("forge:mint"));
    private static final TagKey<Item> TAG_FORGE_NUTS_COOKED = ItemTags.create(ResourceLocation.tryParse("forge:nuts/cooked"));
    private static final TagKey<Item> TAG_FORGE_PORK_SCRAP_COOKED = ItemTags.create(ResourceLocation.tryParse("forge:pork/scrap/cooked"));
    private static final TagKey<Item> TAG_FORGE_SPOONS = ItemTags.create(ResourceLocation.tryParse("forge:spoons"));

    private EDTFCMixingRecipes() {}

    public static void build(Consumer<FinishedRecipe> consumer) {
        save(consumer, "aglio_e_olio_mixing", new ItemStack(ExtraDelightItems.AGLIO_E_OLIO.get(), 1), new ItemStack(Items.BOWL, 1), 2, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluidTag("extradelighttfc:mixing_oils", 250)), null, Ingredient.of(ExtraDelightItems.COOKED_PASTA.get()), Ingredient.of(ExtraDelightItems.ROASTED_GARLIC.get()), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_CHILI), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_CHEESE));
        save(consumer, "aioli_cheaty", new ItemStack(ExtraDelightItems.AIOLI.get(), 1), new ItemStack(Items.GLASS_BOTTLE, 1), 8, Ingredient.of(ExtraDelightItems.WHISK.get()),
                List.of(fluid(ExtraDelightFluids.MAYO.FLUID.get(), 250)), null, Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_GARLIC));
        save(consumer, "aioli_from_scratch", new ItemStack(ExtraDelightItems.AIOLI.get(), 1), new ItemStack(Items.GLASS_BOTTLE, 1), 8, Ingredient.of(ExtraDelightItems.WHISK.get()),
                List.of(fluid(TFCFluids.SIMPLE_FLUIDS.get(SimpleFluid.VINEGAR).source().get(), 250), fluidTag("extradelighttfc:mixing_oils", 250)), null, Ingredient.of(TAG_MIXING_EGG_OR_YOLK), Ingredient.of(TAG_MIXING_EGG_OR_YOLK), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_GARLIC));
        save(consumer, "apple_cookie_dough", new ItemStack(ExtraDelightItems.APPLE_COOKIE_DOUGH.get(), 1), ItemStack.EMPTY, 4, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(), null, Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_FLOUR), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_SWEETENER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_BUTTER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_EGGS), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_APPLE), Ingredient.of(TAG_FORGE_CINNAMON_GROUND));
        save(consumer, "apple_cookie_dough_sugar", new ItemStack(ExtraDelightItems.APPLE_COOKIE_DOUGH.get(), 1), ItemStack.EMPTY, 4, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(), null, Ingredient.of(ExtraDelightItems.SUGAR_COOKIE_DOUGH.get()), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_APPLE), Ingredient.of(TAG_FORGE_CINNAMON_GROUND));
        save(consumer, "apple_slaw", new ItemStack(ExtraDelightItems.APPLE_SLAW.get(), 1), new ItemStack(Items.BOWL, 1), 4, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluid(ExtraDelightFluids.MAYO.FLUID.get(), 250), fluid(TFCFluids.SIMPLE_FLUIDS.get(SimpleFluid.VINEGAR).source().get(), 250)), null, Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_CABBAGE), Ingredient.of(ExtraDelightItems.SLICED_APPLE.get()));
        save(consumer, "beet_mint_salad", new ItemStack(ExtraDelightItems.BEET_MINT_SALAD.get(), 2), new ItemStack(Items.BOWL, 2), 4, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluidTag("extradelighttfc:mixing_oils", 250), fluid(TFCFluids.SIMPLE_FLUIDS.get(SimpleFluid.VINEGAR).source().get(), 250)), null, Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_BEETROOT), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_BEETROOT), Ingredient.of(TAG_FORGE_MINT), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_SWEETENER));
        save(consumer, "buttered_pasta", new ItemStack(ExtraDelightItems.BUTTERED_PASTA.get(), 1), new ItemStack(Items.BOWL, 1), 2, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(), null, Ingredient.of(ExtraDelightItems.COOKED_PASTA.get()), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_BUTTER));
        save(consumer, "cactus_salad", new ItemStack(ExtraDelightItems.CACTUS_SALAD.get(), 1), new ItemStack(Items.BOWL, 1), 2, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(), null, Ingredient.of(TAG_FORGE_CACTUS_COOKED), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_TOMATO), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_ONION), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_CHEESE));
        save(consumer, "carrot_salad", new ItemStack(ExtraDelightItems.CARROT_SALAD.get(), 1), new ItemStack(Items.BOWL, 1), 8, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluid(ExtraDelightFluids.MAYO.FLUID.get(), 250)), null, Ingredient.of(ExtraDelightItems.GRATED_CARROT.get()), Ingredient.of(ExtraDelightItems.GRATED_CARROT.get()), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_APPLE));
        save(consumer, "chicken_alfredo", new ItemStack(ExtraDelightItems.CHICKEN_ALFREDO.get(), 1), new ItemStack(Items.BOWL, 1), 2, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(), null, Ingredient.of(TAG_FORGE_ALFREDO_SAUCE), Ingredient.of(ExtraDelightItems.COOKED_PASTA.get()), Ingredient.of(TFCItems.FOOD.get(Food.COOKED_CHICKEN).get()));
        save(consumer, "citrus_onion_salad_mixing", new ItemStack(SummerCitrus.CITRUS_ONION_SALAD.get(), 3), new ItemStack(Items.BOWL, 3), 2, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluidTag("extradelighttfc:mixing_oils", 250), fluid(TFCFluids.SIMPLE_FLUIDS.get(SimpleFluid.VINEGAR).source().get(), 250), fluid(ExtraDelightFluids.ORANGE_JUICE.FLUID.get(), 250)), null, Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_GRAPEFRUIT), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_ORANGE), Ingredient.of(Items.HONEY_BOTTLE), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_ONION), Ingredient.of(TAG_FORGE_MINT), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_LIME));
        save(consumer, "coffee_jelly", new ItemStack(ExtraDelightItems.COFFEE_JELLY.get(), 2), new ItemStack(Items.GLASS_BOTTLE, 2), 4, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluid(ExtraDelightFluids.COFFEE.FLUID.get(), 250), fluid(ExtraDelightFluids.WHIPPED_CREAM.FLUID.get(), 250)), null, Ingredient.of(TAG_FORGE_GELATIN), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_SWEETENER));
        save(consumer, "cucumber_salad_mixing", new ItemStack(Fermentation.CUCUMBER_SALAD.get(), 2), new ItemStack(Items.BOWL, 2), 2, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluidTag("extradelighttfc:mixing_oils", 250), fluid(TFCFluids.SIMPLE_FLUIDS.get(SimpleFluid.VINEGAR).source().get(), 250)), null, Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_CUCUMBER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_GARLIC), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_GINGER), Ingredient.of(TAG_FORGE_CONDIMENTS_SOY_SAUCE));
        save(consumer, "dalgona_coffee_mixing", new ItemStack(SummerCitrus.DALGONA_COFFEE.get(), 1), new ItemStack(Items.GLASS_BOTTLE, 1), 8, Ingredient.of(ExtraDelightItems.WHISK.get()),
                List.of(fluid(Fluids.WATER, 50), fluidTag("tfc:milks", 250)), null, Ingredient.of(TAG_FORGE_CROPS_COFFEE_GROUND), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_SWEETENER));
        save(consumer, "egg_mix", new ItemStack(ExtraDelightItems.EGG_MIX.get(), 1), new ItemStack(Items.BOWL, 1), 8, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluidTag("tfc:milks", 250)), null, Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_EGGS), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_EGGS));
        save(consumer, "egg_salad", new ItemStack(ExtraDelightItems.EGG_SALAD.get(), 1), new ItemStack(Items.BOWL, 1), 8, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluid(ExtraDelightFluids.MAYO.FLUID.get(), 250)), null, Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_BOILED_EGGS), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_BOILED_EGGS));
        save(consumer, "eton_mess_mixing", new ItemStack(SummerCitrus.ETON_MESS.get(), 1), new ItemStack(Items.GLASS_BOTTLE, 1), 8, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluid(ExtraDelightFluids.WHIPPED_CREAM.FLUID.get(), 250)), null, Ingredient.of(SummerCitrus.MERINGUE.get()), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_SWEETENER), Ingredient.of(SummerCitrus.MERINGUE.get()), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_BERRIES), Ingredient.of(SummerCitrus.LEMON_ZEST.get()), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_ORANGE));
        save(consumer, "fish_salad", new ItemStack(ExtraDelightItems.FISH_SALAD.get(), 2), new ItemStack(Items.BOWL, 2), 8, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluid(ExtraDelightFluids.MAYO.FLUID.get(), 250)), null, Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_COOKED_FISH), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_COOKED_FISH));
        save(consumer, "frosting_black", new ItemStack(ExtraDelightItems.FROSTING_BLACK.get(), 4), ItemStack.EMPTY, 2, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluid(ExtraDelightFluids.WHIPPED_CREAM.FLUID.get(), 250)), null, Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_BUTTER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_SWEETENER), Ingredient.of(TAG_FORGE_DYES_BLACK));
        save(consumer, "frosting_blue", new ItemStack(ExtraDelightItems.FROSTING_BLUE.get(), 4), ItemStack.EMPTY, 2, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluid(ExtraDelightFluids.WHIPPED_CREAM.FLUID.get(), 250)), null, Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_BUTTER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_SWEETENER), Ingredient.of(TAG_FORGE_DYES_BLUE));
        save(consumer, "frosting_brown", new ItemStack(ExtraDelightItems.FROSTING_BROWN.get(), 4), ItemStack.EMPTY, 2, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluid(ExtraDelightFluids.WHIPPED_CREAM.FLUID.get(), 250)), null, Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_BUTTER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_SWEETENER), Ingredient.of(TAG_FORGE_DYES_BROWN));
        save(consumer, "frosting_cyan", new ItemStack(ExtraDelightItems.FROSTING_CYAN.get(), 4), ItemStack.EMPTY, 2, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluid(ExtraDelightFluids.WHIPPED_CREAM.FLUID.get(), 250)), null, Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_BUTTER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_SWEETENER), Ingredient.of(TAG_FORGE_DYES_CYAN));
        save(consumer, "frosting_gray", new ItemStack(ExtraDelightItems.FROSTING_GRAY.get(), 4), ItemStack.EMPTY, 2, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluid(ExtraDelightFluids.WHIPPED_CREAM.FLUID.get(), 250)), null, Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_BUTTER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_SWEETENER), Ingredient.of(TAG_FORGE_DYES_GRAY));
        save(consumer, "frosting_green", new ItemStack(ExtraDelightItems.FROSTING_GREEN.get(), 4), ItemStack.EMPTY, 2, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluid(ExtraDelightFluids.WHIPPED_CREAM.FLUID.get(), 250)), null, Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_BUTTER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_SWEETENER), Ingredient.of(TAG_FORGE_DYES_GREEN));
        save(consumer, "frosting_light_blue", new ItemStack(ExtraDelightItems.FROSTING_LIGHT_BLUE.get(), 4), ItemStack.EMPTY, 2, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluid(ExtraDelightFluids.WHIPPED_CREAM.FLUID.get(), 250)), null, Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_BUTTER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_SWEETENER), Ingredient.of(TAG_FORGE_DYES_LIGHT_BLUE));
        save(consumer, "frosting_light_gray", new ItemStack(ExtraDelightItems.FROSTING_LIGHT_GRAY.get(), 4), ItemStack.EMPTY, 2, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluid(ExtraDelightFluids.WHIPPED_CREAM.FLUID.get(), 250)), null, Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_BUTTER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_SWEETENER), Ingredient.of(TAG_FORGE_DYES_LIGHT_GRAY));
        save(consumer, "frosting_lime", new ItemStack(ExtraDelightItems.FROSTING_LIME.get(), 4), ItemStack.EMPTY, 2, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluid(ExtraDelightFluids.WHIPPED_CREAM.FLUID.get(), 250)), null, Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_BUTTER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_SWEETENER), Ingredient.of(TAG_FORGE_DYES_LIME));
        save(consumer, "frosting_magenta", new ItemStack(ExtraDelightItems.FROSTING_MAGENTA.get(), 4), ItemStack.EMPTY, 2, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluid(ExtraDelightFluids.WHIPPED_CREAM.FLUID.get(), 250)), null, Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_BUTTER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_SWEETENER), Ingredient.of(TAG_FORGE_DYES_MAGENTA));
        save(consumer, "frosting_orange", new ItemStack(ExtraDelightItems.FROSTING_ORANGE.get(), 4), ItemStack.EMPTY, 2, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluid(ExtraDelightFluids.WHIPPED_CREAM.FLUID.get(), 250)), null, Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_BUTTER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_SWEETENER), Ingredient.of(TAG_FORGE_DYES_ORANGE));
        save(consumer, "frosting_pink", new ItemStack(ExtraDelightItems.FROSTING_PINK.get(), 4), ItemStack.EMPTY, 2, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluid(ExtraDelightFluids.WHIPPED_CREAM.FLUID.get(), 250)), null, Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_BUTTER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_SWEETENER), Ingredient.of(TAG_FORGE_DYES_PINK));
        save(consumer, "frosting_purple", new ItemStack(ExtraDelightItems.FROSTING_PURPLE.get(), 4), ItemStack.EMPTY, 2, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluid(ExtraDelightFluids.WHIPPED_CREAM.FLUID.get(), 250)), null, Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_BUTTER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_SWEETENER), Ingredient.of(TAG_FORGE_DYES_PURPLE));
        save(consumer, "frosting_red", new ItemStack(ExtraDelightItems.FROSTING_RED.get(), 4), ItemStack.EMPTY, 2, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluid(ExtraDelightFluids.WHIPPED_CREAM.FLUID.get(), 250)), null, Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_BUTTER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_SWEETENER), Ingredient.of(TAG_FORGE_DYES_RED));
        save(consumer, "frosting_white", new ItemStack(ExtraDelightItems.FROSTING_WHITE.get(), 4), ItemStack.EMPTY, 2, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluid(ExtraDelightFluids.WHIPPED_CREAM.FLUID.get(), 250)), null, Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_BUTTER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_SWEETENER), Ingredient.of(TAG_FORGE_DYES_WHITE));
        save(consumer, "frosting_yellow", new ItemStack(ExtraDelightItems.FROSTING_YELLOW.get(), 4), ItemStack.EMPTY, 2, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluid(ExtraDelightFluids.WHIPPED_CREAM.FLUID.get(), 250)), null, Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_BUTTER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_SWEETENER), Ingredient.of(TAG_FORGE_DYES_YELLOW));
        save(consumer, "furikake_rice", new ItemStack(ExtraDelightItems.FURIKAKE.get(), 4), new ItemStack(Items.BOWL, 4), 4, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(), null, Ingredient.of(TFCItems.FOOD.get(Food.FRESH_SEAWEED).get()), Ingredient.of(TFCItems.FOOD.get(Food.FRESH_SEAWEED).get()), Ingredient.of(TAG_FORGE_FISH_FLAKES), Ingredient.of(TAG_FORGE_FISH_FLAKES));
        save(consumer, "gazpacho_mixing", new ItemStack(Fermentation.GAZPACHO.get(), 1), new ItemStack(Items.BOWL, 1), 8, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluidTag("extradelighttfc:mixing_oils", 250), fluid(TFCFluids.SIMPLE_FLUIDS.get(SimpleFluid.VINEGAR).source().get(), 250)), null, Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_TOMATO), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_CUCUMBER), Ingredient.of(ExtraDelightItems.BREAD_CRUMBS.get()));
        save(consumer, "gingerbread_cookie_dough", new ItemStack(ExtraDelightItems.GINGERBREAD_COOKIE_DOUGH.get(), 1), ItemStack.EMPTY, 4, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(), null, Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_FLOUR), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_SWEETENER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_BUTTER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_EGGS), Ingredient.of(ExtraDelightItems.GRATED_GINGER.get()), Ingredient.of(TAG_FORGE_CINNAMON_GROUND));
        save(consumer, "gingerbread_cookie_dough_sugar", new ItemStack(ExtraDelightItems.GINGERBREAD_COOKIE_DOUGH.get(), 1), ItemStack.EMPTY, 4, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(), null, Ingredient.of(ExtraDelightItems.SUGAR_COOKIE_DOUGH.get()), Ingredient.of(ExtraDelightItems.GRATED_GINGER.get()), Ingredient.of(TAG_FORGE_CINNAMON_GROUND));
        save(consumer, "grapefruit_beetroot_salad_mixing", new ItemStack(SummerCitrus.GRAPEFRUIT_BEETROOT_SALAD.get(), 2), new ItemStack(Items.BOWL, 2), 2, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluidTag("extradelighttfc:mixing_oils", 250)), null, Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_GRAPEFRUIT), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_BEETROOT), Ingredient.of(TFCItems.POWDERS.get(Powder.SALT).get()), Ingredient.of(Items.HONEY_BOTTLE));
        save(consumer, "gummies", new ItemStack(ExtraDelightItems.GUMMIES.get(), 4), ItemStack.EMPTY, 4, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluid(Fluids.WATER, 100)), null, Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_SWEETENER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_SWEETENER), Ingredient.of(TAG_FORGE_GELATIN), Ingredient.of(TAG_FORGE_GELATIN), Ingredient.of(TAG_FORGE_DYES), Ingredient.of(TAG_FORGE_DYES));
        save(consumer, "hot_wings_mixing", new ItemStack(Fermentation.HOT_WINGS.get(), 1), ItemStack.EMPTY, 2, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(), null, Ingredient.of(TAG_FORGE_CHICKEN_WING_COOKED), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_BUTTER), Ingredient.of(TAG_FORGE_CONDIMENTS_HOT_SAUCE));
        save(consumer, "lemonade_mixing", new ItemStack(SummerCitrus.LEMONADE.get(), 4), new ItemStack(Items.GLASS_BOTTLE, 4), 8, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluid(Fluids.WATER, 750), fluid(ExtraDelightFluids.LEMON_JUICE.FLUID.get(), 250)), null, Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_SWEETENER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_SWEETENER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_SWEETENER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_LEMON), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_LEMON));
        save(consumer, "limeade_mixing", new ItemStack(SummerCitrus.LIMEADE.get(), 4), new ItemStack(Items.GLASS_BOTTLE, 4), 8, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluid(Fluids.WATER, 750), fluid(ExtraDelightFluids.LIME_JUICE.FLUID.get(), 250)), null, Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_SWEETENER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_SWEETENER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_SWEETENER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_LIME), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_LIME));
        save(consumer, "marshmallow", new ItemStack(ExtraDelightItems.MARSHMALLOW.get(), 4), ItemStack.EMPTY, 4, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluid(Fluids.WATER, 250)), null, Ingredient.of(TAG_FORGE_MALLOWROOT_POWDER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_SWEETENER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_SWEETENER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_EGGS));
        save(consumer, "mayo", new ItemStack(ExtraDelightItems.MAYO.get(), 1), new ItemStack(Items.GLASS_BOTTLE, 1), 8, Ingredient.of(ExtraDelightItems.WHISK.get()),
                List.of(fluidTag("extradelighttfc:mixing_oils", 250), fluid(TFCFluids.SIMPLE_FLUIDS.get(SimpleFluid.VINEGAR).source().get(), 250)), null, Ingredient.of(TAG_MIXING_EGG_OR_YOLK), Ingredient.of(TAG_MIXING_EGG_OR_YOLK));
        save(consumer, "melon_fruit_salad_mixing", new ItemStack(SummerCitrus.MELON_FRUIT_SALAD_ITEM.get(), 1), new ItemStack(Items.MELON, 1), 2, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluidTag("extradelight:lemon_or_lime_juice", 250)), null, Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_APPLE), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_MELON), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_ORANGE), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_BERRIES), Ingredient.of(Items.HONEY_BOTTLE), Ingredient.of(TAG_FORGE_MINT));
        save(consumer, "melon_gazpacho_mixing", new ItemStack(SummerCitrus.MELON_GAZPACHO.get(), 4), new ItemStack(Items.BOWL, 4), 4, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluidTag("extradelighttfc:mixing_oils", 250), fluid(TFCFluids.SIMPLE_FLUIDS.get(SimpleFluid.VINEGAR).source().get(), 250)), null, Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_MELON), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_TOMATO), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_CUCUMBER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_ONION), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_GARLIC), Ingredient.of(TAG_FORGE_MINT), Ingredient.of(TFCItems.POWDERS.get(Powder.SALT).get()), Ingredient.of(ExtraDelightItems.BREAD_CRUMBS.get()));
        save(consumer, "morkovcha_mixing", new ItemStack(Fermentation.MORKOVCHA.get(), 3), new ItemStack(Items.BOWL, 3), 8, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluidTag("extradelighttfc:mixing_oils", 250), fluid(TFCFluids.SIMPLE_FLUIDS.get(SimpleFluid.VINEGAR).source().get(), 250)), null, Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_CARROT), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_GARLIC), Ingredient.of(TAG_FORGE_CHILI_POWDER), Ingredient.of(TFCItems.POWDERS.get(Powder.SALT).get()));
        save(consumer, "nougat", new ItemStack(ExtraDelightItems.NOUGAT.get(), 4), ItemStack.EMPTY, 4, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluid(Fluids.WATER, 250)), null, Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_SWEETENER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_SWEETENER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_BUTTER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_EGGS), Ingredient.of(TAG_FORGE_NUTS_COOKED));
        save(consumer, "nut_butter_cookie_dough", new ItemStack(ExtraDelightItems.NUT_BUTTER_COOKIE_DOUGH.get(), 1), ItemStack.EMPTY, 4, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluid(ExtraDelightFluids.NUT_BUTTER.FLUID.get(), 250)), null, Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_FLOUR), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_SWEETENER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_BUTTER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_EGGS));
        save(consumer, "nut_butter_cookie_dough_sugar", new ItemStack(ExtraDelightItems.NUT_BUTTER_COOKIE_DOUGH.get(), 1), ItemStack.EMPTY, 4, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluid(ExtraDelightFluids.NUT_BUTTER.FLUID.get(), 250)), null, Ingredient.of(ExtraDelightItems.SUGAR_COOKIE_DOUGH.get()));
        save(consumer, "orangeade_mixing", new ItemStack(SummerCitrus.ORANGEADE.get(), 4), new ItemStack(Items.GLASS_BOTTLE, 4), 8, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluid(Fluids.WATER, 500), fluid(ExtraDelightFluids.ORANGE_JUICE.FLUID.get(), 500)), null, Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_SWEETENER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_SWEETENER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_SWEETENER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_ORANGE), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_ORANGE));
        save(consumer, "pasta_alfredo", new ItemStack(ExtraDelightItems.PASTA_ALFREDO.get(), 1), new ItemStack(Items.BOWL, 1), 2, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(), null, Ingredient.of(TAG_FORGE_ALFREDO_SAUCE), Ingredient.of(ExtraDelightItems.COOKED_PASTA.get()));
        save(consumer, "pasta_meatballs", new ItemStack(ModItems.PASTA_WITH_MEATBALLS.get(), 1), new ItemStack(Items.BOWL, 1), 2, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(), null, Ingredient.of(ModItems.TOMATO_SAUCE.get()), Ingredient.of(ExtraDelightItems.COOKED_PASTA.get()), Ingredient.of(ModItems.BEEF_PATTY.get()));
        save(consumer, "pasta_mutton", new ItemStack(ModItems.PASTA_WITH_MUTTON_CHOP.get(), 1), new ItemStack(Items.BOWL, 1), 2, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(), null, Ingredient.of(ModItems.TOMATO_SAUCE.get()), Ingredient.of(ExtraDelightItems.COOKED_PASTA.get()), Ingredient.of(TFCItems.FOOD.get(Food.COOKED_MUTTON).get()));
        save(consumer, "pasta_tomato", new ItemStack(ExtraDelightItems.PASTA_TOMATO.get(), 1), new ItemStack(Items.BOWL, 1), 2, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(), null, Ingredient.of(ModItems.TOMATO_SAUCE.get()), Ingredient.of(ExtraDelightItems.COOKED_PASTA.get()));
        save(consumer, "potato_salad", new ItemStack(ExtraDelightItems.POTATO_SALAD.get(), 2), new ItemStack(Items.BOWL, 2), 8, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluid(ExtraDelightFluids.MAYO.FLUID.get(), 250)), null, Ingredient.of(TFCItems.FOOD.get(Food.BAKED_POTATO).get()), Ingredient.of(TFCItems.FOOD.get(Food.BAKED_POTATO).get()), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_ONION));
        save(consumer, "pulled_pork", new ItemStack(ExtraDelightItems.PULLED_PORK_FEAST_ITEM.get(), 1), new ItemStack(Items.BOWL, 1), 4, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluid(ExtraDelightFluids.BBQ.FLUID.get(), 250)), null, Ingredient.of(TAG_FORGE_PORK_SCRAP_COOKED), Ingredient.of(TAG_FORGE_PORK_SCRAP_COOKED), Ingredient.of(TAG_FORGE_PORK_SCRAP_COOKED), Ingredient.of(TAG_FORGE_PORK_SCRAP_COOKED));
        save(consumer, "pumpkin_cookie_dough", new ItemStack(ExtraDelightItems.PUMPKIN_COOKIE_DOUGH.get(), 1), ItemStack.EMPTY, 4, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(), null, Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_FLOUR), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_SWEETENER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_BUTTER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_EGGS), Ingredient.of(TFCItems.FOOD.get(Food.PUMPKIN_CHUNKS).get()));
        save(consumer, "pumpkin_cookie_dough_sugar", new ItemStack(ExtraDelightItems.PUMPKIN_COOKIE_DOUGH.get(), 1), ItemStack.EMPTY, 4, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(), null, Ingredient.of(ExtraDelightItems.SUGAR_COOKIE_DOUGH.get()), Ingredient.of(TFCItems.FOOD.get(Food.PUMPKIN_CHUNKS).get()));
        save(consumer, "salad_meat", new ItemStack(ExtraDelightItems.SALAD_FEAST_ITEM.get(), 1), new ItemStack(Items.BOWL, 1), 4, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluidTag("extradelighttfc:mixing_oils", 250), fluid(TFCFluids.SIMPLE_FLUIDS.get(SimpleFluid.VINEGAR).source().get(), 250)), null, Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_CABBAGE), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_ONION), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_CARROT), Ingredient.of(TAG_FORGE_CROUTONS), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_CHEESE), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_BOILED_EGGS), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_COOKED_MEAT));
        save(consumer, "salad_veg", new ItemStack(ExtraDelightItems.SALAD_FEAST_ITEM.get(), 1), new ItemStack(Items.BOWL, 1), 4, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluidTag("extradelighttfc:mixing_oils", 250), fluid(TFCFluids.SIMPLE_FLUIDS.get(SimpleFluid.VINEGAR).source().get(), 250)), null, Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_CABBAGE), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_ONION), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_CARROT), Ingredient.of(ExtraDelightItems.CROUTONS.get()), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_CHEESE), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_BOILED_EGGS), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_VEGETABLES));
        save(consumer, "salami_mix", new ItemStack(Fermentation.SALAMI_MIX.get(), 8), ItemStack.EMPTY, 4, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(), null, Ingredient.of(TAG_FORGE_BEEF_GROUND), Ingredient.of(TAG_FORGE_FAT), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_GARLIC));
        save(consumer, "seaweed_salad", new ItemStack(ExtraDelightItems.SEAWEED_SALAD.get(), 2), new ItemStack(Items.BOWL, 2), 4, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluid(TFCFluids.SIMPLE_FLUIDS.get(SimpleFluid.VINEGAR).source().get(), 250)), null, Ingredient.of(TFCItems.FOOD.get(Food.FRESH_SEAWEED).get()), Ingredient.of(TFCItems.FOOD.get(Food.FRESH_SEAWEED).get()), Ingredient.of(ExtraDelightItems.GRATED_CARROT.get()));
        save(consumer, "shirazi_salad_mixing", new ItemStack(Fermentation.SHIRAZI_SALAD.get(), 1), new ItemStack(Items.BOWL, 1), 2, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluidTag("extradelighttfc:mixing_oils", 250), fluidTag("extradelight:lemon_or_lime_juice", 250)), null, Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_TOMATO), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_CUCUMBER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_ONION), Ingredient.of(TAG_FORGE_MINT));
        save(consumer, "stiff_peaks_mixing", new ItemStack(ExtraDelightItems.STIFF_PEAKS.get(), 1), new ItemStack(Items.BOWL, 1), 8, Ingredient.of(ExtraDelightItems.WHISK.get()),
                List.of(fluid(ExtraDelightFluids.EGG_WHITE.FLUID.get(), 250)), null);
        save(consumer, "sugar_cookie_dough", new ItemStack(ExtraDelightItems.SUGAR_COOKIE_DOUGH.get(), 1), ItemStack.EMPTY, 4, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(), null, Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_FLOUR), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_SWEETENER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_BUTTER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_EGGS));
        save(consumer, "sweet_berry_cookie_dough", new ItemStack(ExtraDelightItems.SWEET_BERRY_COOKIE_DOUGH.get(), 1), ItemStack.EMPTY, 4, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(), null, Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_FLOUR), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_SWEETENER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_BUTTER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_EGGS), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_BERRIES));
        save(consumer, "sweet_berry_cookie_dough_sugar", new ItemStack(ExtraDelightItems.SWEET_BERRY_COOKIE_DOUGH.get(), 1), ItemStack.EMPTY, 4, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(), null, Ingredient.of(ExtraDelightItems.SUGAR_COOKIE_DOUGH.get()), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_BERRIES));
        save(consumer, "thai_melon_salad_mixing", new ItemStack(SummerCitrus.THAI_MELON_SALAD.get(), 4), new ItemStack(Items.BOWL, 4), 4, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluid(ExtraDelightFluids.LIME_JUICE.FLUID.get(), 250), fluid(TFCFluids.SIMPLE_FLUIDS.get(SimpleFluid.VINEGAR).source().get(), 250)), null, Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_MELON), Ingredient.of(TAG_FORGE_CROPS_PEANUT_COOKED), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_CUCUMBER), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_ONION), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_GINGER), Ingredient.of(TAG_FORGE_MINT), Ingredient.of(TAG_EXTRADELIGHTTFC_MIXING_SWEETENER), Ingredient.of(TAG_FORGE_FISH_SAUCE));
        save(consumer, "dough/barley", new ItemStack(TFCItems.FOOD.get(Food.BARLEY_DOUGH).get(), 6), ItemStack.EMPTY, 8, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluid(Fluids.WATER, 100)), null, Ingredient.of(TFCItems.FOOD.get(Food.BARLEY_FLOUR).get()), Ingredient.of(TFCItems.FOOD.get(Food.BARLEY_FLOUR).get()), Ingredient.of(TFCItems.FOOD.get(Food.BARLEY_FLOUR).get()));
        save(consumer, "dough/maize", new ItemStack(TFCItems.FOOD.get(Food.MAIZE_DOUGH).get(), 6), ItemStack.EMPTY, 8, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluid(Fluids.WATER, 100)), null, Ingredient.of(TFCItems.FOOD.get(Food.MAIZE_FLOUR).get()), Ingredient.of(TFCItems.FOOD.get(Food.MAIZE_FLOUR).get()), Ingredient.of(TFCItems.FOOD.get(Food.MAIZE_FLOUR).get()));
        save(consumer, "dough/oat", new ItemStack(TFCItems.FOOD.get(Food.OAT_DOUGH).get(), 6), ItemStack.EMPTY, 8, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluid(Fluids.WATER, 100)), null, Ingredient.of(TFCItems.FOOD.get(Food.OAT_FLOUR).get()), Ingredient.of(TFCItems.FOOD.get(Food.OAT_FLOUR).get()), Ingredient.of(TFCItems.FOOD.get(Food.OAT_FLOUR).get()));
        save(consumer, "dough/rice", new ItemStack(TFCItems.FOOD.get(Food.RICE_DOUGH).get(), 6), ItemStack.EMPTY, 8, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluid(Fluids.WATER, 100)), null, Ingredient.of(TFCItems.FOOD.get(Food.RICE_FLOUR).get()), Ingredient.of(TFCItems.FOOD.get(Food.RICE_FLOUR).get()), Ingredient.of(TFCItems.FOOD.get(Food.RICE_FLOUR).get()));
        save(consumer, "dough/rye", new ItemStack(TFCItems.FOOD.get(Food.RYE_DOUGH).get(), 6), ItemStack.EMPTY, 8, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluid(Fluids.WATER, 100)), null, Ingredient.of(TFCItems.FOOD.get(Food.RYE_FLOUR).get()), Ingredient.of(TFCItems.FOOD.get(Food.RYE_FLOUR).get()), Ingredient.of(TFCItems.FOOD.get(Food.RYE_FLOUR).get()));
        save(consumer, "dough/wheat", new ItemStack(TFCItems.FOOD.get(Food.WHEAT_DOUGH).get(), 6), ItemStack.EMPTY, 8, Ingredient.of(TAG_FORGE_SPOONS),
                List.of(fluid(Fluids.WATER, 100)), null, Ingredient.of(TFCItems.FOOD.get(Food.WHEAT_FLOUR).get()), Ingredient.of(TFCItems.FOOD.get(Food.WHEAT_FLOUR).get()), Ingredient.of(TFCItems.FOOD.get(Food.WHEAT_FLOUR).get()));
        save(consumer, "whipped_cream", new ItemStack(ExtraDelightItems.WHIPPED_CREAM.get(), 1), new ItemStack(Items.BOWL, 1), 8, Ingredient.of(ExtraDelightItems.WHISK.get()),
                List.of(fluidTag("tfc:milks", 250)), false);
        save(consumer, "whipped_cream_firmalife", new ItemStack(ExtraDelightItems.WHIPPED_CREAM.get(), 1), new ItemStack(Items.BOWL, 1), 8, Ingredient.of(ExtraDelightItems.WHISK.get()),
                List.of(fluidTag("extradelighttfc:mixing_cream", 250)), true);
        save(consumer, "butter", new ItemStack(ExtraDelightItems.BUTTER.get(), 1), ItemStack.EMPTY, 8, Ingredient.of(ExtraDelightItems.WHISK.get()),
                List.of(fluid(ExtraDelightFluids.WHIPPED_CREAM.FLUID.get(), 250)), false);
        save(consumer, "butter_firmalife", new ItemStack(ExtraDelightItems.BUTTER.get(), 1), ItemStack.EMPTY, 8, Ingredient.of(ExtraDelightItems.WHISK.get()),
                List.of(fluidTag("extradelighttfc:mixing_cream", 1000)), true, Ingredient.of(TFCItems.POWDERS.get(Powder.SALT).get()));
    }

    private static JsonObject fluid(Fluid fluid, int amount) {
        JsonObject json = new JsonObject();
        json.addProperty("fluid", ForgeRegistries.FLUIDS.getKey(fluid).toString());
        json.addProperty("amount", amount);
        return json;
    }

    private static JsonObject fluidTag(String tag, int amount) {
        JsonObject json = new JsonObject();
        json.addProperty("tag", tag);
        json.addProperty("amount", amount);
        return json;
    }

    private static void save(Consumer<FinishedRecipe> consumer, String name, ItemStack result,
                             ItemStack container, int stirs, Ingredient tool, List<JsonObject> fluids,
                             @Nullable Boolean firmalife, Ingredient... ingredients) {
        JsonObject json = new JsonObject();
        JsonArray inputs = new JsonArray();
        for (Ingredient ingredient : ingredients) inputs.add(ingredient.toJson());
        json.add("ingredients", inputs);
        JsonArray fluidArray = new JsonArray();
        fluids.forEach(fluidArray::add);
        json.add("fluids", fluidArray);
        JsonObject output = stack(result);
        // Optional FL registry entries must also be emitted when datagen runs without FL.
        if (name.equals("butter_firmalife")) output.addProperty("id", "firmalife:food/butter");
        json.add("result", output);
        if (!container.isEmpty()) json.add("container", stack(container));
        json.addProperty("stirs", stirs);
        json.add("utensil", tool.toJson());
        if (firmalife != null) {
            JsonObject loaded = new JsonObject();
            loaded.addProperty("type", "forge:mod_loaded");
            loaded.addProperty("modid", "firmalife");
            JsonObject condition = loaded;
            if (!firmalife) {
                condition = new JsonObject();
                condition.addProperty("type", "forge:not");
                condition.add("value", loaded);
            }
            JsonArray conditions = new JsonArray();
            conditions.add(condition);
            json.add("conditions", conditions);
        }
        consumer.accept(new Result(ExtraDelightTFC.modLoc("mixing/" + name), json));
    }

    private static JsonObject stack(ItemStack stack) {
        JsonObject json = new JsonObject();
        json.addProperty("id", ForgeRegistries.ITEMS.getKey(stack.getItem()).toString());
        json.addProperty("count", stack.getCount());
        return json;
    }

    private record Result(ResourceLocation id, JsonObject data) implements FinishedRecipe {
        @Override public void serializeRecipeData(JsonObject json) { data.entrySet().forEach(e -> json.add(e.getKey(), e.getValue())); }
        @Override public ResourceLocation getId() { return id; }
        @Override public RecipeSerializer<?> getType() { return ExtraDelightRecipes.MIXING_BOWL_SERIALIZER.get(); }
        @Override public @Nullable JsonObject serializeAdvancement() { return null; }
        @Override public @Nullable ResourceLocation getAdvancementId() { return null; }
    }
}
