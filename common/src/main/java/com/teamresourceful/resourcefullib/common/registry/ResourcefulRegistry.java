package com.teamresourceful.resourcefullib.common.registry;

import net.minecraft.resources.Identifier;

import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface ResourcefulRegistry<T> {

    default String namespace() {
        return null;
    }

    <I extends T> RegistryEntry<I> register(String id, Supplier<I> supplier);

    HolderRegistryEntry<T> registerHolder(String id, Supplier<T> supplier);

    default <I extends T> RegistryEntry<I> register(Identifier id, Supplier<I> supplier) {
        if (!id.getNamespace().equals(namespace())) {
            throw new UnsupportedOperationException(
                    "Registry does not support external namespace: " + id
            );
        }

        return register(id.getPath(), supplier);
    }

    default HolderRegistryEntry<T> registerHolder(Identifier id, Supplier<T> supplier) {
        if (!id.getNamespace().equals(namespace())) {
            throw new UnsupportedOperationException(
                    "Registry does not support external namespace: " + id
            );
        }

        return registerHolder(id.getPath(), supplier);
    }

    Collection<RegistryEntry<T>> getEntries();

    default Stream<RegistryEntry<T>> stream() {
        return getEntries().stream();
    }

    default Stream<T> boundStream() {
        return stream().map(RegistryEntry::get);
    }

    void init();
}
