package com.teamresourceful.resourcefullib.common.modules.fabric;

import com.teamresourceful.resourcefullib.common.modules.Module;
import com.teamresourceful.resourcefullib.common.modules.ModuleType;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

@SuppressWarnings("UnstableApiUsage")
public class ModuleRegistryEntry<M extends Module, T extends ModuleType<?>> implements RegistryEntry<T> {

    private final AttachmentType<M> attachment;
    private final T type;

    @SuppressWarnings("unchecked")
    public <I extends ModuleType<?>> ModuleRegistryEntry(AttachmentType<M> attachment, Supplier<I> type) {
        this.attachment = attachment;
        this.type = (T) type.get();
    }

    @Override
    public T get() {
        return type;
    }

    @Override
    public ResourceLocation getId() {
        return attachment.identifier();
    }

    public AttachmentType<M> attachment() {
        return attachment;
    }
}
