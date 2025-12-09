package com.teamresourceful.resourcefullib.mixins.fabric;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FogRenderer.class)
public class FogRendererMixin {

    @Inject(method = "setupFog", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/fog/FogRenderer;updateBuffer(Ljava/nio/ByteBuffer;ILorg/joml/Vector4f;FFFFFF)V"))
    private void setupFog(
            Camera camera,
            int renderDistance, DeltaTracker deltaTracker, float darkenWorldAmount,
            ClientLevel level,
            CallbackInfoReturnable<Vector4f> cir,

            @Local LocalRef<FogData> data
    ) {
        FluidState state = level.getFluidState(camera.blockPosition());
        double fluidY = camera.blockPosition().getY() + state.getHeight(level, camera.blockPosition());
        if (camera.position().y >= fluidY) return;
        if (!(state.getType() instanceof ResourcefulFlowingFluid fluid)) return;
        var properties = ResourcefulClientFluidRegistry.get(fluid.getData().id());
        if (properties == null) return;
        data.set(properties.modifyFogRender(
                camera,
                renderDistance,
                deltaTracker.getGameTimeDeltaPartialTick(false),
                data.get()
        ));
    }

    @Inject(method = "computeFogColor", at = @At("RETURN"), cancellable = true)
    private void setupColor(
            Camera camera,
            float partialTicks,
            ClientLevel clientLevel,
            int renderDistance, float darkenWorldAmount,
            CallbackInfoReturnable<Vector4f> cir
    ) {
        FluidState state = camera.entity().level().getFluidState(camera.blockPosition());
        double fluidY = camera.blockPosition().getY() + state.getHeight(camera.entity().level(), camera.blockPosition());
        if (camera.position().y >= fluidY) return;
        if (!(state.getType() instanceof ResourcefulFlowingFluid fluid)) return;
        FluidData data = fluid.getData();
        var properties = ResourcefulClientFluidRegistry.get(data.id());
        if (properties == null) return;

        cir.setReturnValue(properties.modifyFogColor(camera, partialTicks, clientLevel, renderDistance, darkenWorldAmount, cir.getReturnValue()));
    }
}
