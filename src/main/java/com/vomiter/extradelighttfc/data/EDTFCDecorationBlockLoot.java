package com.vomiter.extradelighttfc.data;

import com.vomiter.extradelighttfc.registry.EDTFCDecorations;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public final class EDTFCDecorationBlockLoot extends BlockLootSubProvider {
    public EDTFCDecorationBlockLoot() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        getKnownBlocks().forEach(this::dropSelf);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return EDTFCDecorations.BLOCKS.getEntries().stream().map(RegistryObject::get).toList();
    }
}
