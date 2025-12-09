package com.teamresourceful.resourcefullib.common.registry.fabric;

import com.teamresourceful.resourcefullib.common.registry.HolderRegistryEntry;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class FabricHolderRegistryEntry<T> implements HolderRegistryEntry<T> {

    private final Identifier id;
    private final Holder<@NotNull T> value;

    private FabricHolderRegistryEntry(Identifier id, Holder<@NotNull T> value) {
        this.id = id;
        this.value = value;
    }

    public static <T, I extends T> FabricHolderRegistryEntry<T> of(Registry<@NotNull T> registry, Identifier id, Supplier<I> supplier) {
        return new FabricHolderRegistryEntry<>(id, Registry.registerForHolder(registry, id, supplier.get()));
    }

    @Override
    public Holder<@NotNull T> holder() {
        return this.value;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }
}