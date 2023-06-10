package com.teamresourceful.resourcefullib.fabric;

import com.teamresourceful.resourcefullib.common.lib.Constants;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.CustomValue;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public final class FabricResourcePackHandler {

    private static final String RESOURCE_PACK_KEY = "resourcefullib:resourcepack";

    public static void load() {
        for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
            ModMetadata metadata = mod.getMetadata();
            if (metadata.containsCustomValue(RESOURCE_PACK_KEY)) {
                try {
                    initMod(mod, metadata);
                }catch (Exception e) {
                    Constants.LOGGER.error("Resourceful Lib failed to load resource pack for mod: " + metadata.getName());
                    e.printStackTrace();
                }
            }
        }
    }

    private static void initMod(ModContainer mod, ModMetadata metadata) {
        var packs = metadata.getCustomValue(RESOURCE_PACK_KEY).getAsArray();
        for (CustomValue pack : packs) {
            loadPack(mod, pack);
        }
    }

    private static void loadPack(ModContainer container, CustomValue value) {
        try {
            String name = value.getAsString();
            ResourceManagerHelper.registerBuiltinResourcePack(
                new ResourceLocation(name),
                container,
                Component.translatable("resourcefullib.resourcepack." + name),
                ResourcePackActivationType.NORMAL
            );
        } catch (Exception ignored) {
            var object = value.getAsObject();
            ResourceLocation id = new ResourceLocation(object.get("name").getAsString());
            String description = object.get("description").getAsString();
            ResourceManagerHelper.registerBuiltinResourcePack(id, container, Component.translatable(description), ResourcePackActivationType.NORMAL);
        }
    }
}
