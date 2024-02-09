package com.teamresourceful.resourcefullib.forge;

import com.teamresourceful.resourcefullib.ResourcefulLib;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(ResourcefulLib.MOD_ID)
public class ResourcefulLibForge {

    public ResourcefulLibForge() {
        ResourcefulLib.init();
        ForgeCompatibilityHandler.load();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ResourcefulLibForgeClient::init);
    }
}
