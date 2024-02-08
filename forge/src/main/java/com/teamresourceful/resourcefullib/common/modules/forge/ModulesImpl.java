package com.teamresourceful.resourcefullib.common.modules.forge;

import com.teamresourceful.resourcefullib.common.modules.Module;
import com.teamresourceful.resourcefullib.common.modules.ModuleType;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import java.util.Optional;

public class ModulesImpl {

    private static <T extends Module> Capability<T> resolve(RegistryEntry<ModuleType<T>> type) {
        if (type instanceof ModuleRegistryEntry<T,ModuleType<T>> entry) {
            return entry.capability();
        } else {
            throw new RuntimeException("ModuleTypeImpl#resolve: type is not a ModuleRegistryEntry");
        }
    }

    public static <T extends Module> Optional<T> get(RegistryEntry<ModuleType<T>> type, LivingEntity entity) {
        return entity.getCapability(resolve(type)).resolve();
    }

    public static <T extends Module> boolean set(RegistryEntry<ModuleType<T>> type, LivingEntity entity, T newValue) {
        Capability<T> capability = resolve(type);
        LazyOptional<T> optional = entity.getCapability(capability);
        optional.ifPresent(value -> ModuleRegistry.copy(newValue, value));
        return optional.isPresent();
    }
}
