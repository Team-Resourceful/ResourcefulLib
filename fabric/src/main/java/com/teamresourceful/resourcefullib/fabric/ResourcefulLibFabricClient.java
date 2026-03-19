package com.teamresourceful.resourcefullib.fabric;

import com.teamresourceful.resourcefullib.ResourcefulLib;
import com.teamresourceful.resourcefullib.client.highlights.HighlightHandler;
import com.teamresourceful.resourcefullib.client.sysinfo.SystemInfo;
import com.teamresourceful.resourcefullib.common.ApiProxy;
import com.teamresourceful.resourcefullib.common.utils.files.GlobalStorage;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;

import java.nio.file.Files;
import java.nio.file.Path;

public class ResourcefulLibFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ApiProxy.setInstance(FabricClientProxy.INSTANCE);
        ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloadListener(
                Identifier.fromNamespaceAndPath(ResourcefulLib.MOD_ID, "highlights"),
                new HighlightHandler()
        );
        FabricResourcePackHandler.load();

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            var command = ClientCommands.literal("rlib");
            command.then(ClientCommands.literal("info")
                    .executes(context -> {
                        String info = SystemInfo.buildForDiscord();
                        Minecraft.getInstance().keyboardHandler.setClipboard(info);
                        context.getSource().sendFeedback(Component.literal("[Resourceful Lib]: Info copied to clipboard!"));
                        return 1;
                    })
            );
            if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
                command.then(ClientCommands.literal("teststorage")
                        .executes(_ -> {
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
