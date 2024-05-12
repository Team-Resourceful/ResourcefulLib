package com.teamresourceful.resourcefullib.common.registry.fabric;

import com.teamresourceful.resourcefullib.common.registry.HolderRegistryEntry;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class FabricHolderRegistryEntry<T> implements HolderRegistryEntry<T> {

    private final ResourceLocation id;
    private final Holder<T> value;

    private FabricHolderRegistryEntry(ResourceLocation id, Holder<T> value) {
        this.id = id;
        this.value = value;
    }

    public static <T, I extends T> FabricHolderRegistryEntry<T> of(Registry<T> registry, ResourceLocation id, Supplier<I> supplier) {
        return new FabricHolderRegistryEntry<>(id, Registry.registerForHolder(registry, id, supplier.get()));
    }

    @Override
    public Holder<T> holder() {
        return this.value;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }
}