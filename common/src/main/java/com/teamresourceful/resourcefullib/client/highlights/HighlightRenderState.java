package com.teamresourceful.resourcefullib.client.highlights;

import com.teamresourceful.resourcefullib.client.highlights.base.Highlight;
import net.minecraft.world.phys.Vec3;

public sealed interface HighlightRenderState {

    Vec3 offset();

    record Dynamic(Highlight highlight, Vec3 offset) implements HighlightRenderState {}
    record Cached(float[] data, Vec3 offset) implements HighlightRenderState {}
}
