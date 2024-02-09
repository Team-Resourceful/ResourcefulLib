package com.teamresourceful.resourcefullib.common.comptibility;

import com.google.gson.JsonArray;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.teamresourceful.resourcefullib.common.comptibility.options.CompatabilityOption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompatabilityManager {

    private static final Map<String, List<CompatabilityOption<?>>> ISSUES = new HashMap<>();

    private static final Codec<List<CompatabilityOption<?>>> OPTIONS_CODEC = CompatabilityOptions.CODEC.listOf();
    private static boolean initialized = false;
    private static boolean isClient = false;

    private static void init() {
        if (!initialized) {
            CompatabilityOptions.init();
            initialized = true;
        }
    }

    public static void check(String modId, boolean isClient, JsonArray json) {
        CompatabilityManager.isClient = isClient;
        init();
        List<CompatabilityOption<?>> options = new ArrayList<>();
        OPTIONS_CODEC.parse(JsonOps.INSTANCE, json).result().ifPresent(options::addAll);
        options.removeIf(CompatabilityOption::matches);

        if (options.isEmpty()) return;
        ISSUES.put(modId, options);
    }

    public static Map<String, List<CompatabilityOption<?>>> issues() {
        return ISSUES;
    }

    public static boolean isClient() {
        return isClient;
    }
}
