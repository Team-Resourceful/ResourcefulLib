package com.teamresourceful.resourcefullib.common.utils.modinfo;

import com.teamresourceful.resourcefullib.common.exceptions.NotImplementedException;
import com.teamresourceful.resourcefullib.common.exceptions.UtilityClassException;
import dev.architectury.injectables.annotations.ExpectPlatform;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public final class ModInfoUtils {

    private ModInfoUtils() throws UtilityClassException {
        throw new UtilityClassException();
    }

    @ExpectPlatform
    @Contract(pure = true)
    public static boolean isModLoaded(String id) {
        throw new NotImplementedException();
    }

    @ExpectPlatform
    @Contract(pure = true)
    public static boolean isMixinModLoaded(String id) {
        throw new NotImplementedException();
    }

    @Nullable
    @ExpectPlatform
    public static ModInfo getModInfo(String id) {
        throw new NotImplementedException();
    }

    @ExpectPlatform
    public static int getLoadedMods() {
        throw new NotImplementedException();
    }
}
