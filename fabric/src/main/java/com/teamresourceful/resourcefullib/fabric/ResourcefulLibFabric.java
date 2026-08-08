package com.teamresourceful.resourcefullib.fabric;

import com.teamresourceful.resourcefullib.ResourcefulLib;
import com.teamresourceful.resourcefullib.common.event.events.CommandRegistrationEvent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class ResourcefulLibFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ResourcefulLib.init();

        CommandRegistrationCallback.EVENT.register((dispatcher, context, selection) -> CommandRegistrationEvent.EVENT.post(new CommandRegistrationEvent(dispatcher, context, selection)));
    }
}
