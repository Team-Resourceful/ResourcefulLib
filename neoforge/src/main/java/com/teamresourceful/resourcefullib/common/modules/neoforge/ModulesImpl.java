package com.teamresourceful.resourcefullib.common.modules.neoforge;

import com.teamresourceful.resourcefullib.common.modules.Module;
import com.teamresourceful.resourcefullib.common.modules.ModuleType;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.attachment.AttachmentType;

import java.util.Optional;

public class ModulesImpl {

    private static <T extends Module> AttachmentType<T> resolve(RegistryEntry<ModuleType<T>> type) {
        if (type instanceof ModuleRegistryEntry<T, ModuleType<T>> entry) {
            return entry.attachment();
        } else {
            throw new RuntimeException("ModuleTypeImpl#resolve: type is not a ModuleRegistryEntry");
        }
    }

    public static <T extends Module> Optional<T> get(RegistryEntry<ModuleType<T>> type, LivingEntity entity) {
        AttachmentType<T> attachment = resolve(type);
        if (entity.hasData(attachment)) {
            return Optional.ofNullable(entity.getData(attachment));
        }
        return Optional.empty();
    }

    public static <T extends Module> boolean set(RegistryEntry<ModuleType<T>> type, LivingEntity entity, T newValue) {
        entity.setData(resolve(type), newValue);
        return true;
    }
}
