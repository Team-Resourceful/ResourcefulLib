package com.teamresourceful.resourcefullib.common.item.tabs.fabric;

import com.teamresourceful.resourcefullib.common.item.tabs.ResourcefulCreativeTab;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;

import java.util.function.Supplier;

public class ResourcefulCreativeTabImpl {
    public static Supplier<CreativeModeTab> create(ResourcefulCreativeTab tab) {
        var group = FabricItemGroup.builder()
                .icon(() -> tab.icon.get())
                .title(Component.translatable("itemGroup." + tab.id.getNamespace() + "." + tab.id.getPath()));
        if (tab.hideScrollBar) group.noScrollBar();
        if (tab.hideTitle) group.hideTitle();
        group.displayItems((params, output) -> {
            tab.registries.forEach(registry -> registry.boundStream().forEach(output::accept));
            tab.stacks.stream().map(Supplier::get).forEach(output::accept);

            tab.contents.stream().flatMap(Supplier::get).forEach(output::accept);
        });
        CreativeModeTab tab1 = group.build();
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, tab.id, tab1);
        return () -> tab1;
    }
}
