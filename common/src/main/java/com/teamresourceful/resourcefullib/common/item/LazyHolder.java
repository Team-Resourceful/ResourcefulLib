package com.teamresourceful.resourcefullib.common.item;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;
import java.util.function.Supplier;

public final class LazyHolder<T> implements Supplier<T> {

    private final ResourceLocation id;
    private final Registry<T> registry;
    private T item;

    public LazyHolder(Registry<T> registry, ResourceLocation id) {
        this.registry = registry;
        this.id = id;
    }

    public ResourceLocation getId() {
        return id;
    }

    public Registry<T> getRegistry() {
        return registry;
    }

    @Override
    public T get() {
        if (this.item == null) {
            this.item = registry.getOptional(id).orElseThrow(() -> new IllegalStateException(registry.key() + ": " + id + " does not exist!"));
        }
        return this.item;
    }

    public static <T> Function<ResourceLocation, LazyHolder<T>> map(Registry<T> registry) {
        return id -> new LazyHolder<>(registry, id);
    }
}
