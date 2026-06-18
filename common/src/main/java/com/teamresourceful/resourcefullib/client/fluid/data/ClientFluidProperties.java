package com.teamresourceful.resourcefullib.client.fluid.data;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Function3;
import com.mojang.datafixers.util.Function6;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.Lightmap;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.FluidRenderer;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.util.function.Function;

public interface ClientFluidProperties {

    Material still();
    Material flowing();
    Material overlay();

    Identifier screenOverlay();

    default void renderOverlay(Minecraft minecraft, PoseStack stack, SubmitNodeCollector submitNodeCollector) {
        Identifier texture = screenOverlay();
        if (texture != null) {
            Player player = minecraft.player;
            BlockPos blockpos = BlockPos.containing(player.getX(), player.getEyeY(), player.getZ());
            float brightness = Lightmap.getBrightness(player.level().dimensionType(), player.level().getMaxLocalRawBrightness(blockpos));
            int color = ARGB.colorFromFloat(0.1F, brightness, brightness, brightness);

            float a = -player.getYRot() / 64.0F;
            float b = player.getXRot() / 64.0F;

            submitNodeCollector.submitCustomGeometry(stack, RenderTypes.blockScreenEffect(texture), (pose, builder) -> {
                Matrix4f matrix = pose.pose();
                builder.addVertex(matrix, -1.0F, -1.0F, -0.5F).setUv(4.0F + a, 4.0F + b).setColor(color);
                builder.addVertex(matrix, 1.0F, -1.0F, -0.5F).setUv(0.0F + a, 4.0F + b).setColor(color);
                builder.addVertex(matrix, 1.0F, 1.0F, -0.5F).setUv(0.0F + a, 0.0F + b).setColor(color);
                builder.addVertex(matrix, -1.0F, 1.0F, -0.5F).setUv(4.0F + a, 0.0F + b).setColor(color);
            });
        }
    }

    int tintColor(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, @Nullable FluidState state);

    default boolean renderFluid(
            BlockPos pos,
            BlockAndTintGetter world,
            FluidRenderer.Output output,
            BlockState blockState, FluidState fluidState
    ) {
        return false;
    }

    default Vector4f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector4f fluidFogColor) {
        return fluidFogColor;
    }

    default FogData modifyFogRender(Camera camera, float renderDistance, float partialTick, FogData data) {
        return data;
    }

    static ClientFluidProperties.Builder builder() {
        return new ClientFluidProperties.Builder();
    }

    class Builder {

        private Material still = null;
        private Material flowing = null;
        private Material overlay = null;
        private Identifier screenOverlay = null;
        private Function3<BlockAndTintGetter, BlockPos, FluidState, Integer> tintColor = (a, b, c) -> -1;
        private Function6<BlockPos, BlockAndTintGetter, VertexConsumer, BlockState, FluidState, Function<Identifier, TextureAtlasSprite>, Boolean> renderFluid = (a, b, c, d, e, f) -> false;

        public Builder still(Identifier still) {
            this.still = new Material(still);
            return this;
        }

        public Builder flowing(Identifier flowing) {
            this.flowing = new Material(flowing);
            return this;
        }

        public Builder overlay(Identifier overlay) {
            this.overlay = new Material(overlay);
            return this;
        }

        public Builder screenOverlay(Identifier screenOverlay) {
            this.screenOverlay = screenOverlay;
            return this;
        }

        public Builder tintColor(Function3<BlockAndTintGetter, BlockPos, FluidState, Integer> tintColor) {
            this.tintColor = tintColor;
            return this;
        }

        public Builder tintColor(int tintColor) {
            this.tintColor = (a, b, c) -> tintColor;
            return this;
        }

        public ClientFluidProperties build() {
            return new ClientFluidProperties() {

                @Override
                public Material still() {
                    return still;
                }

                @Override
                public Material flowing() {
                    return flowing;
                }

                @Override
                public Material overlay() {
                    return overlay;
                }

                @Override
                public Identifier screenOverlay() {
                    return screenOverlay;
                }

                @Override
                public int tintColor(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, @Nullable FluidState state) {
                    return tintColor.apply(view, pos, state);
                }
            };
        }
    }
}
