package com.teamresourceful.resourcefullib.common.utils.modinfo.fabric;

import com.teamresourceful.resourcefullib.common.utils.modinfo.ModInfo;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.jetbrains.annotations.Nullable;

public class ModInfoUtilsImpl {
    @Nullable
    public static ModInfo getModInfo(String id) {
        return FabricLoader.getInstance()
            .getModContainer(id)
            .map(Info::new)
            .orElse(null);
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
    }
}
