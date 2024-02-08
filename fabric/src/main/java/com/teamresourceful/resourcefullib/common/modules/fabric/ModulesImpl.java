package com.teamresourceful.resourcefullib.common.modules.fabric;

import com.teamresourceful.resourcefullib.common.modules.Module;
import com.teamresourceful.resourcefullib.common.modules.ModuleType;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;

@SuppressWarnings("UnstableApiUsage")
public class ModulesImpl {

    @SuppressWarnings("unchecked")
    private static <T extends Module> AttachmentType<T> resolve(RegistryEntry<ModuleType<T>> type) {
        if (type instanceof ModuleRegistryEntry<?, ModuleType<T>> entry) {
            return (AttachmentType<T>) entry.attachment();
        } else {
            throw new RuntimeException("ModuleTypeImpl#resolve: type is not a ModuleRegistryEntry");
        }
    }

    public static <T extends Module> Optional<T> get(RegistryEntry<ModuleType<T>> type, LivingEntity entity) {
        return Optional.ofNullable(entity.getAttached(resolve(type)));
    }

    public static <T extends Module> boolean set(RegistryEntry<ModuleType<T>> type, LivingEntity entity, T newValue) {
        entity.setAttached(resolve(type), newValue);
        return true;
    }
}
