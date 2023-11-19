package com.teamresourceful.resourcefullib.common.item.tabs.neoforge;

import com.teamresourceful.resourcefullib.common.item.tabs.ResourcefulCreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;

import java.util.function.Supplier;

public class ResourcefulCreativeModeTabImpl {
    public static CreativeModeTab create(ResourcefulCreativeModeTab tab) {
        var creativeTab = CreativeModeTab.builder()
                .icon(() -> tab.icon.get())
                .title(Component.translatable("itemGroup." + tab.id.getNamespace() + "." + tab.id.getPath()));
        if (tab.hideScrollBar) creativeTab.noScrollBar();
        if (tab.hideTitle) creativeTab.hideTitle();
        creativeTab.displayItems((params, output) -> tab.contents.stream().flatMap(Supplier::get).forEach(output::accept));
        return creativeTab.build();
    }
}