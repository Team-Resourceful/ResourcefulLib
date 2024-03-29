package com.teamresourceful.resourcefullib.client.components.context;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class ContextMenu extends AbstractWidget {

    private final List<Pair<Component, Runnable>> options = new ArrayList<>();

    public ContextMenu() {
        super(0, 0, 0, 0, CommonComponents.EMPTY);
        this.visible = false;
        this.active = false;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.fillGradient(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, -1072689136, -804253680);
        int y = this.getY() + 5;
        for (Pair<Component, Runnable> entry : this.options) {
            if (entry == null) {
                graphics.fill(this.getX() + 5, y + 3, this.getX() + width - 5, y + 4, 0x80808080);
                y += 7;
                continue;
            }
            int x = this.getX() + 5;
            if (mouseX >= x && mouseX <= x + this.width && mouseY >= y && mouseY <= y + Minecraft.getInstance().font.lineHeight) {
                graphics.fill(x - 5, y, x + this.width - 5, y + Minecraft.getInstance().font.lineHeight + 4, 0x80808080);
            }
            graphics.drawString(Minecraft.getInstance().font, entry.getFirst(), x, y + 2, 0xFFFFFFFF);
            y += Minecraft.getInstance().font.lineHeight + 4;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            int y = this.getY() + 5;
            for (var entry : this.options) {
                if (entry == null) {
                    y += 7;
                    continue;
                }
                int x = this.getX() + 5;
                int width = Minecraft.getInstance().font.width(entry.getFirst());
                if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + Minecraft.getInstance().font.lineHeight + 4) {
                    entry.getSecond().run();
                    this.visible = false;
                    return true;
                }
                y += Minecraft.getInstance().font.lineHeight + 4;
            }
            close();
        }
        return false;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return isActive();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    public ContextMenu start(double x, double y) {
        this.options.clear();
        this.setX((int) x);
        this.setY((int) y);
        return this;
    }

    public ContextMenu addOption(Component component, Runnable runnable) {
        this.options.add(Pair.of(component, runnable));
        return this;
    }

    public ContextMenu addDivider() {
        this.options.add(null);
        return this;
    }

    public ContextMenu open() {
        int longest = 0;
        this.height = 10;
        for (var entry : this.options) {
            if (entry == null) {
                this.height += 7;
                continue;
            }
            int width = Minecraft.getInstance().font.width(entry.getFirst());
            if (width > longest) {
                longest = width;
            }
            this.height += Minecraft.getInstance().font.lineHeight + 4;
        }
        this.width = longest + 10;
        this.visible = true;
        this.active = true;
        return this;
    }

    public ContextMenu close() {
        this.visible = false;
        this.active = false;
        return this;
    }
}