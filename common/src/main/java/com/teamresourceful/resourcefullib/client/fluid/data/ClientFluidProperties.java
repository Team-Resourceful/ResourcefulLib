package com.teamresourceful.resourcefullib.client.fluid.data;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.datafixers.util.Function3;
import com.mojang.datafixers.util.Function5;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public interface ClientFluidProperties {

    ResourceLocation still(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, FluidState state);

    ResourceLocation flowing(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, FluidState state);

    ResourceLocation overlay(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, FluidState state);

    ResourceLocation screenOverlay();

    default void renderOverlay(Minecraft minecraft, PoseStack stack) {
        ResourceLocation texture = screenOverlay();
        if (texture != null) {
            Player player = minecraft.player;
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, texture);

            BlockPos blockpos = BlockPos.containing(player.getX(), player.getEyeY(), player.getZ());
            float brightness = LightTexture.getBrightness(player.level().dimensionType(), player.level().getMaxLocalRawBrightness(blockpos));

            BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
            RenderSystem.enableBlend();
            RenderSystem.setShaderColor(brightness, brightness, brightness, 0.1F);
            float f7 = -player.getYRot() / 64.0F;
            float f8 = player.getXRot() / 64.0F;
            Matrix4f matrix4f = stack.last().pose();
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            bufferbuilder.vertex(matrix4f, -1.0F, -1.0F, -0.5F).uv(4.0F + f7, 4.0F + f8).endVertex();
            bufferbuilder.vertex(matrix4f, 1.0F, -1.0F, -0.5F).uv(0.0F + f7, 4.0F + f8).endVertex();
            bufferbuilder.vertex(matrix4f, 1.0F, 1.0F, -0.5F).uv(0.0F + f7, 0.0F + f8).endVertex();
            bufferbuilder.vertex(matrix4f, -1.0F, 1.0F, -0.5F).uv(4.0F + f7, 0.0F + f8).endVertex();
            BufferUploader.drawWithShader(bufferbuilder.end());
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.disableBlend();
        }
    }

    int tintColor(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, FluidState state);

    default boolean renderFluid(BlockPos pos, BlockAndTintGetter world, VertexConsumer vertexConsumer, BlockState blockState, FluidState fluidState) {
        return false;
    }

    default Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
        return fluidFogColor;
    }

    default void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick, float nearDistance, float farDistance, FogShape shape) {

    }

    static ClientFluidProperties.Builder builder() {
        return new ClientFluidProperties.Builder();
    }

    class Builder {

        private Function3<BlockAndTintGetter, BlockPos, FluidState, ResourceLocation> still = (a, b, c) -> null;
        private Function3<BlockAndTintGetter, BlockPos, FluidState, ResourceLocation> flowing = (a, b, c) -> null;
        private Function3<BlockAndTintGetter, BlockPos, FluidState, ResourceLocation> overlay = (a, b, c) -> null;
        private ResourceLocation screenOverlay = null;
        private Function3<BlockAndTintGetter, BlockPos, FluidState, Integer> tintColor = (a, b, c) -> -1;
        private Function5<BlockPos, BlockAndTintGetter, VertexConsumer, BlockState, FluidState, Boolean> renderFluid = (a, b, c, d, e) -> false;

        public Builder still(ResourceLocation still) {
            this.still = (a, b, c) -> still;
            return this;
        }

        public Builder flowing(ResourceLocation flowing) {
            this.flowing = (a, b, c) -> flowing;
            return this;
        }

        public Builder overlay(ResourceLocation overlay) {
            this.overlay = (a, b, c) -> overlay;
            return this;
        }

        public Builder screenOverlay(ResourceLocation screenOverlay) {
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

        public Builder renderFluid(Function5<BlockPos, BlockAndTintGetter, VertexConsumer, BlockState, FluidState, Boolean> renderFluid) {
            this.renderFluid = renderFluid;
            return this;
        }

        public ClientFluidProperties build() {
            return new ClientFluidProperties() {

                @Override
                public ResourceLocation still(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, FluidState state) {
                    return still.apply(view, pos, state);
                }

                @Override
                public ResourceLocation flowing(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, FluidState state) {
                    return flowing.apply(view, pos, state);
                }

                @Override
                public ResourceLocation overlay(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, FluidState state) {
                    return overlay.apply(view, pos, state);
                }

                @Override
                public ResourceLocation screenOverlay() {
                    return screenOverlay;
                }

                @Override
                public int tintColor(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, FluidState state) {
                    return tintColor.apply(view, pos, state);
                }

                @Override
                public boolean renderFluid(BlockPos pos, BlockAndTintGetter world, VertexConsumer vertexConsumer, BlockState blockState, FluidState fluidState) {
                    return renderFluid.apply(pos, world, vertexConsumer, blockState, fluidState);
                }
            };
        }
    }
}
