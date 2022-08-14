package com.teamresourceful.resourcefullib.client.screens;

import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface TooltipProvider {

    static <T> List<Component> getTooltips(Collection<T> items, int mouseX, int mouseY) {
        List<Component> tooltips = new ArrayList<>();
        for (T item : items) {
            if (item instanceof TooltipProvider provider) {
                tooltips.addAll(provider.getTooltip(mouseX, mouseY));
            }
        }
        return tooltips;
    }

    @NotNull
    List<Component> getTooltip(int mouseX, int mouseY);
}
