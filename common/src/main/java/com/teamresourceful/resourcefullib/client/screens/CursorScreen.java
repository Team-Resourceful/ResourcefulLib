package com.teamresourceful.resourcefullib.client.screens;

import com.mojang.blaze3d.platform.cursor.CursorType;
import com.mojang.blaze3d.platform.cursor.CursorTypes;
import com.teamresourceful.resourcefullib.client.components.CursorWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;

import java.util.List;

public interface CursorScreen {

    default void applyCursor(GuiGraphics graphics, List<? extends GuiEventListener> listeners, double mouseX, double mouseY) {
        for (GuiEventListener child : listeners) {
            if (child instanceof CursorWidget widget && child.isMouseOver(mouseX, mouseY)) {
                widget.getCursor().apply(graphics);
                break;
            }
        }
    }

    enum Cursor {
        DEFAULT(CursorType.DEFAULT),
        POINTER(CursorTypes.POINTING_HAND),
        DISABLED(CursorTypes.NOT_ALLOWED),
        TEXT(CursorTypes.IBEAM),
        CROSSHAIR(CursorTypes.CROSSHAIR),
        RESIZE_EW(CursorTypes.RESIZE_EW),
        RESIZE_NS(CursorTypes.RESIZE_NS),
        RESIZE_ALL(CursorTypes.RESIZE_ALL),
        ;

        private final CursorType type;

        Cursor(CursorType type) {
            this.type = type;
        }

        public void apply(GuiGraphics graphics) {
            graphics.requestCursor(this.type);
        }
    }
}
