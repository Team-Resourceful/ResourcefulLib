package com.teamresourceful.resourcefullib.forge;

import com.teamresourceful.resourcefullib.ResourcefulLib;
import net.minecraftforge.fml.common.Mod;

@Mod(ResourcefulLib.MOD_ID)
public class ResourcefulLibForge {

    public ResourcefulLibForge() {
        ResourcefulLib.init();
    }
}
