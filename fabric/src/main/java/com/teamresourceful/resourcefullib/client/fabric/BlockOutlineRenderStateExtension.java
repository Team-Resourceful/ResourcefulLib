package com.teamresourceful.resourcefullib.client.fabric;

import com.teamresourceful.resourcefullib.client.highlights.HighlightRenderState;
import org.jetbrains.annotations.Nullable;

public interface BlockOutlineRenderStateExtension {

    @Nullable HighlightRenderState resourcefullib$getHighlight();

    void resourcefullib$setHighlight(@Nullable HighlightRenderState state);
}
