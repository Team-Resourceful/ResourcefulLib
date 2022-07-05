package com.teamresourceful.resourcefullib.fabric;

import com.teamresourceful.resourcefullib.ResourcefulLib;
import net.fabricmc.api.ModInitializer;

public class ResourcefulLibFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ResourcefulLib.init();
    }
}
