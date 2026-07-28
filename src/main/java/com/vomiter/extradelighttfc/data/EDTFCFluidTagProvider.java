package com.vomiter.extradelighttfc.data;

import com.vomiter.extradelight.ExtraDelight;
import com.vomiter.extradelight.registry.ExtraDelightFluids;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class EDTFCFluidTagProvider extends FluidTagsProvider {
    public EDTFCFluidTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider p_256380_) {
        tag(EDTFCTags.FLUID_INGREDIENT).add(
                ExtraDelightFluids.FLUIDS
                        .getEntries().stream().map(Supplier::get)
                        .toArray(Fluid[]::new)
        );
    }
}
