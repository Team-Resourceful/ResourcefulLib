package com.teamresourceful.resourcefullib.client.components;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.teamresourceful.resourcefullib.client.screens.TooltipProvider;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class ParentWidget extends AbstractContainerEventHandler implements Widget, NarratableEntry, TooltipProvider {

    public final List<Widget> renderables = Lists.newArrayList();
    protected final List<GuiEventListener> children = Lists.newArrayList();

    protected final int x, y;

    public ParentWidget(int x, int y) {
        this.x = x;
        this.y = y;
    }

    abstract protected void init();

    @Override
    public @NotNull List<? extends GuiEventListener> children() {
        return children;
    }

    protected <T extends GuiEventListener & Widget> T addRenderableWidget(T widget) {
        this.renderables.add(widget);
        this.children.add(widget);
        return widget;
    }

    @Override
    public void render(@NotNull PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        for (Widget widget : renderables) {
            widget.render(stack, mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public @NotNull NarrationPriority narrationPriority() {
        return NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(@NotNull NarrationElementOutput output) {

    }

    @Override
    public @NotNull List<Component> getTooltip(int mouseX, int mouseY) {
        return TooltipProvider.getTooltips(this.renderables, mouseX, mouseY);
    }
}
