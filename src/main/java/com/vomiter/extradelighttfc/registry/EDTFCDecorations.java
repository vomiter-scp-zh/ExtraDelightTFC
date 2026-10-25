package com.vomiter.extradelighttfc.registry;

import com.vomiter.extradelight.common.aethetics.MoldingBlock;
import com.vomiter.extradelight.common.aethetics.StepStoolBlock;
import com.vomiter.extradelight.common.blocks.HalfCabinetBlock;
import com.vomiter.extradelight.common.complex.cabinet.countercabinet.CounterCabinetBlock;
import com.vomiter.extradelight.common.complex.cabinet.sink.SinkCabinetBlock;
import com.vomiter.extradelight.common.complex.displays.food.FoodDisplayBlock;
import com.vomiter.extradelight.common.complex.displays.knife.KnifeBlock;
import com.vomiter.extradelight.registry.ExtraDelightBlockEntities;
import com.vomiter.extradelighttfc.ExtraDelightTFC;
import com.vomiter.extradelighttfc.common.WoodenFruitBowlBlock;
import com.vomiter.survivorsdelight.mixin.BlockEntityTypeAccessor;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.minecraft.world.item.BlockItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public final class EDTFCDecorations {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ExtraDelightTFC.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ExtraDelightTFC.MOD_ID);
    public static final Map<Wood, Map<Kind, RegistryObject<Block>>> WOODS = registerWoods();
    public static final Map<Wood, Map<DyeColor, RegistryObject<Block>>> WALLPAPERS = registerWallpapers();

    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ExtraDelightTFC.MOD_ID);
    public static final RegistryObject<CreativeModeTab> TAB = TABS.register("wood_decorations",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.extradelighttfc.wood_decorations"))
                    .icon(() -> new ItemStack(WOODS.get(Wood.OAK).get(Kind.COUNTER_CABINET).get()))
                    .build());

    private EDTFCDecorations() {}

    public enum Kind {
        STEP_STOOL("step_stool", StepStoolBlock::new),
        KNIFE_BLOCK("knife_block", KnifeBlock::new),
        FOOD_DISPLAY("food_display", FoodDisplayBlock::new),
        DRIED_CORN_FENCE("dried_corn_fence", () -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE))),
        FRUIT_BOWL("fruit_bowl", WoodenFruitBowlBlock::new),
        HALF_CABINET("half_cabinet", HalfCabinetBlock::new),
        COUNTER_CABINET("counter_cabinet", CounterCabinetBlock::new),
        SINK("sink", SinkCabinetBlock::new);

        public final String path;
        private final Supplier<Block> factory;

        Kind(String path, Supplier<Block> factory) {
            this.path = path;
            this.factory = factory;
        }
    }

    private static Map<Wood, Map<Kind, RegistryObject<Block>>> registerWoods() {
        Map<Wood, Map<Kind, RegistryObject<Block>>> woods = new EnumMap<>(Wood.class);
        for (Wood wood : Wood.values()) {
            Map<Kind, RegistryObject<Block>> blocks = new EnumMap<>(Kind.class);
            for (Kind kind : Kind.values()) {
                blocks.put(kind, register(wood.getSerializedName() + "_" + kind.path, kind.factory));
            }
            woods.put(wood, Collections.unmodifiableMap(blocks));
        }
        return Collections.unmodifiableMap(woods);
    }

    private static Map<Wood, Map<DyeColor, RegistryObject<Block>>> registerWallpapers() {
        Map<Wood, Map<DyeColor, RegistryObject<Block>>> woods = new EnumMap<>(Wood.class);
        for (Wood wood : Wood.values()) {
            Map<DyeColor, RegistryObject<Block>> colors = new EnumMap<>(DyeColor.class);
            for (DyeColor color : DyeColor.values()) {
                colors.put(color, register(wood.getSerializedName() + "_molded_" + color.getName() + "_wallpaper",
                        () -> new MoldingBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS))));
            }
            woods.put(wood, Collections.unmodifiableMap(colors));
        }
        return Collections.unmodifiableMap(woods);
    }

    private static RegistryObject<Block> register(String name, Supplier<Block> factory) {
        RegistryObject<Block> block = BLOCKS.register(name, factory);
        ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        return block;
    }

    public static void register(IEventBus bus) {
        TABS.register(bus);
        BLOCKS.register(bus);
        ITEMS.register(bus);
        bus.addListener(EDTFCDecorations::commonSetup);
        bus.addListener(EDTFCDecorations::creativeItems);
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        // Keep the original BE types so their menus, packets and client renderers are reused.
        // Do this after every mod has registered its blocks and block entity types.
        event.enqueueWork(() -> {
            extend(ExtraDelightBlockEntities.KNIFE_BLOCK.get(), Kind.KNIFE_BLOCK);
            extend(ExtraDelightBlockEntities.FOOD_DISPLAY.get(), Kind.FOOD_DISPLAY);
            extend(ExtraDelightBlockEntities.FRUIT_BOWL.get(), Kind.FRUIT_BOWL);
            extend(ModBlockEntityTypes.CABINET.get(), Kind.HALF_CABINET);
            extend(ExtraDelightBlockEntities.COUNTER_CABINET_BLOCK.get(), Kind.COUNTER_CABINET);
            extend(ExtraDelightBlockEntities.SINK_BLOCK.get(), Kind.SINK);
        });
    }

    private static void extend(BlockEntityType<?> type, Kind kind) {
        BlockEntityTypeAccessor accessor = (BlockEntityTypeAccessor) type;
        Set<Block> valid = new HashSet<>(accessor.getValidBlocks());
        WOODS.values().forEach(blocks -> valid.add(blocks.get(kind).get()));
        accessor.setValidBlocks(valid);
    }

    private static void creativeItems(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == TAB.get()) {
            for (Wood wood : Wood.values()) {
                WOODS.get(wood).values().forEach(block -> event.accept(block.get()));
                WALLPAPERS.get(wood).values().forEach(block -> event.accept(block.get()));
            }
        }
    }
}
