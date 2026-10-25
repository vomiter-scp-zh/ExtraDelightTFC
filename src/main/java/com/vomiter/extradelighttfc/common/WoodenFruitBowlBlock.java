package com.vomiter.extradelighttfc.common;

import com.vomiter.extradelight.common.complex.displays.fruitbowl.FruitBowlBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.List;

public final class WoodenFruitBowlBlock extends FruitBowlBlock {
    public WoodenFruitBowlBlock() {
        super(Properties.copy(Blocks.OAK_PLANKS).noOcclusion());
    }

    @Override
    public int numStyles() { return 1; }

    @Override
    public int getCurrentStyle(BlockState state) { return 0; }

    @Override
    public BlockState getState(int style) { return defaultBlockState(); }

    @Override
    public void setNextStyle(Level level, BlockPos pos, BlockState state) {}

    @Override
    public void setPrevStyle(Level level, BlockPos pos, BlockState state) {}

    @Override
    public void setStyle(Level level, BlockPos pos, BlockState state, int style) {}

    @Override
    public void appendHoverText(ItemStack stack, BlockGetter level, List<Component> tooltip, TooltipFlag flag) {}

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        // Vanilla Block behavior. The upstream bowl also spawns its item manually;
        // these fixed variants instead use a normal loot table (including explosions).
        spawnDestroyParticles(level, player, pos, state);
        if (state.is(BlockTags.GUARDED_BY_PIGLINS)) {
            PiglinAi.angerNearbyPiglins(player, false);
        }
        level.gameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(player, state));
    }
}
