package com.teamresourceful.resourcefullib.mixins.fabric;

import com.teamresourceful.resourcefullib.client.fluid.data.ClientFluidProperties;
import com.teamresourceful.resourcefullib.client.fluid.registry.ResourcefulClientFluidRegistry;
import com.teamresourceful.resourcefullib.common.fluid.ResourcefulFlowingFluid;
import com.teamresourceful.resourcefullib.common.fluid.data.FluidData;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogParameters;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.level.material.FluidState;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FogRenderer.class)
public class FogRendererMixin {

    @Inject(method = "setupFog", at = @At("RETURN"), cancellable = true)
    private static void setupFog(
            Camera camera,
            FogRenderer.FogMode fogMode,
            Vector4f vector4f,
            float partialTicks,
            boolean ignored,
            float renderDistance,
            CallbackInfoReturnable<FogParameters> cir
    ) {
        FogParameters parameters = cir.getReturnValue();
        FluidState state = camera.getEntity().level().getFluidState(camera.getBlockPosition());
        double fluidY = camera.getBlockPosition().getY() + state.getHeight(camera.getEntity().level(), camera.getBlockPosition());
        if (camera.getPosition().y >= fluidY) return;
        if (!(state.getType() instanceof ResourcefulFlowingFluid fluid)) return;
        FluidData data = fluid.getData();
        ClientFluidProperties properties = ResourcefulClientFluidRegistry.get(data.id());
        if (properties == null) return;

        cir.setReturnValue(properties.modifyFogRender(camera, fogMode, partialTicks, renderDistance, parameters));
    }

    @Inject(method = "computeFogColor", at = @At("RETURN"), cancellable = true)
    private static void setupColor(
            Camera camera,
            float partialTicks,
            ClientLevel clientLevel,
            int renderDistance,
            float darkenWorldAmount,
            CallbackInfoReturnable<Vector4f> cir
    ) {
        FluidState state = camera.getEntity().level().getFluidState(camera.getBlockPosition());
        double fluidY = camera.getBlockPosition().getY() + state.getHeight(camera.getEntity().level(), camera.getBlockPosition());
        if (camera.getPosition().y >= fluidY) return;
        if (!(state.getType() instanceof ResourcefulFlowingFluid fluid)) return;
        FluidData data = fluid.getData();
        ClientFluidProperties properties = ResourcefulClientFluidRegistry.get(data.id());
        if (properties == null) return;

        cir.setReturnValue(properties.modifyFogColor(camera, partialTicks, clientLevel, renderDistance, darkenWorldAmount, cir.getReturnValue()));
    }
}
