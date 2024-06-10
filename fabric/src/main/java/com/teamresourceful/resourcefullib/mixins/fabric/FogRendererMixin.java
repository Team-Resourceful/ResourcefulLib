package com.teamresourceful.resourcefullib.mixins.fabric;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import com.teamresourceful.resourcefullib.client.fluid.data.ClientFluidProperties;
import com.teamresourceful.resourcefullib.client.fluid.registry.ResourcefulClientFluidRegistry;
import com.teamresourceful.resourcefullib.common.fluid.ResourcefulFlowingFluid;
import com.teamresourceful.resourcefullib.common.fluid.data.FluidData;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.level.material.FluidState;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FogRenderer.class)
public class FogRendererMixin {

    @Shadow private static float fogRed;

    @Shadow private static float fogGreen;

    @Shadow private static float fogBlue;

    @Inject(method = "setupFog", at = @At("TAIL"))
    private static void setupFog(Camera camera, FogRenderer.FogMode fogMode, float renderDistance, boolean bl, float partialTicks, CallbackInfo ci) {
        FluidState state = camera.getEntity().level().getFluidState(camera.getBlockPosition());
        double fluidY = camera.getBlockPosition().getY() + state.getHeight(camera.getEntity().level(), camera.getBlockPosition());
        if (camera.getPosition().y >= fluidY) return;
        if (!(state.getType() instanceof ResourcefulFlowingFluid fluid)) return;
        FluidData data = fluid.getData();
        ClientFluidProperties properties = ResourcefulClientFluidRegistry.get(data.id());
        if (properties == null) return;

        float start = RenderSystem.getShaderFogStart();
        float end = RenderSystem.getShaderFogEnd();
        FogShape shape = RenderSystem.getShaderFogShape();
        properties.modifyFogRender(camera, fogMode, partialTicks, renderDistance, start, end, shape);
    }

    @Inject(method = "setupColor", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;clearColor(FFFF)V", shift = At.Shift.BEFORE))
    private static void setupColor(Camera camera, float partialTicks, ClientLevel clientLevel, int renderDistance, float darkenWorldAmount, CallbackInfo ci) {
        FluidState state = camera.getEntity().level().getFluidState(camera.getBlockPosition());
        double fluidY = camera.getBlockPosition().getY() + state.getHeight(camera.getEntity().level(), camera.getBlockPosition());
        if (camera.getPosition().y >= fluidY) return;
        if (!(state.getType() instanceof ResourcefulFlowingFluid fluid)) return;
        FluidData data = fluid.getData();
        ClientFluidProperties properties = ResourcefulClientFluidRegistry.get(data.id());
        if (properties == null) return;

        Vector3f color = new Vector3f(fogRed, fogGreen, fogBlue);
        color = properties.modifyFogColor(camera, partialTicks, clientLevel, renderDistance, darkenWorldAmount, color);
        fogRed = color.x;
        fogGreen = color.y;
        fogBlue = color.z;
    }
}
