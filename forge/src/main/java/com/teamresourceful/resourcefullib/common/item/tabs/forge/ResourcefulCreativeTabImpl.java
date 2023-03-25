package com.teamresourceful.resourcefullib.common.item.tabs.forge;

import com.teamresourceful.resourcefullib.common.item.tabs.ResourcefulCreativeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.function.Supplier;

public class ResourcefulCreativeTabImpl {

    public static Supplier<CreativeModeTab> create(ResourcefulCreativeTab tab) {
        final Entry group = new Entry(tab);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(group::build);
        return group;
    }

    private static class Entry implements Supplier<CreativeModeTab> {

        private final ResourcefulCreativeTab tab;
        private CreativeModeTab builtTab;

        public Entry(ResourcefulCreativeTab tab) {
            this.tab = tab;
        }

        public void build(CreativeModeTabEvent.Register event) {
            builtTab = event.registerCreativeModeTab(tab.id, builder -> {
                builder.icon(tab.icon);
                builder.title(Component.translatable("itemGroup." + tab.id.getNamespace() + "." + tab.id.getPath()));
                if (tab.hideScrollBar) builder.noScrollBar();
                if (tab.hideTitle) builder.hideTitle();
                builder.displayItems((params, output) -> {
                    tab.registries.forEach(registry -> registry.boundStream().forEach(output::accept));
                    tab.stacks.stream().map(Supplier::get).forEach(output::accept);
                });
            });
        }

        @Override
        public CreativeModeTab get() {
            return builtTab;
        }
    }
}
