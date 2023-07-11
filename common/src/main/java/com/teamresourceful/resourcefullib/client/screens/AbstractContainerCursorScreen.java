package com.teamresourceful.resourcefullib.client.screens;

import com.teamresourceful.resourcefullib.client.utils.CursorUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractContainerCursorScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> implements CursorScreen {

    private Cursor cursor = Cursor.DEFAULT;

    public AbstractContainerCursorScreen(T abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float f) {
        setCursor(Cursor.DEFAULT);
        super.render(graphics, mouseX, mouseY, f);
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
