package com.teamresourceful.resourcefullib.client.fluid.fabric;

import com.teamresourceful.resourcefullib.client.fluid.data.ClientFluidProperties;
import com.teamresourceful.resourcefullib.client.fluid.registry.ResourcefulClientFluidRegistry;
import com.teamresourceful.resourcefullib.common.fluid.data.FluidData;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderingRegistry;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.block.FluidRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record ResourcefulFluidRenderHandler(ClientFluidProperties properties) implements FluidRenderHandler {

    public static void register(Identifier id, FluidData data) {
        var still = data.still().get();
        var flowing = data.flowing().get();

        if (still == null && flowing == null) return;

        var properties = Objects.requireNonNull(
                ResourcefulClientFluidRegistry.get(id),
                "ClientFluidProperties for " + id + " was not found! Make sure to register it."
        );
        var handler = new ResourcefulFluidRenderHandler(properties);
        var tint = new FluidBlockTintSource(properties);
        var model = new FluidModel.Unbaked(properties.still(), properties.flowing(), properties.overlay(), tint);

        FluidRenderingRegistry.register(still, model, handler);
        FluidRenderingRegistry.register(flowing, model, handler);
    }

    @Override
    public void renderFluid(@NotNull FluidRenderer renderer, @NotNull BlockPos pos, @NotNull BlockAndTintGetter level, @NotNull FluidRenderer.Output output, @NotNull BlockState block, @NotNull FluidState fluid) {
        if (!properties().renderFluid(pos, level, output, block, fluid)) {
            FluidRenderHandler.super.renderFluid(renderer, pos, level, output, block, fluid);
        }
    }

}
