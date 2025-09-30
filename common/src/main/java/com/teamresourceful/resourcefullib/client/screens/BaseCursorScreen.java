package com.teamresourceful.resourcefullib.client.screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public abstract class BaseCursorScreen extends Screen implements CursorScreen {

    protected BaseCursorScreen(Component component) {
        super(component);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float f) {
        super.render(graphics, mouseX, mouseY, f);
        if (this.getRectangle().containsPoint(mouseX, mouseY)) {
            applyCursor(graphics, children(), mouseX, mouseY);
        }
    }
}
