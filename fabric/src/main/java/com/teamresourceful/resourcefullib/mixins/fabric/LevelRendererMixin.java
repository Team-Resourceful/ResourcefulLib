package com.teamresourceful.resourcefullib.mixins.fabric;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamresourceful.resourcefullib.client.fabric.BlockOutlineRenderStateExtension;
import com.teamresourceful.resourcefullib.client.highlights.HighlightHandler;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.state.BlockOutlineRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Shadow
    private @Nullable ClientLevel level;

    @WrapOperation(
            method = "extractBlockOutline",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/state/BlockOutlineRenderState;<init>(Lnet/minecraft/core/BlockPos;ZZLnet/minecraft/world/phys/shapes/VoxelShape;)V"
            )
    )
    private BlockOutlineRenderState resourcefullib$extractBlockOutline(BlockPos pos, boolean b1, boolean b2, VoxelShape shape, Operation<BlockOutlineRenderState> original, @Local(ordinal = 0) BlockState state) {
        var renderState = original.call(pos, b1, b2, shape);
        //noinspection ConstantValue
        if ((Object) renderState instanceof BlockOutlineRenderStateExtension extension) {
            extension.resourcefullib$setHighlight(HighlightHandler.extractState(this.level, pos, state));
        }
        return null;
    }

    @Inject(method = "renderHitOutline", at = @At("HEAD"), cancellable = true)
    public void onRenderHitOutline(PoseStack poseStack, VertexConsumer vertexConsumer, double d, double e, double f, BlockOutlineRenderState state, int i, CallbackInfo ci) {
        //noinspection ConstantValue
        if ((Object) state instanceof BlockOutlineRenderStateExtension extension) {
            var highlight = extension.resourcefullib$getHighlight();
            if (highlight != null && HighlightHandler.onBlockHighlight(new Vec3(d, e, f), poseStack, state.pos(), highlight, vertexConsumer, i)) {
                ci.cancel();
            }
        }
    }
}
