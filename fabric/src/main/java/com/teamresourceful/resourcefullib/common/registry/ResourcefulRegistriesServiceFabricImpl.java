package com.teamresourceful.resourcefullib.common.registry;

import net.minecraft.core.Registry;

class ResourcefulRegistriesServiceFabricImpl implements ResourcefulRegistriesService {
    @Override
    public <T> ResourcefulRegistry<T> make(Registry<T> registry, String id) {
        return new FabricResourcefulRegistry<>(registry, id);
    }

    @Override
    public <D, T extends ResourcefulRegistry<D>> T make(ResourcefulRegistryType<D, T> type, String id) {
        if (type == ResourcefulRegistryType.FLUID) {
            return (T) new FabricResourcefulFluidRegistry(id);
        }
        throw new IllegalArgumentException("Unknown registry type: " + type);
    }
}
