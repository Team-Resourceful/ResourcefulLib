package com.teamresourceful.resourcefullib.client.closables;

import net.minecraft.client.gui.GuiGraphics;

public record CloseablePose(GuiGraphics graphics) implements AutoCloseable {

    public CloseablePose {
        graphics.pose().pushMatrix();
    }

    @Override
    public void close() {
        this.graphics.pose().popMatrix();
    }
}
