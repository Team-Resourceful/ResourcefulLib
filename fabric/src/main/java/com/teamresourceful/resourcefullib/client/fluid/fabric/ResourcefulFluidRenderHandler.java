package com.teamresourceful.resourcefullib.client.fluid.fabric;

import com.teamresourceful.resourcefullib.common.fluid.data.FluidData;
import com.teamresourceful.resourcefullib.common.fluid.data.FluidProperties;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;

public class ResourcefulFluidRenderHandler implements FluidRenderHandler {

    protected final FluidProperties properties;
    protected TextureAtlasSprite[] sprites;

    private ResourcefulFluidRenderHandler(FluidProperties properties) {
        this.properties = properties;
        this.sprites = new TextureAtlasSprite[2];
    }

    public static void register(FluidData data) {
        FlowingFluid still = data.still().get();
        FlowingFluid flowing = data.flowing().get();

        if (still == null && flowing == null) return;
        FluidRenderHandlerRegistry.INSTANCE.register(still, new ResourcefulFluidRenderHandler(data.properties()));
        FluidRenderHandlerRegistry.INSTANCE.register(flowing, new ResourcefulFluidRenderHandler(data.properties()));
    }

    @Override
    public TextureAtlasSprite[] getFluidSprites(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, FluidState state) {
        return this.sprites;
    }

    @Override
    public void reloadTextures(TextureAtlas textureAtlas) {
        if (properties.overlay() != null && sprites.length < 3) {
            sprites = new TextureAtlasSprite[3];
        } else if (properties.overlay() == null && sprites.length > 2) {
            sprites = new TextureAtlasSprite[2];
        }
        sprites[0] = textureAtlas.getSprite(properties.still());
        sprites[1] = textureAtlas.getSprite(properties.flowing());

        if (properties.overlay() != null) {
            sprites[2] = textureAtlas.getSprite(properties.overlay());
        }
    }

    @Override
    public int getFluidColor(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, FluidState state) {
        return properties.tintColor();
    }
}
