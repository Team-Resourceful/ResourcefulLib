package com.teamresourceful.resourcefullib.client.fluid.neoforge;

import com.teamresourceful.resourcefullib.client.fluid.data.ClientFluidProperties;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.FluidRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.fluid.CustomFluidRenderer;
import org.jetbrains.annotations.NotNull;

public record ResourcefulFluidRenderer(ClientFluidProperties properties) implements CustomFluidRenderer {

    @Override
    public boolean renderFluid(@NotNull FluidRenderer renderer, @NotNull FluidState fluid, @NotNull BlockAndTintGetter getter, @NotNull BlockPos pos, @NotNull FluidRenderer.Output output, @NotNull BlockState block) {
        return this.properties.renderFluid(pos, getter, output, block, fluid);
    }
}
