package com.teamresourceful.resourcefullib.neoforge;

import com.teamresourceful.resourcefullib.ResourcefulLib;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;

@Mod(ResourcefulLib.MOD_ID)
public class ResourcefulLibNeoForge {

    public ResourcefulLibNeoForge() {
        ResourcefulLib.init();
        if (FMLLoader.getDist().isClient()) {
            ResourcefulLibNeoForgeClient.init();
        }
    }
}
