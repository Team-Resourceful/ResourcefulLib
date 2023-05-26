package com.teamresourceful.resourcefullib.common.utils.modinfo;

import com.teamresourceful.resourcefullib.common.exceptions.NotImplementedException;
import com.teamresourceful.resourcefullib.common.exceptions.UtilityClassException;
import dev.architectury.injectables.annotations.ExpectPlatform;
import org.jetbrains.annotations.Nullable;

public final class ModInfoUtils {

    private ModInfoUtils() throws UtilityClassException {
        throw new UtilityClassException();
    }

    public static boolean isModLoaded(String id) {
        return getModInfo(id) != null;
    }

    @Nullable
    @ExpectPlatform
    public static ModInfo getModInfo(String id) {
        throw new NotImplementedException();
    }
}
