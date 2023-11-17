package com.teamresourceful.resourcefullib.common.item.tabs.fabric;

import com.teamresourceful.resourcefullib.common.item.tabs.ResourcefulCreativeModeTab;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;

import java.util.function.Supplier;

public class ResourcefulCreativeModeTabImpl {
    public static CreativeModeTab create(ResourcefulCreativeModeTab tab) {
        var group = FabricItemGroup.builder()
                .icon(() -> tab.icon.get())
                .title(Component.translatable("itemGroup." + tab.id.getNamespace() + "." + tab.id.getPath()));
        if (tab.hideScrollBar) group.noScrollBar();
        if (tab.hideTitle) group.hideTitle();
        group.displayItems((params, output) -> tab.contents.stream().flatMap(Supplier::get).forEach(output::accept));
        return group.build();
    }
}
