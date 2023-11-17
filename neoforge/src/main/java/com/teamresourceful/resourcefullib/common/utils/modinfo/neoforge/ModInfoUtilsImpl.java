package com.teamresourceful.resourcefullib.common.utils.modinfo.neoforge;

import com.teamresourceful.resourcefullib.common.utils.modinfo.ModInfo;
import net.neoforged.fml.ModList;
import org.jetbrains.annotations.Nullable;

public class ModInfoUtilsImpl {
    @Nullable
    public static ModInfo getModInfo(String id) {
        return ModList.get()
            .getModContainerById(id)
            .map(modContainer -> new NeoForgeModInfo(modContainer.getModInfo()))
            .orElse(null);
    }

    public static int getLoadedMods() {
        return ModList.get().size();
    }
}
