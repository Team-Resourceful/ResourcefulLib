package com.teamresourceful.resourcefullib.mixins.fabric;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamresourceful.resourcefullib.client.fluid.data.ClientFluidProperties;
import com.teamresourceful.resourcefullib.client.fluid.fabric.EntityFluidEyesHook;
import com.teamresourceful.resourcefullib.client.fluid.registry.ResourcefulClientFluidRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRendererMixin {

    @Inject(method = "renderScreenEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isEyeInFluid(Lnet/minecraft/tags/TagKey;)Z", shift = At.Shift.BEFORE))
    private static void rlib_renderScreenEffect(Minecraft minecraft, PoseStack poseStack, CallbackInfo ci) {
        Player player = minecraft.player;
        if (player instanceof EntityFluidEyesHook hook && hook.rlib$getEyesFluid() != null) {
            ResourceLocation id = hook.rlib$getEyesFluid().getData().id();
            ClientFluidProperties properties = ResourcefulClientFluidRegistry.get(id);
            if (properties != null) {
                properties.renderOverlay(minecraft, poseStack);
            }
        }
    }
}
