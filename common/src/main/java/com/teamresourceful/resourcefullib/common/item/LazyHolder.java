package com.teamresourceful.resourcefullib.common.item;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;
import java.util.function.Supplier;

public sealed class LazyHolder<T> implements Supplier<T>  {

    protected final ResourceLocation id;
    protected final Registry<T> registry;
    protected T item;

    public LazyHolder(Registry<T> registry, ResourceLocation id) {
        this.registry = registry;
        this.id = id;
    }

    public static <R> LazyHolder<R> of(Registry<R> registry, ResourceLocation id) {
        return new LazyHolder<>(registry, id);
    }

    public static <R> LazyHolder<R> of(Registry<R> registry, R value) {
        return new StaticHolder<>(registry, value);
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

    private static final class StaticHolder<T> extends LazyHolder<T> {

        public StaticHolder(Registry<T> registry, T value) {
            super(registry, registry.getKey(value));
            this.item = value;
        }
    }
}
