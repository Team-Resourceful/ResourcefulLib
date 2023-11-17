package com.teamresourceful.resourcefullib.fabric;

import com.teamresourceful.resourcefullib.client.sysinfo.SystemInfo;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;

public class ResourcefulLibFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new FabricHighlightHandler());
        FabricResourcePackHandler.load();

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, dedicated) ->
                dispatcher.register(ClientCommandManager.literal("rlib")
                        .then(ClientCommandManager.literal("info")
                                .executes(context -> {
                                    String info = SystemInfo.buildForDiscord();
                                    Minecraft.getInstance().keyboardHandler.setClipboard(info);
                                    context.getSource().sendFeedback(Component.literal("[Resourceful Lib]: Info copied to clipboard!"));
                                    return 1;
                                })
                        )
                )
        );
    }
}
