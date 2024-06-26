package com.teamresourceful.resourcefullib.neoforge;

import com.teamresourceful.resourcefullib.common.ApiProxy;
import net.minecraft.core.RegistryAccess;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.Objects;

public final class NeoForgeServerApiProxy implements ApiProxy {

    public static final NeoForgeServerApiProxy INSTANCE = new NeoForgeServerApiProxy();

    @Override
    public RegistryAccess getRegistryAccess() {
        Objects.requireNonNull(ServerLifecycleHooks.getCurrentServer(), "Server is null");
        return ServerLifecycleHooks.getCurrentServer().registryAccess();
    }
}
