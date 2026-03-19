package com.teamresourceful.resourcefullib.mixins.fabric;

import com.teamresourceful.resourcefullib.client.fluid.registry.ResourcefulClientFluidRegistry;
import com.teamresourceful.resourcefullib.common.fluid.ResourcefulFlowingFluid;
import com.teamresourceful.resourcefullib.common.fluid.data.FluidData;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.FogRenderer;
import net.minecraft.world.level.material.FluidState;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FogRenderer.class)
public class FogRendererMixin {

    @Inject(method = "setupFog", at = @At("RETURN"), cancellable = true)
    private void setupFog(
            Camera camera,
            int renderDistance, DeltaTracker deltaTracker, float darkenWorldAmount,
            ClientLevel level,
            CallbackInfoReturnable<FogData> cir
    ) {
        FluidState state = level.getFluidState(camera.blockPosition());
        double fluidY = camera.blockPosition().getY() + state.getHeight(level, camera.blockPosition());
        if (camera.position().y >= fluidY) return;
        if (!(state.getType() instanceof ResourcefulFlowingFluid fluid)) return;
        var properties = ResourcefulClientFluidRegistry.get(fluid.getData().id());
        if (properties == null) return;
        cir.setReturnValue(properties.modifyFogRender(
                camera,
                renderDistance,
                deltaTracker.getGameTimeDeltaPartialTick(false),
                cir.getReturnValue()
        ));
    }

    @Inject(method = "computeFogColor", at = @At("TAIL"))
    private void setupColor(
            Camera camera,
            float partialTicks,
            ClientLevel clientLevel,
            int renderDistance, float darkenWorldAmount,
            Vector4f dest,
            CallbackInfo ci
    ) {
        FluidState state = camera.entity().level().getFluidState(camera.blockPosition());
        double fluidY = camera.blockPosition().getY() + state.getHeight(camera.entity().level(), camera.blockPosition());
        if (camera.position().y >= fluidY) return;
        if (!(state.getType() instanceof ResourcefulFlowingFluid fluid)) return;
        FluidData data = fluid.getData();
        var properties = ResourcefulClientFluidRegistry.get(data.id());
        if (properties == null) return;

        dest.set(properties.modifyFogColor(camera, partialTicks, clientLevel, renderDistance, darkenWorldAmount, dest));
    }
}
