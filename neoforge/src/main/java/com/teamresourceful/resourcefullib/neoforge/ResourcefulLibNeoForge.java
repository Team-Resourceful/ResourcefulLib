package com.teamresourceful.resourcefullib.neoforge;

import com.teamresourceful.resourcefullib.ResourcefulLib;
import com.teamresourceful.resourcefullib.common.network.neoforge.NeoForgeNetworking;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@Mod(ResourcefulLib.MOD_ID)
public class ResourcefulLibNeoForge {

    public ResourcefulLibNeoForge(IEventBus bus) {
        ResourcefulLib.init();
        if (FMLLoader.getDist().isClient()) {
            ResourcefulLibNeoForgeClient.init(bus);
        }

        bus.addListener(ResourcefulLibNeoForge::onNetworkSetup);
    }

    public static void onNetworkSetup(RegisterPayloadHandlersEvent event) {
        NeoForgeNetworking.setupNetwork(event);
    }
}
