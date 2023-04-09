package com.teamresourceful.resourcefullib.client.screens;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;

import java.util.List;

public interface CursorScreen {

    void setCursor(Cursor cursor);

    default void setCursor(List<? extends GuiEventListener> listeners) {
        for (GuiEventListener child : listeners) {
            if (child instanceof AbstractWidget widget && widget.isHovered()) {
                if (widget.active) {
                    setCursor(widget instanceof EditBox ? Cursor.TEXT : Cursor.POINTER);
                } else {
                    setCursor(Cursor.DISABLED);
                }
                break;
            }
        }
    }

    enum Cursor {
        DEFAULT,
        POINTER,
        DISABLED,
        TEXT,
        CROSSHAIR,
        RESIZE_EW,
        RESIZE_NS,
        RESIZE_NWSE,
        RESIZE_NESW,
        RESIZE_ALL
    }
}
