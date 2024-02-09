package com.teamresourceful.resourcefullib.client.compatibility;

import com.teamresourceful.resourcefullib.client.components.selection.ListEntry;
import com.teamresourceful.resourcefullib.client.scissor.ScissorBoxStack;
import com.teamresourceful.resourcefullib.client.screens.CursorScreen;
import com.teamresourceful.resourcefullib.client.utils.CursorUtils;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class ComptabilityEntry extends ListEntry {

    private final boolean indent;
    private final Component text;
    private final String url;

    private int width;

    public ComptabilityEntry(boolean indent, Component text, String url) {
        this.indent = indent;
        this.text = text;
        this.url = url;
    }

    public ComptabilityEntry(Component text) {
        this(false, text, "");
    }

    public ComptabilityEntry() {
        this(false, CommonComponents.EMPTY, "");
    }


    @Override
    protected void render(@NotNull GuiGraphics graphics, @NotNull ScissorBoxStack stack, int id, int left, int top, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTick, boolean selected) {
        this.width = width;
        left += indent ? 20 : 10;
        Font font = Minecraft.getInstance().font;
        graphics.drawString(font, text, left, top + 3, 0xFFFFFF);

        boolean hoveredOver = mouseX >= left && mouseX <= left + width && mouseY >= top && mouseY <= top + height;
        CursorUtils.setCursor(hoveredOver && !url.isBlank(), CursorScreen.Cursor.POINTER);
    }

    @Override
    public boolean mouseClicked(double x, double y, int i) {
        if (url.isBlank()) return false;
        if (x < 0 || x > width) return false;
        if (y < 0 || y > 16) return false;
        Util.getPlatform().openUri(url);
        return true;
    }

    @Override
    public void setFocused(boolean bl) {

    }

    @Override
    public boolean isFocused() {
        return false;
    }
}
