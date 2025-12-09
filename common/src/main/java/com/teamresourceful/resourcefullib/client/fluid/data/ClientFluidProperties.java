package com.teamresourceful.resourcefullib.client.fluid.data;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Function3;
import com.mojang.datafixers.util.Function6;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.util.function.Function;

public interface ClientFluidProperties {

    Identifier still(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, @Nullable FluidState state);

    Identifier flowing(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, @Nullable FluidState state);

    Identifier overlay(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, @Nullable FluidState state);

    Identifier screenOverlay();

    default void renderOverlay(Minecraft minecraft, PoseStack stack, MultiBufferSource source) {
        Identifier texture = screenOverlay();
        if (texture != null) {
            Player player = minecraft.player;
            BlockPos blockpos = BlockPos.containing(player.getX(), player.getEyeY(), player.getZ());
            float brightness = LightTexture.getBrightness(player.level().dimensionType(), player.level().getMaxLocalRawBrightness(blockpos));
            int color = ARGB.colorFromFloat(0.1F, brightness, brightness, brightness);

            float a = -player.getYRot() / 64.0F;
            float b = player.getXRot() / 64.0F;
            Matrix4f matrix = stack.last().pose();
            VertexConsumer consumer = source.getBuffer(RenderTypes.blockScreenEffect(texture));
            consumer.addVertex(matrix, -1.0F, -1.0F, -0.5F).setUv(4.0F + a, 4.0F + b).setColor(color);
            consumer.addVertex(matrix, 1.0F, -1.0F, -0.5F).setUv(0.0F + a, 4.0F + b).setColor(color);
            consumer.addVertex(matrix, 1.0F, 1.0F, -0.5F).setUv(0.0F + a, 0.0F + b).setColor(color);
            consumer.addVertex(matrix, -1.0F, 1.0F, -0.5F).setUv(4.0F + a, 0.0F + b).setColor(color);
        }
    }

    int tintColor(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, @Nullable FluidState state);

    default boolean renderFluid(
            BlockPos pos,
            BlockAndTintGetter world, VertexConsumer vertexConsumer,
            BlockState blockState, FluidState fluidState,
            Function<Identifier, TextureAtlasSprite> sprites
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

        private Function3<BlockAndTintGetter, BlockPos, FluidState, Identifier> still = (a, b, c) -> null;
        private Function3<BlockAndTintGetter, BlockPos, FluidState, Identifier> flowing = (a, b, c) -> null;
        private Function3<BlockAndTintGetter, BlockPos, FluidState, Identifier> overlay = (a, b, c) -> null;
        private Identifier screenOverlay = null;
        private Function3<BlockAndTintGetter, BlockPos, FluidState, Integer> tintColor = (a, b, c) -> -1;
        private Function6<BlockPos, BlockAndTintGetter, VertexConsumer, BlockState, FluidState, Function<Identifier, TextureAtlasSprite>, Boolean> renderFluid = (a, b, c, d, e, f) -> false;

        public Builder still(Identifier still) {
            this.still = (a, b, c) -> still;
            return this;
        }

        public Builder flowing(Identifier flowing) {
            this.flowing = (a, b, c) -> flowing;
            return this;
        }

        public Builder overlay(Identifier overlay) {
            this.overlay = (a, b, c) -> overlay;
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

        public Builder renderFluid(Function6<BlockPos, BlockAndTintGetter, VertexConsumer, BlockState, FluidState, Function<Identifier, TextureAtlasSprite>, Boolean> renderFluid) {
            this.renderFluid = renderFluid;
            return this;
        }

        public ClientFluidProperties build() {
            return new ClientFluidProperties() {

                @Override
                public Identifier still(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, @Nullable FluidState state) {
                    return still.apply(view, pos, state);
                }

                @Override
                public Identifier flowing(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, @Nullable FluidState state) {
                    return flowing.apply(view, pos, state);
                }

                @Override
                public Identifier overlay(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, @Nullable FluidState state) {
                    return overlay.apply(view, pos, state);
                }

                @Override
                public Identifier screenOverlay() {
                    return screenOverlay;
                }

                @Override
                public int tintColor(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, @Nullable FluidState state) {
                    return tintColor.apply(view, pos, state);
                }

                @Override
                public boolean renderFluid(BlockPos pos, BlockAndTintGetter world, VertexConsumer vertexConsumer, BlockState blockState, FluidState fluidState, Function<Identifier, TextureAtlasSprite> sprites) {
                    return renderFluid.apply(pos, world, vertexConsumer, blockState, fluidState, sprites);
                }
            };
        }
    }
}
