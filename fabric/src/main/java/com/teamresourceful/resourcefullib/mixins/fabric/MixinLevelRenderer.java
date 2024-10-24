package com.teamresourceful.resourcefullib.mixins.fabric;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamresourceful.resourcefullib.client.highlights.HighlightHandler;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class MixinLevelRenderer {

    @Inject(method = "renderHitOutline", at = @At("HEAD"), cancellable = true)
    public void onRenderHitOutline(PoseStack poseStack, VertexConsumer vertexConsumer, Entity entity, double d, double e, double f, BlockPos blockPos, BlockState blockState, int i, CallbackInfo ci) {
        if (HighlightHandler.onBlockHighlight(new Vec3(d, e, f), entity, poseStack, blockPos, blockState, vertexConsumer, i)) {
            ci.cancel();
        }
    }
}
