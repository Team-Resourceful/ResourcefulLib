package com.teamresourceful.resourcefullib.common.registry.fabric;

import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistryType;
import net.minecraft.core.Registry;

public class ResourcefulRegistriesImpl {
    public static <T> ResourcefulRegistry<T> create(Registry<T> registry, String id) {
        return new FabricResourcefulRegistry<>(registry, id);
    }

    @SuppressWarnings("unchecked")
    public static <D, T extends ResourcefulRegistry<D>> T create(ResourcefulRegistryType<D, T> type, String id) {
        if (type == ResourcefulRegistryType.FLUID) {
            return (T) new FabricResourcefulFluidRegistry(id);
        }
        throw new IllegalArgumentException("Unknown registry type: " + type);
    }
}
