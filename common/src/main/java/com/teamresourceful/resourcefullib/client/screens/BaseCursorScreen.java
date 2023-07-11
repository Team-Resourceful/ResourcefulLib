package com.teamresourceful.resourcefullib.client.screens;

import com.teamresourceful.resourcefullib.client.utils.CursorUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public abstract class BaseCursorScreen extends Screen implements CursorScreen {

    private Cursor cursor = Cursor.DEFAULT;

    protected BaseCursorScreen(Component component) {
        super(component);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float f) {
        setCursor(Cursor.DEFAULT);
        actuallyRender(graphics, mouseX, mouseY, f);
        setCursor(children(), mouseX, mouseY);

        switch (cursor) {
            case DEFAULT -> CursorUtils.setDefault();
            case POINTER -> CursorUtils.setPointing();
            case DISABLED -> CursorUtils.setDisabled();
            case TEXT -> CursorUtils.setText();
            case CROSSHAIR -> CursorUtils.setCrosshair();
            case RESIZE_EW -> CursorUtils.setResizeEastWest();
            case RESIZE_NS -> CursorUtils.setResizeNorthSouth();
            case RESIZE_NESW -> CursorUtils.setResizeNorthEastSouthWest();
            case RESIZE_NWSE -> CursorUtils.setResizeNorthWestSouthEast();
            case RESIZE_ALL -> CursorUtils.setResizeAll();
        }
    }

    public void actuallyRender(@NotNull GuiGraphics graphics, int i, int j, float f) {
        super.render(graphics, i, j, f);
    }

    @Override
    public void removed() {
        super.removed();
        CursorUtils.setDefault();
    }

    @Override
    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }
}
