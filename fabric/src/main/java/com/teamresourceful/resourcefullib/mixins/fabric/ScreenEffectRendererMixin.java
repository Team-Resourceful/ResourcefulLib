package com.teamresourceful.resourcefullib.mixins.fabric;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.teamresourceful.resourcefullib.client.fluid.data.ClientFluidProperties;
import com.teamresourceful.resourcefullib.fabric.ResourcefulLibFabricClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.PlayerRenderState;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRendererMixin {

    @Shadow
    @Final
    private GameRenderer gameRenderer;

    @Inject(method = "submit", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/state/level/PlayerRenderState;waterOverlay:Lnet/minecraft/client/renderer/state/level/PlayerRenderState$WaterOverlay;", opcode = Opcodes.GETFIELD))
    private void rlib_renderScreenEffect(
            float partialTicks,
            SubmitNodeCollector submitNodeCollector,
            PlayerRenderState playerRenderState,
            CameraRenderState cameraRenderState,
            boolean hideGui,
            CallbackInfo ci,
            @Local PoseStack poseStack
    ) {
        ClientFluidProperties properties = playerRenderState.getData(ResourcefulLibFabricClient.FLUID_DATA_KEY);
        if (properties == null) {
            return;
        }

        properties.renderOverlay(Minecraft.getInstance(), poseStack, submitNodeCollector);
    }
}
