package com.teamresourceful.resourcefullib.mixins.fabric;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.teamresourceful.resourcefullib.client.fabric.BlockOutlineRenderStateExtension;
import com.teamresourceful.resourcefullib.client.highlights.HighlightHandler;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.state.BlockOutlineRenderState;
import net.minecraft.client.renderer.state.LevelRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ARGB;
import net.minecraft.util.CommonColors;
import net.minecraft.world.level.block.state.BlockState;
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
                    value = "NEW",
                    target = "(Lnet/minecraft/core/BlockPos;ZZLnet/minecraft/world/phys/shapes/VoxelShape;)Lnet/minecraft/client/renderer/state/BlockOutlineRenderState;"
            )
    )
    private BlockOutlineRenderState resourcefullib$extractBlockOutline(
            BlockPos pos,
            boolean b1,
            boolean b2,
            VoxelShape shape,
            Operation<BlockOutlineRenderState> original,
            @Local(ordinal = 0) BlockState state
    ) {
        var renderState = original.call(pos, b1, b2, shape);
        //noinspection ConstantValue
        if ((Object) renderState instanceof BlockOutlineRenderStateExtension extension) {
            extension.resourcefullib$setHighlight(HighlightHandler.extractState(this.level, pos, state));
        }
        return renderState;
    }

    @Inject(method = "renderBlockOutline", at = @At("HEAD"), cancellable = true)
    public void onRenderHitOutline(
            MultiBufferSource.BufferSource bufferSource,
            PoseStack poseStack,
            boolean bl,
            LevelRenderState levelRenderState,
            CallbackInfo ci
    ) {
        var state = levelRenderState.blockOutlineRenderState;
        //noinspection ConstantValue
        if ((Object) state instanceof BlockOutlineRenderStateExtension extension) {
            var vec3 = levelRenderState.cameraRenderState.pos;
            var highlight = extension.resourcefullib$getHighlight();
            if (HighlightHandler.canRender(highlight)) {
                if (state.highContrast()) {
                    HighlightHandler.onBlockHighlight(
                            vec3,
                            poseStack,
                            state.pos(),
                            highlight,
                            bufferSource.getBuffer(RenderType.secondaryBlockOutline()),
                            CommonColors.BLACK
                    );
                }

                HighlightHandler.onBlockHighlight(
                        vec3,
                        poseStack,
                        state.pos(),
                        highlight,
                        bufferSource.getBuffer(RenderType.lines()),
                        state.highContrast() ? CommonColors.HIGH_CONTRAST_DIAMOND : ARGB.color(102, CommonColors.BLACK)
                );
                ci.cancel();
            }
        }
    }
}
