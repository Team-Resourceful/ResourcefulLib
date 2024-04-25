package com.teamresourceful.resourcefullib.common.utils.modinfo.neoforge;

import com.teamresourceful.resourcefullib.common.utils.modinfo.ModInfo;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.LoadingModList;
import org.jetbrains.annotations.Nullable;

public class ModInfoUtilsImpl {

    public static boolean isModLoaded(String id) {
        return ModList.get().isLoaded(id);
    }

    public static boolean isMixinModLoaded(String id) {
        return LoadingModList.get().getModFileById(id) != null;
    }

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
