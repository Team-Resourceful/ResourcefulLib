package com.teamresourceful.resourcefullib.fabric;

import com.teamresourceful.resourcefullib.common.lib.Constants;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.CustomValue;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.Optionull;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

public final class FabricResourcePackHandler {

    private static final String RESOURCE_PACK_KEY = "resourcefullib:resourcepack";

    public static void load() {
        for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
            ModMetadata metadata = mod.getMetadata();
            if (metadata.containsCustomValue(RESOURCE_PACK_KEY)) {
                try {
                    initMod(mod, metadata);
                } catch (Exception e) {
                    Constants.LOGGER.error("Resourceful Lib failed to load resource pack for mod: {}", metadata.getName(), e);
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
            ResourceLoader.registerBuiltinPack(
                    Identifier.fromNamespaceAndPath(container.getMetadata().getId(), name),
                    container,
                    createDescription(null, name),
                    PackActivationType.NORMAL
            );
        } catch (Exception ignored) {
            var object = value.getAsObject();
            String name = object.get("name").getAsString();
            Identifier id = Identifier.fromNamespaceAndPath(container.getMetadata().getId(), name);
            Component description = createDescription(Optionull.map(object.get("description"), CustomValue::getAsString), name);
            var type = Optionull.mapOrDefault(object.get("required"), CustomValue::getAsBoolean, false) ? PackActivationType.ALWAYS_ENABLED : PackActivationType.NORMAL;
            ResourceLoader.registerBuiltinPack(id, container, description, type);
        }
    }

    private static Component createDescription(@Nullable String description, String name) {
        if (description != null) {
            return Component.literal(description);
        }
        return Component.translatableWithFallback("resourcefullib.resourcepack." + name, name);
    }
}
