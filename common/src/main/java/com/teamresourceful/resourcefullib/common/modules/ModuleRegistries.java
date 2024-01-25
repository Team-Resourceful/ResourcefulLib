package com.teamresourceful.resourcefullib.common.modules;

import com.teamresourceful.resourcefullib.common.exceptions.NotImplementedException;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import dev.architectury.injectables.annotations.ExpectPlatform;

public class ModuleRegistries {

    @ExpectPlatform
    public static ResourcefulRegistry<ModuleType<?>> create(String id) {
        throw new NotImplementedException();
    }
}
