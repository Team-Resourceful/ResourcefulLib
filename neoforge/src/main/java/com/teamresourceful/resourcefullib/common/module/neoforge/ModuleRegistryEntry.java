package com.teamresourceful.resourcefullib.common.module.neoforge;

import com.teamresourceful.resourcefullib.common.modules.ModuleType;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public class ModuleRegistryEntry<T extends ModuleType<?>> implements RegistryEntry<T> {

    private final DeferredHolder<AttachmentType<?>, AttachmentType<?>> object;
    private final Supplier<T> type;

    public ModuleRegistryEntry(DeferredHolder<AttachmentType<?>, AttachmentType<?>> object, Supplier<T> type) {
        this.object = object;
        this.type = type;
    }

    @Override
    public T get() {
        return type.get();
    }

    @Override
    public ResourceLocation getId() {
        return object.getId();
    }
}
