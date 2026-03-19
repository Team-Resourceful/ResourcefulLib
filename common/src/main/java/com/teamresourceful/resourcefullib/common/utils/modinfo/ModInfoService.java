package com.teamresourceful.resourcefullib.common.utils.modinfo;

import com.teamresourceful.resourcefullib.common.exceptions.NotImplementedException;
import com.teamresourceful.resourcefullib.common.lib.PlatformService;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@PlatformService
interface ModInfoService {

    boolean isModLoaded(String id);
    boolean isMixinModLoaded(String id);
    ModInfo getModInfo(String id);
    int getLoadedMods();

    static ModInfoService create() {
        throw new NotImplementedException();
    }
}
