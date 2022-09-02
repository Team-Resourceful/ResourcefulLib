package com.teamresourceful.resourcefullib.client.highlights.base;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public interface Highlightable {

    /**
     * @return returns a highlight of a block, null if no special highlight will be applied.
     */
    @Nullable
    Highlight getHighlight(Level level, BlockPos pos, BlockState state);
}
