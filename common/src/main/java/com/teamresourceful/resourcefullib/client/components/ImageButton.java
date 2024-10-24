package com.teamresourceful.resourcefullib.client.components;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public abstract class ImageButton extends AbstractButton {

    protected int imageWidth = 256;
    protected int imageHeight = 256;

    public ImageButton(int x, int y, int width, int height) {
        super(x, y, width, height, CommonComponents.EMPTY);
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.blit(
            RenderType::guiTextured,
            getTexture(mouseX, mouseY),
            getX(), getY(),
            getU(mouseX, mouseY), getV(mouseX, mouseY),
            this.width, this.height,
            this.imageWidth, this.imageHeight
        );
    }

    public abstract ResourceLocation getTexture(int mouseX, int mouseY);
    public abstract int getU(int mouseX, int mouseY);
    public abstract int getV(int mouseX, int mouseY);

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput output) {

    }
}
