package com.teamresourceful.resourcefullib.common.registry.neoforge;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.registries.DeferredHolder;

public class NeoForgeRegistryEntry<R, T extends R> implements RegistryEntry<T> {

    private final DeferredHolder<R, T> object;

    public NeoForgeRegistryEntry(DeferredHolder<R, T> object) {
        this.object = object;
    }

    @Override
    public T get() {
        return object.get();
    }

    @Override
    public Identifier getId() {
        return object.getId();
    }
}
