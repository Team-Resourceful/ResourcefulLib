package com.teamresourceful.resourcefullib.client.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamresourceful.resourcefullib.client.utils.CursorUtils;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
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
    public void render(@NotNull PoseStack stack, int i, int j, float f) {
        cursor = Cursor.DEFAULT;
        super.render(stack, i, j, f);
        for (GuiEventListener child : children()) {
            if (child instanceof AbstractWidget widget && widget.isHovered()) {
                if (widget.active) {
                    cursor = widget instanceof EditBox ? Cursor.TEXT : Cursor.POINTER;
                } else {
                    cursor = Cursor.DISABLED;
                }
                break;
            }
        }

        switch (cursor) {
            case DEFAULT -> CursorUtils.setDefault();
            case POINTER -> CursorUtils.setPointing();
            case DISABLED -> CursorUtils.setDisabled();
            case TEXT -> CursorUtils.setText();
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
