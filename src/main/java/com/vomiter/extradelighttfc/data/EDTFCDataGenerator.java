package com.vomiter.extradelighttfc.data;

import com.vomiter.extradelighttfc.ExtraDelightTFC;
import net.dries007.tfc.common.items.Powder;
import net.dries007.tfc.common.items.TFCItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class EDTFCDataGenerator {
    public static void gather(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();

        var blockTag = new EDTFCBlockTagProvider(output, lookupProvider, ExtraDelightTFC.MOD_ID, helper);
        generator.addProvider(event.includeServer(), blockTag);
        generator.addProvider(event.includeServer(), new EDTFCItemTagProvider(
                output, lookupProvider, blockTag.contentsGetter(), ExtraDelightTFC.MOD_ID, helper
        ));
        generator.addProvider(event.includeServer(), new EDTFCRecipeProvider(output));
        generator.addProvider(event.includeServer(), new LootTableProvider(output, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(() -> consumer -> consumer.accept(
                        EDTFCRecipeProvider.EVAPORATOR_SALT_LOOT,
                        LootTable.lootTable().withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(TFCItems.POWDERS.get(Powder.SALT).get())))),
                        LootContextParamSets.ARCHAEOLOGY))));
        generator.addProvider(event.includeClient(), new EDTFCItemModelProvider(output, helper));
        generator.addProvider(event.includeClient(), new EDTFCLanguageProvider(output, "en_us"));
        generator.addProvider(event.includeClient(), new EDTFCLanguageProvider(output, "zh_tw"));
        generator.addProvider(event.includeServer(), new EDTFCFluidTagProvider(output, lookupProvider, ExtraDelightTFC.MOD_ID, helper));
        var food = new EDTFCFoodDataProvider(output);
        food.save();
        generator.addProvider(event.includeServer(), food);

    }
}
