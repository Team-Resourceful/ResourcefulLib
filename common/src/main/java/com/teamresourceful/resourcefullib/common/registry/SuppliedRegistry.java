package com.teamresourceful.resourcefullib.common.registry;

import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A generic registry which allows for creation of 1 time data values and give them a specific id.
 * This is not limited to vanilla registry types this can be used for any registry data type.
 * All values in this are registered late and are not registered right away and are not stored until first gotten.
 */
@ApiStatus.Experimental
public class SuppliedRegistry<K, T> {

    private final Map<K, Supplier<? extends T>> needsRegistration = new HashMap<>();
    protected List<Object<K, ? extends T>> registration = new ArrayList<>();
    protected final String modId;
    private Function<K, T> registryGetter;
    private boolean registered = false;

    public SuppliedRegistry(String modId) {
        this.modId = modId;
    }

    public static <K, T> SuppliedRegistry<K, T> of(String modId) {
        return new SuppliedRegistry<>(modId);
    }

    public static <T> ResourceSuppliedRegistry<T> ofResource(String modId) {
        return new ResourceSuppliedRegistry<>(modId);
    }

    public <O extends T> Object<K, T> register(K id, Supplier<? extends O> supplier) {
        if (registered) throw new RuntimeException("Cannot register after registration has occurred");
        needsRegistration.put(id, supplier);
        var obj = new Object<>(this, id);
        registration.add(obj);
        return obj;
    }

    public void startRegistration(BiConsumer<K, Supplier<? extends T>> registerer, Function<K, T> getter) {
        if (registered) throw new RuntimeException("Cannot register again after registration has occurred once");
        this.registryGetter = getter;
        this.needsRegistration.forEach(registerer);
        this.needsRegistration.clear(); // clear out the map, so we don't try to register again
        this.registered = true;
        this.registration = Collections.unmodifiableList(registration); // freeze registry!
    }

    public List<Object<K, ? extends T>> getRegistration() {
        return registration;
    }

    public boolean isFrozen() {
        return this.registered;
    }

    public static class Object<K, T> implements Supplier<T> {

        private final K id;
        private final SuppliedRegistry<K, T> supplier;

        private T value;
        private boolean hasValue = false;

        public Object(SuppliedRegistry<K, T> supplier, K id) {
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
}
