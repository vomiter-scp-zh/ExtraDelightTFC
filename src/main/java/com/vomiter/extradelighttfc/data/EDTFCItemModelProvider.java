package com.vomiter.extradelighttfc.data;

import com.vomiter.extradelighttfc.ExtraDelightTFC;
import com.vomiter.extradelighttfc.registry.EDTFCItems;
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
