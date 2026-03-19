package com.teamresourceful.resourcefullib.common.utils.modinfo;

import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;

class ModInfoServiceNeoForgeImpl implements ModInfoService {

    @Override
    public boolean isModLoaded(String id) {
        return ModList.get().isLoaded(id);
    }

    @Override
    public boolean isMixinModLoaded(String id) {
        return FMLLoader.getCurrent().getLoadingModList().getModFileById(id) != null;
    }

    @Override
    public ModInfo getModInfo(String id) {
        return ModList.get()
                .getModContainerById(id)
                .map(modContainer -> new NeoForgeModInfo(modContainer.getModInfo()))
                .orElse(null);
    }

    @Override
    public int getLoadedMods() {
        return ModList.get().size();
    }
}
