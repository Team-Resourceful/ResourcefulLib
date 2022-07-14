package com.teamresourceful.resourcefullib.client.scissor;

import com.mojang.blaze3d.systems.RenderSystem;

public record ClosingScissorBox(int x, int y, int width, int height) implements AutoCloseable {

    public ClosingScissorBox {
        RenderSystem.enableScissor(x, y, width, height);
    }

    @Override
    public void close() {
        RenderSystem.disableScissor();
    }
}
