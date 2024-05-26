package com.teamresourceful.resourcefullib.common.fluid.neoforge;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamresourceful.resourcefullib.client.fluid.data.ClientFluidProperties;
import com.teamresourceful.resourcefullib.client.fluid.registry.ResourcefulClientFluidRegistry;
import com.teamresourceful.resourcefullib.common.fluid.data.FluidProperties;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.common.SoundAction;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
            public @NotNull ResourceLocation getStillTexture(@NotNull FluidState state, @NotNull BlockAndTintGetter getter, @NotNull BlockPos pos) {
                return properties().still(getter, pos, state);
            }

            @Override
            public @NotNull ResourceLocation getFlowingTexture(@NotNull FluidState state, @NotNull BlockAndTintGetter getter, @NotNull BlockPos pos) {
                return properties().flowing(getter, pos, state);
            }

            @Override
            public @NotNull ResourceLocation getOverlayTexture(@NotNull FluidState state, @NotNull BlockAndTintGetter getter, @NotNull BlockPos pos) {
                return properties().overlay(getter, pos, state);
            }

            @Override
            public @Nullable ResourceLocation getRenderOverlayTexture(@NotNull Minecraft mc) {
                return properties().screenOverlay();
            }

            @Override
            public int getTintColor(@NotNull FluidState state, @NotNull BlockAndTintGetter getter, @NotNull BlockPos pos) {
                return properties().tintColor(getter, pos, state);
            }

            @Override
            public int getTintColor(@NotNull FluidStack stack) {
                return properties().tintColor(null, null, stack.getFluid().defaultFluidState());
            }

            @Override
            public boolean renderFluid(@NotNull FluidState fluidState, @NotNull BlockAndTintGetter getter, @NotNull BlockPos pos, @NotNull VertexConsumer vertexConsumer, @NotNull BlockState blockState) {
                if (properties().renderFluid(pos, getter, vertexConsumer, blockState, fluidState)) {
                    return true;
                }
                return IClientFluidTypeExtensions.super.renderFluid(fluidState, getter, pos, vertexConsumer, blockState);
            }
        });
    }
}
