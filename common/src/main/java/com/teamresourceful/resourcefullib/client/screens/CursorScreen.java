package com.teamresourceful.resourcefullib.client.screens;

import com.teamresourceful.resourcefullib.client.components.CursorWidget;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;

import java.util.List;

public interface CursorScreen {

    void setCursor(Cursor cursor);


    default void setCursor(List<? extends GuiEventListener> listeners, double mouseX, double mouseY) {
        for (GuiEventListener child : listeners) {
            boolean hovered = child.isMouseOver(mouseX, mouseY);
            if (child instanceof CursorWidget widget && hovered) {
                setCursor(widget.getCursor());
                break;
            } else if (child instanceof AbstractWidget widget && hovered && widget.visible) {
                if (widget.active) {
                    setCursor(widget instanceof EditBox || widget instanceof MultiLineEditBox ? Cursor.TEXT : Cursor.POINTER);
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
