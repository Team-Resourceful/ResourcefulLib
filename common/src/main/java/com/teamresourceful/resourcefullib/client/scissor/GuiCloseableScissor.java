package com.teamresourceful.resourcefullib.client.scissor;

import net.minecraft.client.gui.GuiGraphics;

public record GuiCloseableScissor(GuiGraphics graphics) implements AutoCloseable {

    public GuiCloseableScissor(GuiGraphics graphics, int x, int y, int width, int height) {
        this(graphics);
        graphics.enableScissor(x, y, width, height);
    }

    @Override
    public void close() {
        graphics.disableScissor();
    }
}