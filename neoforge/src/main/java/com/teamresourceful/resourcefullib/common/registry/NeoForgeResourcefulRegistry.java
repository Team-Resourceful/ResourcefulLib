
package com.teamresourceful.resourcefullib.common.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class NeoForgeResourcefulRegistry<T> implements ResourcefulRegistry<T> {
    private final ResourceKey<? extends Registry<T>> registryKey;
    private final String namespace;

    private final RegistryEntries<T> entries = new RegistryEntries<>();

    private final Map<Identifier, Supplier<? extends T>> pending = new LinkedHashMap<>();

    private boolean initialized;
    private boolean frozen;

    public NeoForgeResourcefulRegistry(Registry<T> registry, String namespace) {
        this.registryKey = registry.key();
        this.namespace = namespace;
    }

    @Override
    public String namespace() {
        return namespace;
    }

    @Override
    public <I extends T> RegistryEntry<I> register(String id, Supplier<I> supplier) {
        return register(
                Identifier.fromNamespaceAndPath(namespace, id),
                supplier
        );
    }

    @Override
    public <I extends T> RegistryEntry<I> register(Identifier id, Supplier<I> supplier) {
        addPending(id, supplier);
        DeferredHolder<T, I> holder = DeferredHolder.create(registryKey, id);
        return entries.add(new NeoForgeRegistryEntry<>(holder));
    }

    @Override
    public HolderRegistryEntry<T> registerHolder(String id, Supplier<T> supplier) {
        return registerHolder(Identifier.fromNamespaceAndPath(namespace, id), supplier);
    }

    @Override
    public HolderRegistryEntry<T> registerHolder(Identifier id, Supplier<T> supplier) {
        addPending(id, supplier);

        DeferredHolder<T, T> holder = DeferredHolder.create(registryKey, id);

        return entries.add(new NeoForgeHolderRegistryEntry<>(holder));
    }

    private void addPending(Identifier id, Supplier<? extends T> supplier) {
        if (frozen) {
            throw new IllegalStateException("Registry is already frozen: " + registryKey);
        }

        if (pending.containsKey(id)) {
            throw new IllegalArgumentException("Duplicate registry entry: " + id);
        }

        pending.put(id, supplier);
    }

    private void onRegister(RegisterEvent event) {
        event.register(registryKey, helper -> {
            frozen = true;

            pending.forEach((id, supplier) ->
                    helper.register(id, supplier.get())
            );
        });
    }

    @Override
    public Collection<RegistryEntry<T>> getEntries() {
        return entries.getEntries();
    }

    @Override
    public void init() {
        if (initialized) {
            return;
        }

        initialized = true;

        ModLoadingContext.get()
                .getActiveContainer()
                .getEventBus()
                .addListener(this::onRegister);
    }
}
