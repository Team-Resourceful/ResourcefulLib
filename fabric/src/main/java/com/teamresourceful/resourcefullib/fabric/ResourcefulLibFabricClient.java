package com.teamresourceful.resourcefullib.fabric;

import com.teamresourceful.resourcefullib.client.sysinfo.SystemInfo;
import com.teamresourceful.resourcefullib.common.ApiProxy;
import com.teamresourceful.resourcefullib.common.utils.files.GlobalStorage;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;

import java.nio.file.Files;
import java.nio.file.Path;

public class ResourcefulLibFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ApiProxy.setInstance(FabricClientProxy.INSTANCE);
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new FabricHighlightHandler());
        FabricResourcePackHandler.load();

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            var command = ClientCommandManager.literal("rlib");
            command.then(ClientCommandManager.literal("info")
                    .executes(context -> {
                        String info = SystemInfo.buildForDiscord();
                        Minecraft.getInstance().keyboardHandler.setClipboard(info);
                        context.getSource().sendFeedback(Component.literal("[Resourceful Lib]: Info copied to clipboard!"));
                        return 1;
                    })
            );
            if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
                command.then(ClientCommandManager.literal("teststorage")
                        .executes(context -> {
                            try {
                                Path file = GlobalStorage.getCacheDirectory("test").resolve("test.txt");
                                Files.deleteIfExists(file);
                                Files.createDirectories(file.getParent());
                                Files.write(file, "This is a test file".getBytes());
                            }catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            return 1;
                        })
                );
            }

            dispatcher.register(command);
        });
    }
}
