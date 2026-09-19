package com.teamresourceful.resourcefullib.client.fluid.neoforge;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamresourceful.resourcefullib.client.fluid.data.ClientFluidProperties;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.environment.FogEnvironment;
import net.minecraft.client.renderer.state.level.PlayerRenderState;
import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4f;
import org.jspecify.annotations.NonNull;

public record ResourcefulClientFluidType(ClientFluidProperties properties) implements IClientFluidTypeExtensions {

    @Override
    public void extractOverlay(
        @NonNull Minecraft minecraft,
        @NonNull LocalPlayer player,
        @NonNull PlayerRenderState playerRenderState,
        @NonNull BlockPos eyePos,
        float brightness) {
        playerRenderState.customFluidOverlayRenderer = (_, _, submitNodeCollector, poseStack, _, _, _) -> {
            this.properties.renderOverlay(minecraft, poseStack, submitNodeCollector);
            return true;
        };
    }

    @Override
    public void modifyFogColor(@NotNull Camera camera, float partialTick, @NotNull ClientLevel level, int renderDistance, float darkenWorldAmount, @NotNull Vector4f color) {
        color.set(this.properties.modifyFogColor(camera, partialTick, level, renderDistance, darkenWorldAmount, color));
    }

    @Override
    public void modifyFogRender(@NotNull Camera camera, @Nullable FogEnvironment environment, float renderDistance, float partialTick, @NotNull FogData data) {
        var newData = this.properties.modifyFogRender(camera, renderDistance, partialTick, data);
        data.environmentalStart = newData.environmentalStart;
        data.renderDistanceStart = newData.renderDistanceStart;
        data.environmentalEnd = newData.environmentalEnd;
        data.renderDistanceEnd = newData.renderDistanceEnd;
        data.skyEnd = newData.skyEnd;
        data.cloudEnd = newData.cloudEnd;
    }
}
