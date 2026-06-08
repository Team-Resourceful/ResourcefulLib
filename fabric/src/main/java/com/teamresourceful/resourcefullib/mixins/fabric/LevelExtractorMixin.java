package com.teamresourceful.resourcefullib.mixins.fabric;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.teamresourceful.resourcefullib.client.fabric.BlockOutlineRenderStateExtension;
import com.teamresourceful.resourcefullib.client.highlights.HighlightHandler;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.extract.LevelExtractor;
import net.minecraft.client.renderer.state.level.BlockOutlineRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LevelExtractor.class)
public class LevelExtractorMixin {

    @Shadow private @Nullable ClientLevel level;

    @WrapOperation(
            method = "extractBlockOutline",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/core/BlockPos;ZZLnet/minecraft/world/phys/shapes/VoxelShape;)Lnet/minecraft/client/renderer/state/level/BlockOutlineRenderState;"
            )
    )
    private BlockOutlineRenderState resourcefullib$extractBlockOutline(
            BlockPos pos,
            boolean isTranslucent,
            boolean highContrast,
            VoxelShape shape,
            Operation<BlockOutlineRenderState> original,
            @Local(ordinal = 0) BlockState state
    ) {
        var renderState = original.call(pos, isTranslucent, highContrast, shape);
        //noinspection ConstantValue
        if ((Object) renderState instanceof BlockOutlineRenderStateExtension extension) {
            extension.resourcefullib$setHighlight(HighlightHandler.extractState(this.level, pos, state));
        }
        return renderState;
    }

}
