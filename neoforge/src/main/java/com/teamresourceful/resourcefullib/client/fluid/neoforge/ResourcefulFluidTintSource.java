package com.teamresourceful.resourcefullib.client.fluid.neoforge;

import com.teamresourceful.resourcefullib.client.fluid.data.ClientFluidProperties;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.fluid.FluidTintSource;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record ResourcefulFluidTintSource(ClientFluidProperties properties) implements FluidTintSource {

    @Override
    public int color(FluidState state) {
        return this.properties.tintColor(null, null, state);
    }

    @Override
    public int colorInWorld(FluidState fluidState, BlockState blockState, BlockAndTintGetter level, BlockPos pos) {
        return this.properties.tintColor(level, pos, fluidState);
    }

    @Override
    public int colorAsStack(FluidStack stack) {
        return this.properties.tintColor(null, null, stack.getFluid().defaultFluidState());
    }

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
