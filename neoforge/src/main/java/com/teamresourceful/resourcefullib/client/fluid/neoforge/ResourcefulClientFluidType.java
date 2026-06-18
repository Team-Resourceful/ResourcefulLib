package com.teamresourceful.resourcefullib.client.fluid.neoforge;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamresourceful.resourcefullib.client.fluid.data.ClientFluidProperties;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.environment.FogEnvironment;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4f;

public record ResourcefulClientFluidType(ClientFluidProperties properties) implements IClientFluidTypeExtensions {

    @Override
    public void renderOverlay(@NotNull Minecraft mc, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector submitNodeCollector) {
        this.properties.renderOverlay(mc, poseStack, submitNodeCollector);
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
