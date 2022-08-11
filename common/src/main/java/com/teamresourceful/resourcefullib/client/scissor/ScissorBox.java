package com.teamresourceful.resourcefullib.client.scissor;

import com.mojang.blaze3d.systems.RenderSystem;

@SuppressWarnings("UnusedReturnValue")
public record ScissorBox(int x, int y, int width, int height) {

    public ScissorBox subBox(int x, int y, int width, int height) {
        int startY = Math.max(this.y(), y);
        int endY = Math.min(this.y() + this.height(), y + height);
        int startX = Math.max(this.x(), x);
        int endX = Math.min(this.x() + this.width(), x + width);

        startY = Math.min(startY, endY);
        startX = Math.min(startX, endX);

        return new ScissorBox(startX, startY, endX - startX, endY - startY);
    }

    public ScissorBox start() {
        RenderSystem.enableScissor(x, y, width, height);
        return this;
    }

    public ScissorBox end() {
        RenderSystem.disableScissor();
        return this;
    }

}
