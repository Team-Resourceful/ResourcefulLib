package com.teamresourceful.resourcefullib.common.fluid.neoforge;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamresourceful.resourcefullib.client.fluid.data.ClientFluidProperties;
import com.teamresourceful.resourcefullib.client.fluid.registry.ResourcefulClientFluidRegistry;
import com.teamresourceful.resourcefullib.common.fluid.data.FluidProperties;
import net.minecraft.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.textures.FluidSpriteCache;
import net.neoforged.neoforge.common.SoundAction;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.function.Consumer;

public class ResourcefulFluidType extends FluidType {

    private final ResourceLocation id;

    public ResourcefulFluidType(ResourceLocation id, FluidProperties props) {
        super(Util.make(Properties.create(), properties -> {
            properties.descriptionId(Util.makeDescriptionId("fluid_type", id));
            properties.adjacentPathType(props.adjacentPathType());
            properties.canConvertToSource(props.canConvertToSource());
            properties.canDrown(props.canDrown());
            properties.canExtinguish(props.canExtinguish());
            properties.canHydrate(props.canHydrate());
            properties.canPushEntity(props.canPushEntity());
            properties.canSwim(props.canSwim());
            properties.density(props.density());
            properties.fallDistanceModifier(props.fallDistanceModifier());
            properties.lightLevel(props.lightLevel());
            properties.motionScale(props.motionScale());
            properties.pathType(props.pathType());
            properties.rarity(props.rarity());
            properties.temperature(props.temperature());
            properties.viscosity(props.viscosity());
            props.sounds().sounds().forEach((name, sound) -> properties.sound(SoundAction.get(name), sound));
        }));
        this.id = id;
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        var type = this;
        consumer.accept(new IClientFluidTypeExtensions() {

            private ClientFluidProperties properties = null;

            private ClientFluidProperties properties() {
                if (properties == null) {
                    properties = ResourcefulClientFluidRegistry.get(type.id);
                }
                return properties;
            }

            @Override
            public @NotNull ResourceLocation getStillTexture() {
                return properties().still(null, null, null);
            }

            @Override
            public @NotNull ResourceLocation getStillTexture(@NotNull FluidStack stack) {
                return properties().still(null, null, stack.getFluid().defaultFluidState());
            }

            @Override
            public @NotNull ResourceLocation getStillTexture(@NotNull FluidState state, @NotNull BlockAndTintGetter getter, @NotNull BlockPos pos) {
                return properties().still(getter, pos, state);
            }

            @Override
            public @NotNull ResourceLocation getFlowingTexture() {
                return properties().flowing(null, null, null);
            }

            @Override
            public @NotNull ResourceLocation getFlowingTexture(@NotNull FluidStack stack) {
                return properties().flowing(null, null, stack.getFluid().defaultFluidState());
            }

            @Override
            public @NotNull ResourceLocation getFlowingTexture(@NotNull FluidState state, @NotNull BlockAndTintGetter getter, @NotNull BlockPos pos) {
                return properties().flowing(getter, pos, state);
            }

            @Override
            public @Nullable ResourceLocation getOverlayTexture() {
                return properties().overlay(null, null, null);
            }

            @Override
            public @NotNull ResourceLocation getOverlayTexture(@NotNull FluidStack stack) {
                return properties().overlay(null, null, stack.getFluid().defaultFluidState());
            }

            @Override
            public @NotNull ResourceLocation getOverlayTexture(@NotNull FluidState state, @NotNull BlockAndTintGetter getter, @NotNull BlockPos pos) {
                return properties().overlay(getter, pos, state);
            }

            @Override
            public void renderOverlay(@NotNull Minecraft mc, @NotNull PoseStack poseStack) {
                properties().renderOverlay(mc, poseStack);
            }

            @Override
            public int getTintColor() {
                return properties().tintColor(null, null, null);
            }

            @Override
            public int getTintColor(@NotNull FluidStack stack) {
                return properties().tintColor(null, null, stack.getFluid().defaultFluidState());
            }

            @Override
            public int getTintColor(@NotNull FluidState state, @NotNull BlockAndTintGetter getter, @NotNull BlockPos pos) {
                return properties().tintColor(getter, pos, state);
            }

            @Override
            public boolean renderFluid(@NotNull FluidState fluidState, @NotNull BlockAndTintGetter getter, @NotNull BlockPos pos, @NotNull VertexConsumer vertexConsumer, @NotNull BlockState blockState) {
                if (properties().renderFluid(pos, getter, vertexConsumer, blockState, fluidState, FluidSpriteCache::getSprite)) {
                    return true;
                }
                return IClientFluidTypeExtensions.super.renderFluid(fluidState, getter, pos, vertexConsumer, blockState);
            }

            @Override
            public @NotNull Vector3f modifyFogColor(@NotNull Camera camera, float partialTick, @NotNull ClientLevel level, int renderDistance, float darkenWorldAmount, @NotNull Vector3f fluidFogColor) {
                return properties().modifyFogColor(camera, partialTick, level, renderDistance, darkenWorldAmount, fluidFogColor);
            }

            @Override
            public void modifyFogRender(@NotNull Camera camera, FogRenderer.@NotNull FogMode mode, float renderDistance, float partialTick, float nearDistance, float farDistance, @NotNull FogShape shape) {
                properties().modifyFogRender(camera, mode, renderDistance, partialTick, nearDistance, farDistance, shape);
            }
        });
    }
}
