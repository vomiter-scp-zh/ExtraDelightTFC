package com.vomiter.extradelighttfc.data;

import com.vomiter.extradelight.ExtraDelight;
import com.vomiter.extradelighttfc.ExtraDelightTFC;
import net.minecraft.tags.FluidTags;
import net.minecraft.resources.ResourceLocation;
import net.dries007.tfc.common.fluids.TFCFluids;
import net.dries007.tfc.common.fluids.SimpleFluid;
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
        tag(FluidTags.create(ExtraDelightTFC.modLoc("mixing_oils"))).add(
                ExtraDelightFluids.OIL.FLUID.get(), TFCFluids.SIMPLE_FLUIDS.get(SimpleFluid.OLIVE_OIL).source().get());
        tag(FluidTags.create(ExtraDelightTFC.modLoc("mixing_cream")))
                .addOptional(ResourceLocation.tryParse("firmalife:cream"));
        tag(EDTFCTags.FLUID_INGREDIENT).add(
                ExtraDelightFluids.FLUIDS
                        .getEntries().stream().map(Supplier::get)
                        .toArray(Fluid[]::new)
        );
    }
}
