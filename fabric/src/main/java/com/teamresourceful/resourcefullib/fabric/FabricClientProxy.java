package com.teamresourceful.resourcefullib.fabric;

import com.teamresourceful.resourcefullib.common.ApiProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;

import java.util.Objects;

public class FabricClientProxy implements ApiProxy {

    public static final FabricClientProxy INSTANCE = new FabricClientProxy();

    @Override
    public RegistryAccess getRegistryAccess() {
        Objects.requireNonNull(Minecraft.getInstance().getConnection(), "Client is not connected to a server");
        return Minecraft.getInstance().getConnection().registryAccess();
    }
}
