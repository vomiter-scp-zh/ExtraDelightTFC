package com.vomiter.extradelighttfc.data;

import com.vomiter.extradelighttfc.ExtraDelightTFC;
import com.vomiter.extradelighttfc.registry.EDTFCItems;
import com.vomiter.extradelighttfc.registry.EDTFCDecorations;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public final class EDTFCItemModelProvider extends ItemModelProvider {
    public EDTFCItemModelProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, ExtraDelightTFC.MOD_ID, helper);
    }

    @Override
    protected void registerModels() {
        pestles();
        EDTFCDecorations.BLOCKS.getEntries().forEach(block ->
                withExistingParent(block.getId().getPath(), block.getId().withPrefix("block/")));

        withExistingParent("item/" + EDTFCItems.STONE_SPOON.getId().getPath(),
                ResourceLocation.fromNamespaceAndPath("extradelight", "item/stone_spoon"));
        withExistingParent("item/" + EDTFCItems.STONE_SPOON_HEAD.getId().getPath(), mcLoc("item/generated"))
                .texture("layer0", modLoc("item/" + EDTFCItems.STONE_SPOON_HEAD.getId().getPath()));
        EDTFCItems.METAL_SPOONS.values().forEach(item ->
                withExistingParent("item/" + item.getId().getPath(), mcLoc("item/generated"))
                        .texture("layer0", modLoc("item/" + item.getId().getPath())));
    }

    private void pestles() {
        withExistingParent("item/" + EDTFCItems.STONE_PESTLE.getId().getPath(),
                ResourceLocation.fromNamespaceAndPath("extradelight", "item/pestle_stone"));
        EDTFCItems.METAL_PESTLES.forEach((metal, item) -> {
            ResourceLocation texture = ResourceLocation.fromNamespaceAndPath("tfc", "block/metal/smooth/" + metal.getSerializedName());
            withExistingParent("item/" + item.getId().getPath(), ResourceLocation.fromNamespaceAndPath("extradelight", "item/pestle"))
                    .texture("0", texture).texture("particle", texture);
        });
    }
}
