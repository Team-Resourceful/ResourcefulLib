package com.teamresourceful.resourcefullib.fabric;

import com.teamresourceful.resourcefullib.common.ApiProxy;
import net.fabricmc.api.DedicatedServerModInitializer;

public class ResourcefulLibFabricServer implements DedicatedServerModInitializer {

    @Override
    public void onInitializeServer() {
        ApiProxy.setInstance(FabricServerProxy.INSTANCE);
    }
}
