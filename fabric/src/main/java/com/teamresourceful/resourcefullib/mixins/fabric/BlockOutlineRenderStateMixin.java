package com.teamresourceful.resourcefullib.mixins.fabric;

import com.teamresourceful.resourcefullib.client.fabric.BlockOutlineRenderStateExtension;
import com.teamresourceful.resourcefullib.client.highlights.HighlightRenderState;
import net.minecraft.client.renderer.state.BlockOutlineRenderState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BlockOutlineRenderState.class)
public class BlockOutlineRenderStateMixin implements BlockOutlineRenderStateExtension {

    @Unique
    @Nullable
    private HighlightRenderState resourcefullib$highlight = null;

    @Override
    public @Nullable HighlightRenderState resourcefullib$getHighlight() {
        return this.resourcefullib$highlight;
    }

    @Override
    public void resourcefullib$setHighlight(@Nullable HighlightRenderState highlight) {
        this.resourcefullib$highlight = highlight;
    }
}
