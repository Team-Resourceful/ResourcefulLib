package com.teamresourceful.resourcefullib.client.scissor;

import com.mojang.blaze3d.systems.RenderSystem;

import java.util.Objects;

@SuppressWarnings("UnusedReturnValue")
public class ScissorBox {
    private final int x, y, width, height;

    public ScissorBox(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public ScissorBox start() {
        RenderSystem.enableScissor(x, y, width, height);
        return this;
    }

    public ScissorBox end() {
        RenderSystem.disableScissor();
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ScissorBox) obj;
        return this.x == that.x && this.y == that.y && this.width == that.width && this.height == that.height;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, width, height);
    }

}
