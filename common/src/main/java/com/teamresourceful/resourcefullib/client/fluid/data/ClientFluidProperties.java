package com.teamresourceful.resourcefullib.client.fluid.data;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Function3;
import com.mojang.datafixers.util.Function5;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;

public interface ClientFluidProperties {

    ResourceLocation still(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, FluidState state);

    ResourceLocation flowing(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, FluidState state);

    ResourceLocation overlay(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, FluidState state);

    ResourceLocation screenOverlay();

    int tintColor(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, FluidState state);

    default boolean renderFluid(BlockPos pos, BlockAndTintGetter world, VertexConsumer vertexConsumer, BlockState blockState, FluidState fluidState) {
        return false;
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
