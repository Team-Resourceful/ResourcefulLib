package com.teamresourceful.resourcefullib.neoforge;

import com.teamresourceful.resourcefullib.ResourcefulLib;
import com.teamresourceful.resourcefullib.common.ApiProxy;
import com.teamresourceful.resourcefullib.common.event.events.CommandRegistrationEvent;
import com.teamresourceful.resourcefullib.common.network.neoforge.NeoForgeNetworking;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@Mod(ResourcefulLib.MOD_ID)
public class ResourcefulLibNeoForge {

    public ResourcefulLibNeoForge(IEventBus bus) {
        ResourcefulLib.init();
        if (FMLLoader.getDist().isClient()) {
            ResourcefulLibNeoForgeClient.init(bus);
            ApiProxy.setInstance(NeoForgeClientApiProxy.INSTANCE);
        } else {
            ApiProxy.setInstance(NeoForgeServerApiProxy.INSTANCE);
        }

        bus.addListener(ResourcefulLibNeoForge::onNetworkSetup);
        bus.addListener(ResourcefulLibNeoForge::onCommandRegister);
    }

    public static void onCommandRegister(RegisterCommandsEvent event) {
        CommandRegistrationEvent.EVENT.post(new CommandRegistrationEvent(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection()));
    }

    public static void onNetworkSetup(RegisterPayloadHandlersEvent event) {
        NeoForgeNetworking.setupNetwork(event);
    }
}
