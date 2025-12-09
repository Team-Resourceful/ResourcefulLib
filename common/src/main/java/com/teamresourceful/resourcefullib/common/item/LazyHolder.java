package com.teamresourceful.resourcefullib.common.item;

import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public sealed class LazyHolder<T> implements Supplier<T>  {

    protected final Identifier id;
    protected final Registry<@NotNull T> registry;
    protected T item;

    public LazyHolder(Registry<@NotNull T> registry, Identifier id) {
        this.registry = registry;
        this.id = id;
    }

    public static <R> LazyHolder<R> of(Registry<@NotNull R> registry, Identifier id) {
        return new LazyHolder<>(registry, id);
    }

    public static <R> LazyHolder<R> of(Registry<@NotNull R> registry, R value) {
        return new StaticHolder<>(registry, value);
    }

    public Identifier getId() {
        return id;
    }

    public Registry<@NotNull T> getRegistry() {
        return registry;
    }

    @Override
    public T get() {
        if (this.item == null) {
            this.item = registry.getOptional(id).orElseThrow(() -> new IllegalStateException(registry.key() + ": " + id + " does not exist!"));
        }
        return this.item;
    }

    public static <T> Function<Identifier, LazyHolder<T>> map(Registry<@NotNull T> registry) {
        return id -> new LazyHolder<>(registry, id);
    }

    private static final class StaticHolder<T> extends LazyHolder<T> {

        public StaticHolder(Registry<@NotNull T> registry, T value) {
            super(registry, registry.getKey(value));
            this.item = value;
        }
    }
}
