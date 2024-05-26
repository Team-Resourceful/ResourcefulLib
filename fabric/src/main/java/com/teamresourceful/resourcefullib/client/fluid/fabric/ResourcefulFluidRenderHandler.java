package com.teamresourceful.resourcefullib.client.fluid.fabric;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamresourceful.resourcefullib.client.fluid.data.ClientFluidProperties;
import com.teamresourceful.resourcefullib.client.fluid.registry.ResourcefulClientFluidRegistry;
import com.teamresourceful.resourcefullib.common.fluid.data.FluidData;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;

public class ResourcefulFluidRenderHandler implements FluidRenderHandler {

    protected final ResourceLocation id;
    protected ClientFluidProperties properties;
    protected TextureAtlas textureAtlas;
    protected TextureAtlasSprite[] sprites;

    private ResourcefulFluidRenderHandler(ResourceLocation id) {
        this.id = id;
        this.sprites = new TextureAtlasSprite[2];
    }

    private ClientFluidProperties properties() {
        if (properties == null) {
            properties = ResourcefulClientFluidRegistry.get(id);
        }
        return properties;
    }

    public static void register(ResourceLocation id, FluidData data) {
        FlowingFluid still = data.still().get();
        FlowingFluid flowing = data.flowing().get();

        if (still == null && flowing == null) return;
        FluidRenderHandlerRegistry.INSTANCE.register(still, new ResourcefulFluidRenderHandler(id));
        FluidRenderHandlerRegistry.INSTANCE.register(flowing, new ResourcefulFluidRenderHandler(id));
    }

    @Override
    public TextureAtlasSprite[] getFluidSprites(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, FluidState state) {
        if (sprites == null) {
            var overlay = properties().overlay(view, pos, state);
            sprites = new TextureAtlasSprite[overlay == null ? 2 : 3];
            sprites[0] = textureAtlas.getSprite(properties().still(view, pos, state));
            sprites[1] = textureAtlas.getSprite(properties().flowing(view, pos, state));
            if (overlay != null) sprites[2] = textureAtlas.getSprite(overlay);
        }
        return sprites;
    }

    @Override
    public void reloadTextures(TextureAtlas textureAtlas) {
        this.sprites = null;
    }

    @Override
    public int getFluidColor(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, FluidState state) {
        return properties.tintColor(view, pos, state);
    }

    @Override
    public void renderFluid(BlockPos pos, BlockAndTintGetter world, VertexConsumer vertexConsumer, BlockState blockState, FluidState fluidState) {
        if (!properties().renderFluid(pos, world, vertexConsumer, blockState, fluidState)) {
            FluidRenderHandler.super.renderFluid(pos, world, vertexConsumer, blockState, fluidState);
        }
    }
}
