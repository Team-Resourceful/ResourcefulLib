package com.teamresourceful.resourcefullib.common.item.tabs;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;

import java.util.function.Supplier;

class ResourcefulCreativeModeTabServiceFabricImpl implements ResourcefulCreativeModeTabService {
    @Override
    public CreativeModeTab register(ResourcefulCreativeModeTab tab) {
        var group = FabricCreativeModeTab.builder()
                .icon(() -> tab.icon.get())
                .title(Component.translatable("itemGroup." + tab.id.getNamespace() + "." + tab.id.getPath()));
        if (tab.hideScrollBar) group.noScrollBar();
        if (tab.hideTitle) group.hideTitle();
        group.displayItems((_, output) -> tab.contents.stream().flatMap(Supplier::get).forEach(output::accept));
        return group.build();
    }
}
