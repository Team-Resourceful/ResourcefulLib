package com.teamresourceful.resourcefullib.fabric;

import com.google.gson.*;
import com.teamresourceful.resourcefullib.common.comptibility.CompatabilityManager;
import com.teamresourceful.resourcefullib.common.lib.Constants;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.CustomValue;
import net.fabricmc.loader.api.metadata.ModMetadata;

public class FabricCompatibilityHandler {

    private static final String KEY = "resourcefullib:compatibility";

    public static void load() {
        for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
            ModMetadata metadata = mod.getMetadata();
            if (metadata.containsCustomValue(KEY)) {
                try {
                    initMod(metadata);
                }catch (Exception e) {
                    Constants.LOGGER.error("Resourceful Lib failed to load compatibility for mod: " + metadata.getName());
                    e.printStackTrace();
                }
            }
        }
    }

    private static void initMod(ModMetadata metadata) {
        CustomValue.CvArray array = metadata.getCustomValue(KEY).getAsArray();
        CompatabilityManager.check(
                metadata.getId(),
                FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT,
                toJsonArray(array)
        );
    }

    private static JsonArray toJsonArray(CustomValue.CvArray array) {
        JsonArray jsonArray = new JsonArray();
        for (CustomValue value : array) {
            jsonArray.add(toJsonElement(value));
        }
        return jsonArray;
    }

    private static JsonElement toJsonElement(CustomValue value) {
        return switch (value.getType()) {
            case NULL -> JsonNull.INSTANCE;
            case BOOLEAN -> new JsonPrimitive(value.getAsBoolean());
            case NUMBER -> new JsonPrimitive(value.getAsNumber());
            case STRING -> new JsonPrimitive(value.getAsString());
            case ARRAY -> toJsonArray(value.getAsArray());
            case OBJECT -> {
                var object = value.getAsObject();
                var jsonObject = new JsonObject();
                for (var entry : object) {
                    jsonObject.add(entry.getKey(), toJsonElement(entry.getValue()));
                }
                yield jsonObject;
            }
        };
    }
}
