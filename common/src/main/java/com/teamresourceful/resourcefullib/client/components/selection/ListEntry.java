package com.teamresourceful.resourcefullib.client.components.selection;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import org.jetbrains.annotations.NotNull;

public abstract class ListEntry implements GuiEventListener {

    abstract protected void render(@NotNull GuiGraphics graphics, int id, int left, int top, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTick, boolean selected);
}