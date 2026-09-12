package com.teamresourceful.resourcefullib.mixins.fabric;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.teamresourceful.resourcefullib.client.fabric.BlockOutlineRenderStateExtension;
import com.teamresourceful.resourcefullib.client.fluid.fabric.EntityFluidEyesHook;
import com.teamresourceful.resourcefullib.client.fluid.registry.ResourcefulClientFluidRegistry;
import com.teamresourceful.resourcefullib.client.highlights.HighlightHandler;
import com.teamresourceful.resourcefullib.common.fluid.ResourcefulFlowingFluid;
import com.teamresourceful.resourcefullib.fabric.ResourcefulLibFabricClient;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.extract.LevelExtractor;
import net.minecraft.client.renderer.state.level.BlockOutlineRenderState;
import net.minecraft.client.renderer.state.level.PlayerRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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

    @Inject(method = "extractPlayerState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isUnderWater()Z"))
    private void extractStuff(
            Camera camera,
            DeltaTracker deltaTracker,
            float worldPartialTicks,
            PlayerRenderState state,
            CallbackInfo ci,
            @Local LocalPlayer player
    ) {
        if (player instanceof EntityFluidEyesHook hook && hook.rlib$getEyesFluid() != null && hook.rlib$getEyesFluid().getType() instanceof ResourcefulFlowingFluid fluid) {
            Identifier id = fluid.getData().id();
            state.setData(ResourcefulLibFabricClient.FLUID_DATA_KEY, ResourcefulClientFluidRegistry.get(id));
        }
    }
}
