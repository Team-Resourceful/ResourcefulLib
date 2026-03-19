package com.teamresourceful.resourcefullib.client.closables;

import net.minecraft.client.gui.GuiGraphicsExtractor;

public record CloseablePose(GuiGraphicsExtractor graphics) implements AutoCloseable {

    public CloseablePose {
        graphics.pose().pushMatrix();
    }

    @Override
    public void close() {
        this.graphics.pose().popMatrix();
    }
}
