package com.vomiter.extradelighttfc.data;

import com.vomiter.extradelight.data.loot.MiscLootTables;
import com.vomiter.extradelight.registry.ExtraDelightItems;
import java.util.function.BiConsumer;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.crop.Crop;
import net.dries007.tfc.common.blocks.crop.CropBlock;
import net.dries007.tfc.common.blocks.crop.DoubleCropBlock;
import net.dries007.tfc.common.items.Food;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.loot.CropYieldProvider;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

/** Overrides only the cultivated crop's mature food drop and the shucking main product. */
public final class EDTFCCornLoot {
    private EDTFCCornLoot() {}

    public static void crop(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
        CropBlock maize = (CropBlock) TFCBlocks.CROPS.get(Crop.MAIZE).get();
        var bottom = StatePropertiesPredicate.Builder.properties()
                .hasProperty(DoubleCropBlock.PART, DoubleCropBlock.Part.BOTTOM);
        var matureBottom = StatePropertiesPredicate.Builder.properties()
                .hasProperty(DoubleCropBlock.PART, DoubleCropBlock.Part.BOTTOM)
                .hasProperty(maize.getAgeProperty(), maize.getMaxAge());
        consumer.accept(maize.getLootTable(), LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .when(ExplosionCondition.survivesExplosion())
                        .add(LootItem.lootTableItem(ExtraDelightItems.UNSHUCKED_CORN.get())
                                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(maize).setProperties(matureBottom))
                                .apply(SetItemCountFunction.setCount(new CropYieldProvider(
                                        ConstantValue.exactly(0), UniformGenerator.between(6, 10))))))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .when(ExplosionCondition.survivesExplosion())
                        .add(LootItem.lootTableItem(TFCItems.CROP_SEEDS.get(Crop.MAIZE).get())
                                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(maize).setProperties(bottom)))));
    }

    public static void shucking(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
        consumer.accept(MiscLootTables.SHUCKED_CORN, LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(ExtraDelightItems.CORN_HUSK.get()))
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3))))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(ExtraDelightItems.CORN_SILK.get()))
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(TFCItems.FOOD.get(Food.MAIZE).get()))));
    }
}
