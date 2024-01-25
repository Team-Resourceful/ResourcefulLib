package com.teamresourceful.resourcefullib.common.module.forge;

import com.teamresourceful.resourcefullib.common.modules.Module;
import com.teamresourceful.resourcefullib.common.modules.ModuleType;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;

import java.util.function.Supplier;

public class ModuleRegistryEntry<M extends Module<M>, T extends ModuleType<?>> implements RegistryEntry<T> {

    private final ResourceLocation id;
    private final T type;
    private final Capability<M> capability;

    @SuppressWarnings("unchecked")
    public <I extends ModuleType<?>, V extends ModuleType<M>> ModuleRegistryEntry(ResourceLocation id, Supplier<I> type) {
        this.id = id;
        this.type = (T) type.get();
        this.capability = HackyCapabilityManager.get(((V) this.type).type());
    }

    @Override
    public T get() {
        return type;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    public Capability<M> capability() {
        return capability;
    }
}
