package com.teamresourceful.resourcefullib.mixins.fabric;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamresourceful.resourcefullib.client.fabric.BlockOutlineRenderStateExtension;
import com.teamresourceful.resourcefullib.client.highlights.HighlightHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.util.ARGB;
import net.minecraft.util.CommonColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Inject(method = "submitBlockOutline", at = @At("HEAD"), cancellable = true)
    public void onRenderHitOutline(
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
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
                            submitNodeCollector,
                            RenderTypes.secondaryBlockOutline(),
                            CommonColors.BLACK,
                            7f
                    );
                }

                HighlightHandler.onBlockHighlight(
                        vec3,
                        poseStack,
                        state.pos(),
                        highlight,
                        submitNodeCollector,
                        RenderTypes.lines(),
                        state.highContrast() ? CommonColors.HIGH_CONTRAST_DIAMOND : ARGB.color(102, CommonColors.BLACK),
                        Minecraft.getInstance().gameRenderer.gameRenderState().windowRenderState.appropriateLineWidth
                );
                ci.cancel();
            }
        }
    }
}
