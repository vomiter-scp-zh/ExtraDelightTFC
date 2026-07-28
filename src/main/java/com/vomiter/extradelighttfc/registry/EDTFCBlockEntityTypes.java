package com.vomiter.extradelighttfc.registry;

import com.vomiter.extradelight.common.blocks.RecipeFeastBlock;
import com.vomiter.extradelighttfc.ExtraDelightTFC;
import com.vomiter.extradelighttfc.common.RecipeFeastBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EDTFCBlockEntityTypes {
    public static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES
            = DeferredRegister.create(
                    ForgeRegistries.BLOCK_ENTITY_TYPES,
                    ExtraDelightTFC.MOD_ID
            );

    public static RegistryObject<BlockEntityType<?>> DECAY_RECIPE_FEAST
            = BLOCK_ENTITY_TYPES.register(
                    "recipe_feast_decaying",
            () -> BlockEntityType.Builder.of(
                    RecipeFeastBlockEntity::new,
                    ForgeRegistries.BLOCKS.getValues().stream()
                            .filter(block -> block instanceof RecipeFeastBlock)
                            .toArray(Block[]::new)
            ).build(null)
    );
}
