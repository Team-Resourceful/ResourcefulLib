package com.teamresourceful.resourcefullib.client.scissor;

import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.ApiStatus;

/**
 * @deprecated use {@link com.teamresourceful.resourcefullib.client.closables.CloseableScissor}
 */
@Deprecated
@ApiStatus.ScheduledForRemoval(inVersion = ">1.21.6")
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