package com.teamresourceful.resourcefullib.common.item.tabs.neoforge;

import com.teamresourceful.resourcefullib.common.item.tabs.ResourcefulCreativeTab;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryObject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class ResourcefulCreativeTabImpl {

    private static final Map<String, DeferredRegister<CreativeModeTab>> CREATIVE_TABS = new ConcurrentHashMap<>();

    public static Supplier<CreativeModeTab> create(ResourcefulCreativeTab tab) {
        return Entry.of(tab);
    }

    private static RegistryObject<CreativeModeTab> register(ResourceLocation id, Supplier<CreativeModeTab> tab) {
        var register = CREATIVE_TABS.computeIfAbsent(id.getNamespace(), namespace -> {
            var registry = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, namespace);
            registry.register(FMLJavaModLoadingContext.get().getModEventBus());
            return registry;
        });
        return register.register(id.getPath(), tab);
    }

    private record Entry(RegistryObject<CreativeModeTab> builtTab) implements Supplier<CreativeModeTab> {

        public static Entry of(ResourcefulCreativeTab tab) {
            var creativeTab = CreativeModeTab.builder()
                .icon(() -> tab.icon.get())
                .title(Component.translatable("itemGroup." + tab.id.getNamespace() + "." + tab.id.getPath()));
            if (tab.hideScrollBar) creativeTab.noScrollBar();
            if (tab.hideTitle) creativeTab.hideTitle();
            creativeTab.displayItems((params, output) -> {
                tab.registries.forEach(registry -> registry.boundStream().forEach(output::accept));
                tab.stacks.stream().map(Supplier::get).forEach(output::accept);

                tab.contents.stream().flatMap(Supplier::get).forEach(output::accept);
            });
            return new Entry(register(tab.id, creativeTab::build));
        }

        @Override
        public CreativeModeTab get() {
            return builtTab.get();
        }
    }
}
