package com.teamresourceful.resourcefullib.neoforge;

import com.teamresourceful.resourcefullib.ResourcefulLib;
import com.teamresourceful.resourcefullib.common.network.neoforge.NeoForgeNetworking;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import org.jetbrains.annotations.ApiStatus;

@Mod(ResourcefulLib.MOD_ID)
public class ResourcefulLibNeoForge {

    public ResourcefulLibNeoForge(IEventBus bus) {
        ResourcefulLib.init();
        if (FMLLoader.getDist().isClient()) {
            ResourcefulLibNeoForgeClient.init(bus);
        }

        bus.addListener(ResourcefulLibNeoForge::onNetworkSetup);
    }

    public static void onNetworkSetup(RegisterPayloadHandlerEvent event) {
        NeoForgeNetworking.setupNetwork(event);
    }

    @ApiStatus.Internal
    @SuppressWarnings({"removal", "deprecation"})
    public static IEventBus getModEventBus() {
        net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext context = ModLoadingContext.get().extension();
        return context.getModEventBus();
    }
}
