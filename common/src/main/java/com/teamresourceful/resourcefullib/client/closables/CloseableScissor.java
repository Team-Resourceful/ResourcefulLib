package com.teamresourceful.resourcefullib.client.closables;

import net.minecraft.client.gui.GuiGraphicsExtractor;

public record CloseableScissor(GuiGraphicsExtractor graphics) implements AutoCloseable {

    public CloseableScissor(GuiGraphicsExtractor graphics, int x, int y, int width, int height) {
        this(graphics);
        graphics.enableScissor(x, y, width, height);
    }

    @Override
    public void close() {
        graphics.disableScissor();
    }
}