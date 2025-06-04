package com.teamresourceful.resourcefullib.client.closables;

import net.minecraft.client.gui.GuiGraphics;

public record CloseableScissor(GuiGraphics graphics) implements AutoCloseable {

    public CloseableScissor(GuiGraphics graphics, int x, int y, int width, int height) {
        this(graphics);
        graphics.enableScissor(x, y, width, height);
    }

    @Override
    public void close() {
        graphics.disableScissor();
    }
}