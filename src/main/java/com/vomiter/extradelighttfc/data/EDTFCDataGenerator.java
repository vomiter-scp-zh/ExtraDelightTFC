package com.vomiter.extradelighttfc.data;

import com.vomiter.extradelighttfc.ExtraDelightTFC;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;

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
        generator.addProvider(event.includeServer(), new EDTFCFluidTagProvider(output, lookupProvider, ExtraDelightTFC.MOD_ID, helper));
        var food = new EDTFCFoodDataProvider(output);
        food.save();
        generator.addProvider(event.includeServer(), food);

    }
}
