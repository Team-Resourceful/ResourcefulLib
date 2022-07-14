package com.teamresourceful.resourcefullib.client.scissor;

import com.mojang.blaze3d.systems.RenderSystem;

@SuppressWarnings("UnusedReturnValue")
public record ScissorBox(int x, int y, int width, int height) {

    public ScissorBox start() {
        RenderSystem.enableScissor(x, y, width, height);
        return this;
    }

    public ScissorBox end() {
        RenderSystem.disableScissor();
        return this;
    }

}
