package com.vomiter.extradelighttfc.data;

import com.vomiter.extradelight.common.aethetics.MoldingBlock;
import com.vomiter.extradelight.common.complex.displays.food.FoodDisplayBlock;
import com.vomiter.extradelight.registry.AestheticBlocks;
import com.vomiter.extradelighttfc.ExtraDelightTFC;
import com.vomiter.extradelighttfc.registry.EDTFCDecorations;
import com.vomiter.extradelighttfc.registry.EDTFCDecorations.Kind;
import com.vomiter.survivorsdelight.registry.SDBlocks;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import java.util.Map;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.common.registry.ModBlocks;

public final class EDTFCDecorationBlockStateProvider extends BlockStateProvider {
    private static final ResourceLocation STEP_STOOL = edModel("stepstool");
    private static final ResourceLocation KNIFE_BLOCK = edModel("knifeblock");
    private static final ResourceLocation FOOD_DISPLAY = edModel("food_display");
    private static final ResourceLocation FOOD_DISPLAY_ENCASED = edModel("food_display_encased");
    private static final ResourceLocation FRUIT_BOWL = edModel("fruit_bowl");
    private static final ResourceLocation HALF_CABINET = edModel("half_cabinet");
    private static final ResourceLocation SINK = edModel("sink_cabinet_bare");
    private static final ResourceLocation TAP_TEXTURE = edModel("tap");
    private static final ResourceLocation DRIED_CORN_FENCE = edModel("dried_corn_fence");
    private static final ResourceLocation MOLDING_BOTTOM = edModel("molding_bottom");
    private static final ResourceLocation MOLDING_TOP = edModel("molding_top");
    private static final Map<Direction, BooleanProperty> CONNECTIONS = Map.of(
            Direction.NORTH, BlockStateProperties.NORTH, Direction.EAST, BlockStateProperties.EAST,
            Direction.SOUTH, BlockStateProperties.SOUTH, Direction.WEST, BlockStateProperties.WEST);
    private static final String CUTOUT = "minecraft:cutout";

    public EDTFCDecorationBlockStateProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, ExtraDelightTFC.MOD_ID, helper);
    }

    @Override
    protected void registerStatesAndModels() {
        for (Wood wood : Wood.values()) {
            ResourceLocation planks = blockTexture(wood.getBlock(Wood.BlockType.PLANKS).get());
            EDTFCDecorations.WOODS.get(wood).forEach((kind, handle) -> register(wood, kind, handle.get(), planks));
            EDTFCDecorations.WALLPAPERS.get(wood).forEach((color, handle) -> wallpaper(handle.get(), color, planks));
        }
    }

    private void register(Wood wood, Kind kind, Block block, ResourceLocation planks) {
        String name = name(block);
        switch (kind) {
            case STEP_STOOL -> horizontalBlock(block, wooden(name, STEP_STOOL, planks));
            case KNIFE_BLOCK -> horizontalBlock(block, wooden(name, KNIFE_BLOCK, planks));
            case FOOD_DISPLAY -> {
                ModelFile bare = wooden(name, FOOD_DISPLAY, planks);
                ModelFile encased = wooden(name + "_encased", FOOD_DISPLAY_ENCASED, planks);
                horizontalBlock(block, state -> state.getValue(FoodDisplayBlock.ENCASED) ? encased : bare);
            }
            case FRUIT_BOWL -> simpleBlock(block, wooden(name, FRUIT_BOWL, planks));
            case HALF_CABINET, COUNTER_CABINET -> {
                ModelFile closed = cabinet(wood, kind, name, false);
                ModelFile open = cabinet(wood, kind, name + "_open", true);
                horizontalBlock(block, state -> state.getValue(BlockStateProperties.OPEN) ? open : closed);
            }
            case SINK -> horizontalBlock(block, models().withExistingParent(name, SINK).renderType(CUTOUT)
                    .texture("0", TAP_TEXTURE).texture("1", cabinetTexture(wood, CabinetFace.FRONT))
                    .texture("2", cabinetTexture(wood, CabinetFace.SIDE)).texture("3", cabinetTexture(wood, CabinetFace.TOP))
                    .texture("4", planks).texture("particle", cabinetTexture(wood, CabinetFace.FRONT)));
            case DRIED_CORN_FENCE -> cornFence((FenceBlock) block, planks);
        }
    }

    private BlockModelBuilder wooden(String name, ResourceLocation parent, ResourceLocation planks) {
        return models().withExistingParent(name, parent).renderType(CUTOUT).texture("0", planks).texture("particle", planks);
    }

    private ModelFile cabinet(Wood wood, Kind kind, String name, boolean open) {
        ResourceLocation front = cabinetTexture(wood, open ? CabinetFace.FRONT_OPEN : CabinetFace.FRONT);
        ResourceLocation side = cabinetTexture(wood, CabinetFace.SIDE);
        ResourceLocation top = cabinetTexture(wood, CabinetFace.TOP);
        if (kind == Kind.HALF_CABINET) {
            return models().withExistingParent(name, HALF_CABINET).renderType(CUTOUT)
                    .texture("0", front).texture("1", side).texture("2", top)
                    .texture("particle", cabinetTexture(wood, CabinetFace.FRONT));
        }
        return models().orientable(name, side, front, top).renderType(CUTOUT);
    }

    private void wallpaper(Block block, DyeColor color, ResourceLocation planks) {
        ResourceLocation wallpaper = blockTexture(AestheticBlocks.WALLPAPER_BLOCKS.get(color.getId()).get());
        ModelFile bottom = models().withExistingParent(name(block), MOLDING_BOTTOM).renderType(CUTOUT)
                .texture("0", wallpaper).texture("1", planks).texture("particle", wallpaper);
        ModelFile top = models().withExistingParent(name(block) + "_top", MOLDING_TOP).renderType(CUTOUT)
                .texture("0", wallpaper).texture("1", planks).texture("particle", wallpaper);
        getVariantBuilder(block).partialState().with(MoldingBlock.HALF, Direction.DOWN)
                .modelForState().modelFile(bottom).addModel();
        getVariantBuilder(block).partialState().with(MoldingBlock.HALF, Direction.UP)
                .modelForState().modelFile(top).addModel();
    }

    private void cornFence(FenceBlock block, ResourceLocation planks) {
        ModelFile post = models().withExistingParent(name(block), DRIED_CORN_FENCE).renderType(CUTOUT).texture("1", planks);
        ModelFile side = models().fenceSide(name(block) + "_side", planks).renderType(CUTOUT);
        getMultipartBuilder(block).part().modelFile(post).addModel().end();
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            getMultipartBuilder(block).part().modelFile(side)
                    .rotationY(((int) direction.toYRot() + 180) % 360).uvLock(true).addModel()
                    .condition(CONNECTIONS.get(direction), true).end();
        }
    }

    private static ResourceLocation cabinetTexture(Wood wood, CabinetFace face) {
        // SD's oak top image is absent; retain the established FD oak appearance.
        ResourceLocation id = wood == Wood.OAK ? ModBlocks.OAK_CABINET.getId() : SDBlocks.CABINETS.get(wood).getId();
        return id.withPrefix("block/").withSuffix(face.suffix);
    }

    private enum CabinetFace {
        FRONT("_front"), FRONT_OPEN("_front_open"), SIDE("_side"), TOP("_top");
        private final String suffix;
        CabinetFace(String suffix) { this.suffix = suffix; }
    }

    private static String name(Block block) { return ForgeRegistries.BLOCKS.getKey(block).getPath(); }
    private static ResourceLocation edModel(String name) {
        return ResourceLocation.fromNamespaceAndPath("extradelight", "block/" + name);
    }
}
