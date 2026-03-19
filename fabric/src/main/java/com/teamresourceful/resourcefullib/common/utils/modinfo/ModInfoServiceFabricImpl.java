package com.teamresourceful.resourcefullib.common.utils.modinfo;

import com.teamresourceful.resourcefullib.common.utils.modinfo.ModInfo;
import com.teamresourceful.resourcefullib.common.utils.modinfo.ModInfoService;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.nio.file.Path;
import java.util.List;

public class ModInfoServiceFabricImpl implements ModInfoService {

    @Override
    public boolean isModLoaded(String id) {
        return FabricLoader.getInstance().isModLoaded(id);
    }

    @Override
    public boolean isMixinModLoaded(String id) {
        return FabricLoader.getInstance().isModLoaded(id);
    }

    @Override
    public ModInfo getModInfo(String id) {
        return FabricLoader.getInstance()
                .getModContainer(id)
                .map(Info::new)
                .orElse(null);
    }

    @Override
    public int getLoadedMods() {
        return FabricLoader.getInstance().getAllMods().size();
    }

    private record Info(ModContainer container) implements ModInfo {

        @Override
        public String displayName() {
            return container.getMetadata().getName();
        }

        @Override
        public String id() {
            return container.getMetadata().getId();
        }

        @Override
        public String version() {
            return container.getMetadata().getVersion().getFriendlyString();
        }

        public List<Path> getPaths() {
            return List.copyOf(container.getRootPaths());
        }
    }
}
