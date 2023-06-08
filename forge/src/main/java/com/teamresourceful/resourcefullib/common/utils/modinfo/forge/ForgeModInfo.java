package com.teamresourceful.resourcefullib.common.utils.modinfo.forge;

import com.teamresourceful.resourcefullib.common.utils.modinfo.ModInfo;
import net.minecraftforge.forgespi.language.IModInfo;

public record ForgeModInfo(IModInfo info) implements ModInfo {

    @Override
    public String displayName() {
        return info.getDisplayName();
    }

    @Override
    public String id() {
        return info.getModId();
    }

    @Override
    public String version() {
        return info.getVersion().toString();
    }
}
