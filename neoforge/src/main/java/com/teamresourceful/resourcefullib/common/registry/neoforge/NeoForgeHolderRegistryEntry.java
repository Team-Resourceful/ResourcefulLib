package com.teamresourceful.resourcefullib.common.registry.neoforge;

import com.teamresourceful.resourcefullib.common.registry.HolderRegistryEntry;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;

public class NeoForgeHolderRegistryEntry<R> implements HolderRegistryEntry<R> {

    private final DeferredHolder<R, R> object;

    public NeoForgeHolderRegistryEntry(DeferredHolder<R, R> object) {
        this.object = object;
    }

    @Override
    public Holder<R> holder() {
        return object;
    }

    @Override
    public ResourceLocation getId() {
        return object.getId();
    }
}
