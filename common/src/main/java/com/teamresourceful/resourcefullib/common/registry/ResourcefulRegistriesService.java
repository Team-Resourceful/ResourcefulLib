package com.teamresourceful.resourcefullib.common.registry;

import com.teamresourceful.resourcefullib.common.exceptions.NotImplementedException;
import com.teamresourceful.resourcefullib.common.lib.PlatformService;
import net.minecraft.core.Registry;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@PlatformService
interface ResourcefulRegistriesService {

    <T> ResourcefulRegistry<T> make(Registry<T> registry, String id);
    <D, T extends ResourcefulRegistry<D>> T make(ResourcefulRegistryType<D, T> type, String id);

    static ResourcefulRegistriesService create() {
        throw new NotImplementedException();
    }
}
