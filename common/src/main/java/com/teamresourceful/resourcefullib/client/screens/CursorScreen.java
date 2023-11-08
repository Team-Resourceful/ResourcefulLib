package com.teamresourceful.resourcefullib.client.screens;

import com.teamresourceful.resourcefullib.client.components.CursorWidget;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

public interface CursorScreen {

    void setCursor(Cursor cursor);


    default void setCursor(List<? extends GuiEventListener> listeners, double mouseX, double mouseY) {
        setCursor(listeners);
        for (GuiEventListener child : listeners) {
            if (child instanceof CursorWidget widget && child.isMouseOver(mouseX, mouseY) && widget.getCursor() != Cursor.DEFAULT) {
                setCursor(widget.getCursor());
                break;
            }
        }
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "1.21")
    default void setCursor(List<? extends GuiEventListener> listeners) {
        for (GuiEventListener child : listeners) {
            if (child instanceof CursorWidget) continue;
            if (child instanceof AbstractWidget widget && widget.isHovered() && widget.visible) {
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
