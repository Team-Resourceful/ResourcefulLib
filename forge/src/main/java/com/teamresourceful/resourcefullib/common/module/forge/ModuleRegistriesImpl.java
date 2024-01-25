package com.teamresourceful.resourcefullib.common.module.forge;

import com.teamresourceful.resourcefullib.common.modules.ModuleType;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;

public class ModuleRegistriesImpl {
    public static ResourcefulRegistry<ModuleType<?>> create(String id) {
        return new ModuleRegistry(id);
    }
}
