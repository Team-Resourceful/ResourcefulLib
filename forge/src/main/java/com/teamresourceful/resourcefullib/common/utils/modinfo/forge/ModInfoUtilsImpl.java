package com.teamresourceful.resourcefullib.common.utils.modinfo.forge;

import com.teamresourceful.resourcefullib.common.utils.modinfo.ModInfo;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.Nullable;

public class ModInfoUtilsImpl {
    @Nullable
    public static ModInfo getModInfo(String id) {
        return ModList.get()
            .getModContainerById(id)
            .map(modContainer -> new ForgeModInfo(modContainer.getModInfo()))
            .orElse(null);
    }
}
