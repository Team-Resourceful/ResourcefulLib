package com.teamresourceful.resourcefullib.common.utils.modinfo;

import com.teamresourceful.resourcefullib.common.exceptions.UtilityClassException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public final class ModInfoUtils {

    private static final ModInfoService SERVICE = ModInfoService.create();

    private ModInfoUtils() throws UtilityClassException {
        throw new UtilityClassException();
    }

    @Contract(pure = true)
    public static boolean isModLoaded(String id) {
        return SERVICE.isModLoaded(id);
    }

    @Contract(pure = true)
    public static boolean isMixinModLoaded(String id) {
        return SERVICE.isMixinModLoaded(id);
    }

    @Nullable
    public static ModInfo getModInfo(String id) {
        return SERVICE.getModInfo(id);
    }

    public static int getLoadedMods() {
        return SERVICE.getLoadedMods();
    }
}
