package com.teamresourceful.resourcefullib.client.fluid.fabric;

import com.teamresourceful.resourcefullib.client.fluid.data.ClientFluidProperties;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record FluidBlockTintSource(ClientFluidProperties properties) implements BlockTintSource {

    @Override
    public int color(BlockState state) {
        return this.properties.tintColor(null, null, state.getFluidState());
    }

    @Override
    public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
        return this.properties.tintColor(level, pos, state.getFluidState());
    }

    @Override
    public int colorAsTerrainParticle(BlockState state, BlockAndTintGetter level, BlockPos pos) {
        return this.properties.tintColor(level, pos, state.getFluidState());
    }
}
