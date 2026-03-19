package com.teamresourceful.resourcefullib.common.lib;

import com.mojang.logging.LogUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public enum Platform {
    FABRIC,
    NEOFORGE,
    UNKNOWN;

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Platform PLATFORM;
    static {
        if (isLoaded("net.neoforged.fml.loading.FMLLoader")) {
            PLATFORM = NEOFORGE;
        } else if (isLoaded("net.fabricmc.loader.api.FabricLoader")) {
            PLATFORM = FABRIC;
        } else {
            PLATFORM = UNKNOWN;
        }
    }

    public static Platform get() {
        return PLATFORM;
    }

    private static boolean isLoaded(@NotNull String className) {
        try {
            Class.forName(className, false, Platform.class.getClassLoader());
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
