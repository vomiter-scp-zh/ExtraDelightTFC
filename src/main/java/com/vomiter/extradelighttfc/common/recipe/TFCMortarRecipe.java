package com.vomiter.extradelighttfc.common.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.vomiter.extradelight.common.complex.workstations.mortar.recipes.MortarRecipe;
import com.vomiter.extradelighttfc.registry.EDTFCRecipeSerializers;
import net.dries007.tfc.common.recipes.RecipeHelpers;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;

/** A TFC output provider in ED's existing mortar recipe type and JEI category. */
public class TFCMortarRecipe extends MortarRecipe {
    private final ItemStackProvider output;

    public TFCMortarRecipe(ResourceLocation id, String group, Ingredient ingredient,
                          ItemStackProvider output, FluidStack fluid, int grinds) {
        // JEI needs the base stack, without applying input-dependent modifiers.
        super(id, group, ingredient, output.stack().get().copy(), fluid.copy(), grinds);
        if (grinds < 1) throw new IllegalArgumentException("Mortar grinds must be positive");
        this.output = output;
    }

    public ItemStackProvider getOutputProvider() {
        return output;
    }

    public ItemStack createOutput(ItemStack input) {
        ItemStackHandler context = new ItemStackHandler(1);
        context.setStackInSlot(0, input.copyWithCount(1));
        RecipeHelpers.setCraftingInput(context, 0, 1);
        try {
            return output.getSingleStack(input);
        } finally {
            RecipeHelpers.clearCraftingInput();
        }
    }

    @Override
    public ItemStack assemble(Container inventory, RegistryAccess access) {
        return createOutput(inventory.getItem(0));
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return EDTFCRecipeSerializers.MORTAR.get();
    }

    public static class Serializer implements RecipeSerializer<TFCMortarRecipe> {
        @Override
        public TFCMortarRecipe fromJson(ResourceLocation id, JsonObject json) {
            Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));
            ItemStackProvider output = json.has("result")
                    ? ItemStackProvider.fromJson(GsonHelper.getAsJsonObject(json, "result"))
                    : ItemStackProvider.empty();
            FluidStack fluid = FluidStack.EMPTY;
            if (json.has("fluid_output")) {
                JsonObject fluidJson = GsonHelper.getAsJsonObject(json, "fluid_output");
                ResourceLocation fluidId = ResourceLocation.tryParse(GsonHelper.getAsString(fluidJson, "fluid"));
                Fluid type = fluidId == null ? null : ForgeRegistries.FLUIDS.getValue(fluidId);
                int amount = GsonHelper.getAsInt(fluidJson, "amount");
                if (type == null || type == Fluids.EMPTY || amount < 1) {
                    throw new JsonSyntaxException("Invalid mortar fluid output: " + fluidJson);
                }
                fluid = new FluidStack(type, amount);
                if (fluidJson.has("nbt")) {
                    try {
                        fluid.setTag(TagParser.parseTag(GsonHelper.getAsString(fluidJson, "nbt")));
                    } catch (CommandSyntaxException exception) {
                        throw new JsonSyntaxException("Invalid mortar fluid NBT", exception);
                    }
                }
            }
            int grinds = GsonHelper.getAsInt(json, "grinds", 4);
            if (grinds < 1) throw new JsonSyntaxException("Mortar grinds must be positive");
            if (output.stack().get().isEmpty() && output.modifiers().length == 0 && fluid.isEmpty()) {
                throw new JsonSyntaxException("Mortar recipe must have an item or fluid output");
            }
            return new TFCMortarRecipe(id, GsonHelper.getAsString(json, "group", ""), ingredient, output, fluid, grinds);
        }

        @Override
        public TFCMortarRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            String group = buffer.readUtf();
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            ItemStackProvider output = ItemStackProvider.fromNetwork(buffer);
            FluidStack fluid = FluidStack.readFromPacket(buffer);
            return new TFCMortarRecipe(id, group, ingredient, output, fluid, buffer.readVarInt());
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, TFCMortarRecipe recipe) {
            buffer.writeUtf(recipe.getGroup());
            recipe.getIngredients().get(0).toNetwork(buffer);
            recipe.output.toNetwork(buffer);
            recipe.getFluid().writeToPacket(buffer);
            buffer.writeVarInt(recipe.getGrinds());
        }
    }
}
