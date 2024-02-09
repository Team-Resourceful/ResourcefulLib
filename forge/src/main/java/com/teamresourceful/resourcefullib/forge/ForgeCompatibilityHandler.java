package com.teamresourceful.resourcefullib.forge;

import com.electronwill.nightconfig.core.UnmodifiableConfig;
import com.google.gson.*;
import com.teamresourceful.resourcefullib.common.comptibility.CompatabilityManager;
import com.teamresourceful.resourcefullib.common.lib.Constants;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.forgespi.language.IModInfo;

import java.util.List;
import java.util.Map;

public class ForgeCompatibilityHandler {

    private static final String KEY = "resourcefullib:compatibility";

    public static void load() {
        for (IModInfo mod : ModList.get().getMods()) {
            if (mod.getModProperties().containsKey(KEY)) {
                try {
                    initMod(mod, mod.getModProperties());
                }catch (Exception e) {
                    Constants.LOGGER.error("Resourceful Lib failed to load compatibility for mod: " + mod.getDisplayName());
                    e.printStackTrace();
                }
            }
        }
    }

    private static void initMod(IModInfo mod, Map<String, Object> metadata) {
        JsonArray array = toJsonElement(metadata.get(KEY)).getAsJsonArray();
        CompatabilityManager.check(
                mod.getModId(),
                FMLLoader.getDist().isClient(),
                array
        );
    }

    private static JsonElement toJsonElement(Object o) {
        if (o instanceof String string) {
            return new JsonPrimitive(string);
        } else if (o instanceof Number number) {
            return new JsonPrimitive(number);
        } else if (o instanceof Boolean bool) {
            return new JsonPrimitive(bool);
        } else if (o instanceof List<?> list) {
            JsonArray array = new JsonArray();
            for (Object entry : list) {
                array.add(toJsonElement(entry));
            }
            return array;
        } else if (o instanceof UnmodifiableConfig config) {
            Map<String, Object> map = config.valueMap();
            JsonObject object = new JsonObject();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                object.add(entry.getKey(), toJsonElement(entry.getValue()));
            }
            return object;
        }
        return JsonNull.INSTANCE;
    }
}
