package com.teamresourceful.resourcefullib.mixins.fabric;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.teamresourceful.resourcefullib.client.fluid.data.ClientFluidProperties;
import com.teamresourceful.resourcefullib.client.fluid.fabric.EntityFluidEyesHook;
import com.teamresourceful.resourcefullib.client.fluid.registry.ResourcefulClientFluidRegistry;
import com.teamresourceful.resourcefullib.common.fluid.ResourcefulFlowingFluid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRendererMixin {

    @Shadow @Final private Minecraft minecraft;

    @Shadow @Final private MultiBufferSource bufferSource;

    @Inject(method = "renderScreenEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isEyeInFluid(Lnet/minecraft/tags/TagKey;)Z", shift = At.Shift.BEFORE))
    private void rlib_renderScreenEffect(boolean bl, float f, CallbackInfo ci, @Local PoseStack stack) {
        Player player = this.minecraft.player;
        if (player instanceof EntityFluidEyesHook hook && hook.rlib$getEyesFluid() != null && hook.rlib$getEyesFluid().getType() instanceof ResourcefulFlowingFluid fluid) {
            ResourceLocation id = fluid.getData().id();
            ClientFluidProperties properties = ResourcefulClientFluidRegistry.get(id);
            if (properties != null) {
                properties.renderOverlay(this.minecraft, stack, this.bufferSource);
            }
        }
    }
}
