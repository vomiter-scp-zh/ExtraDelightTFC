package com.vomiter.extradelighttfc;

import com.mojang.logging.LogUtils;
import com.vomiter.extradelighttfc.common.recipe.DriedModifier;
import com.vomiter.extradelighttfc.registry.EDTFCFoodTraits;
import net.dries007.tfc.common.recipes.outputs.ItemStackModifiers;
import com.vomiter.extradelighttfc.adapter.EDOvenCookingAdapter;
import com.vomiter.extradelighttfc.adapter.dynamic.FoodModifierRegistry;
import com.vomiter.extradelighttfc.data.EDTFCDataGenerator;
import com.vomiter.extradelighttfc.event.EDTFCForgeEvents;
import com.vomiter.extradelighttfc.registry.EDTFCBlockEntityTypes;
import com.vomiter.extradelighttfc.registry.EDTFCRecipeSerializers;
import com.vomiter.extradelighttfc.registry.EDTFCItems;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.items.Food;
import net.dries007.tfc.common.items.TFCItems;
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

    public static ResourceLocation modLoc(String s) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, s);
    }

    public ExtraDelightTFC(FMLJavaModLoadingContext context) {
        IEventBus modBus = context.getModEventBus();
        EDTFCFoodTraits.init();
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, EDTFCFoodTraits::reloadDryingFoods);
        ItemStackModifiers.register(modLoc("dried"), DriedModifier.INSTANCE);
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

            EDOvenCookingAdapter.registerIndividual(((food, context) -> {
                if(context.stack().is(ItemTags.create(ResourceLocation.fromNamespaceAndPath("tfc", "foods/grains")))){
                    var breadFood = FoodCapability.get(TFCItems.FOOD.get(Food.BARLEY_BREAD).get().getDefaultInstance()).getData();
                    food.addData(breadFood);
                }
                return food;
            }));
        });
    }

    public void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
        });
    }

}
