package com.teamresourceful.resourcefullib.common.modules;

import com.teamresourceful.resourcefullib.common.exceptions.NotImplementedException;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.LivingEntity;

public class Modules {

    @ExpectPlatform
    static <T extends Module> T get(RegistryEntry<ModuleType<T>> type, LivingEntity entity) {
        throw new NotImplementedException();
    }

    @ExpectPlatform
    static <T extends Module> boolean set(RegistryEntry<ModuleType<T>> type, LivingEntity entity, T newValue) {
        throw new NotImplementedException();
    }
}
