package com.teamresourceful.resourcefullib.client.components;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class ParentWidget extends AbstractContainerEventHandler implements Renderable, NarratableEntry {

    public final List<Renderable> renderables = Lists.newArrayList();
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

    protected <T extends GuiEventListener & Renderable> T addRenderableWidget(T widget) {
        this.renderables.add(widget);
        this.children.add(widget);
        return widget;
    }

    @Override
    public void render(@NotNull PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        for (Renderable renderable : renderables) {
            renderable.render(stack, mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public @NotNull NarrationPriority narrationPriority() {
        return NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(@NotNull NarrationElementOutput output) {

    }
}
