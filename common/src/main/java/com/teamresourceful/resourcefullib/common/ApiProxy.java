package com.teamresourceful.resourcefullib.common;

import net.minecraft.core.RegistryAccess;
import org.jetbrains.annotations.ApiStatus;

import java.util.Objects;

public interface ApiProxy {

    static boolean hasProxy() {
        return Storage.instance != null;
    }

    RegistryAccess getRegistryAccess();

    static RegistryAccess getRegistry() {
        Objects.requireNonNull(Storage.instance, "ApiProxy instance is null");
        return Storage.instance.getRegistryAccess();
    }

    @ApiStatus.Internal
    static void setInstance(ApiProxy instance) {
        Storage.instance = instance;
    }

    @ApiStatus.Internal
    class Storage {
        private static ApiProxy instance = null;
    }
}
