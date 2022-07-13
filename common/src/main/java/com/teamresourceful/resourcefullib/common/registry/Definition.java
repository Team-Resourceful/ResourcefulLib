package com.teamresourceful.resourcefullib.common.registry;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

@ApiStatus.Experimental
public class Definition<K, T> implements Supplier<T> {

    private final K id;
    private final SuppliedRegistry<K, T> supplier;

    private T value;
    private boolean hasValue = false;

    public Definition(SuppliedRegistry<K, T> supplier, K id) {
        this.id = id;
        this.supplier = supplier;
    }

    @Nullable
    @Override
    public T get() {
        if (!hasValue) {
            if (!supplier.registered) {
                throw new RuntimeException("Cannot get value before registration has occurred");
            }
            value = supplier.registryGetter.apply(id);
            hasValue = true;
        }
        return value;
    }
}
