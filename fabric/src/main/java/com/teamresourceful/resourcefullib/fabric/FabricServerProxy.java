package com.teamresourceful.resourcefullib.fabric;

import com.teamresourceful.resourcefullib.common.ApiProxy;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.MinecraftServer;

import java.util.Objects;

public class FabricServerProxy implements ApiProxy {

    public static final FabricServerProxy INSTANCE = new FabricServerProxy();
    public static MinecraftServer server = null;

    @Override
    public RegistryAccess getRegistryAccess() {
        Objects.requireNonNull(server, "Server is null");
        return server.registryAccess();
    }
}
