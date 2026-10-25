package com.vomiter.extradelighttfc;

import com.mojang.logging.LogUtils;
import com.vomiter.extradelighttfc.common.recipe.DriedModifier;
import com.vomiter.extradelighttfc.common.recipe.PortionFoodModifier;
import com.vomiter.extradelighttfc.registry.EDTFCFoodTraits;
import net.dries007.tfc.common.recipes.outputs.ItemStackModifiers;
import com.vomiter.extradelighttfc.adapter.EDMixingBowlAdapter;
import com.vomiter.extradelighttfc.util.ExtraNutrientsAfterHeating;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import com.vomiter.extradelighttfc.adapter.dynamic.FoodModifierRegistry;
import com.vomiter.extradelighttfc.data.EDTFCDataGenerator;
import com.vomiter.extradelighttfc.event.EDTFCForgeEvents;
import com.vomiter.extradelighttfc.registry.EDTFCBlockEntityTypes;
import com.vomiter.extradelighttfc.registry.EDTFCRecipeSerializers;
import com.vomiter.extradelighttfc.registry.EDTFCItems;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

@Mod(ExtraDelightTFC.MOD_ID)
public class ExtraDelightTFC {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "extradelighttfc";
    public static final Logger LOGGER = LogUtils.getLogger();
    private static final TagKey<Item> SWEETENER = ItemTags.create(ResourceLocation.fromNamespaceAndPath("tfc", "sweetener"));

    public static ResourceLocation modLoc(String s) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, s);
    }

    public ExtraDelightTFC(FMLJavaModLoadingContext context) {
        IEventBus modBus = context.getModEventBus();
        EDTFCFoodTraits.init();
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, EDTFCFoodTraits::reloadDryingFoods);
        ItemStackModifiers.register(modLoc("dried"), DriedModifier.INSTANCE);
        ItemStackModifiers.register(modLoc("portion_food"), PortionFoodModifier.INSTANCE);
        EDTFCBlockEntityTypes.BLOCK_ENTITY_TYPES.register(modBus);
        EDTFCRecipeSerializers.SERIALIZERS.register(modBus);
        EDTFCItems.ITEMS.register(modBus);
        modBus.addListener(EDTFCItems::addCreativeItems);
        modBus.addListener(EDTFCDataGenerator::gather);
        modBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.addListener(EDTFCForgeEvents::onFinishUse);
        context.registerConfig(ModConfig.Type.COMMON, ExtraDelightTFCConfig.SPEC);
        if(FMLEnvironment.dist.isClient()){
            modBus.addListener(this::clientSetup);
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {

            EDMixingBowlAdapter.registerIndividual((food, context) -> {
                // Sweetener contributes hunger and saturation while mixing.
                if (FoodCapability.get(context.stack()) == null) {
                    if (context.stack().is(SWEETENER)) {
                        food.hunger(2).saturation(0.5f);
                    }
                }
                return food;
            });

            ExtraNutrientsAfterHeating.registerDefaults();

        });
    }

    public void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
        });
    }

}
