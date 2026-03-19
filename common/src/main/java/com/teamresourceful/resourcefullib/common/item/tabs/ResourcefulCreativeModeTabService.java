package com.teamresourceful.resourcefullib.common.item.tabs;

import com.teamresourceful.resourcefullib.common.exceptions.NotImplementedException;
import com.teamresourceful.resourcefullib.common.lib.PlatformService;
import net.minecraft.world.item.CreativeModeTab;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@PlatformService
interface ResourcefulCreativeModeTabService {

    CreativeModeTab register(ResourcefulCreativeModeTab tab);

    static ResourcefulCreativeModeTabService create() {
        throw new NotImplementedException();
    }
}
