package com.teamresourceful.resourcefullib.common.utils.modinfo.neoforge;

import com.teamresourceful.resourcefullib.common.utils.modinfo.ModInfo;
import net.neoforged.neoforgespi.language.IModInfo;

import java.nio.file.Path;
import java.util.List;

public record NeoForgeModInfo(IModInfo info) implements ModInfo {

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

    public List<Path> getPaths() {
        return List.of(info.getOwningFile().getFile().getFilePath());
    }
}
