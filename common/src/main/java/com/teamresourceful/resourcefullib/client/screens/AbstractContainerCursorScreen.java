package com.teamresourceful.resourcefullib.client.screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractContainerCursorScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> implements CursorScreen {

    public AbstractContainerCursorScreen(T abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float f) {
        super.render(graphics, mouseX, mouseY, f);
        if (this.getRectangle().containsPoint(mouseX, mouseY)) {
            applyCursor(graphics, children(), mouseX, mouseY);
        }
    }
}
