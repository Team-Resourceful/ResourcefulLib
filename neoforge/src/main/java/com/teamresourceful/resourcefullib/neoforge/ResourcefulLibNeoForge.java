package com.teamresourceful.resourcefullib.neoforge;

import com.teamresourceful.resourcefullib.ResourcefulLib;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mod(ResourcefulLib.MOD_ID)
public class ResourcefulLibNeoForge {

    private static final List<Consumer<IEventBus>> LISTENERS = new ArrayList<>();
    private static IEventBus bus;

    public ResourcefulLibNeoForge(IEventBus bus) {
        ResourcefulLib.init();
        if (FMLLoader.getDist().isClient()) {
            ResourcefulLibNeoForgeClient.init(bus);
        }

        ResourcefulLibNeoForge.setBus(bus);
    }

    private static void setBus(IEventBus bus) {
        ResourcefulLibNeoForge.bus = bus;
        if (bus != null) {
            LISTENERS.forEach(listener -> listener.accept(bus));
            LISTENERS.clear();
        }
    }

    public static void listen(Consumer<IEventBus> listener) {
        if (bus != null) {
            listener.accept(bus);
        } else {
            LISTENERS.add(listener);
        }
    }
}
