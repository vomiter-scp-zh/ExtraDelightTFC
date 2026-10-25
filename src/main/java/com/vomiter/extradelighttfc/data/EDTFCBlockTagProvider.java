package com.vomiter.extradelighttfc.data;

import net.minecraft.core.HolderLookup;
import com.vomiter.extradelighttfc.registry.EDTFCDecorations;
import com.vomiter.extradelighttfc.registry.EDTFCDecorations.Kind;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class EDTFCBlockTagProvider extends BlockTagsProvider {
    public EDTFCBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider p_256380_) {
        EDTFCDecorations.BLOCKS.getEntries().forEach(block -> tag(BlockTags.MINEABLE_WITH_AXE).add(block.get()));
        EDTFCDecorations.WOODS.values().forEach(blocks -> {
            var fence = blocks.get(Kind.DRIED_CORN_FENCE).get();
            tag(BlockTags.FENCES).add(fence);
            tag(BlockTags.WOODEN_FENCES).add(fence);
            tag(Tags.Blocks.FENCES_WOODEN).add(fence);
        });

    }
}
