package com.teamresourceful.resourcefullib.common.registry.builtin;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import com.teamresourceful.resourcefullib.common.registry.builtin.base.ItemLikeEntry;
import com.teamresourceful.resourcefullib.common.registry.builtin.base.ItemLikeHolderEntry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class ResourcefulBlockRegistry implements ResourcefulRegistry<Block> {

    private final String namespace;
    private final ResourcefulRegistry<Block> registry;

    public ResourcefulBlockRegistry(String id) {
        this(ResourcefulRegistries.create(BuiltInRegistries.BLOCK, id));
    }

    public ResourcefulBlockRegistry(ResourcefulRegistry<Block> parent) {
        this.namespace = Objects.requireNonNull(parent.namespace(), "Parent registry must have a namespace.");
        this.registry = parent;
    }

    public <I extends Block> ItemLikeEntry<I> register(String id, Function<Block.Properties, I> factory, Supplier<Block.Properties> getter) {
        var key = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(this.namespace, id));
        return this.register(id, () -> factory.apply(getter.get().setId(key)));
    }

    @Override
    public String namespace() {
        return this.namespace;
    }

    @Override
    public <I extends Block> ItemLikeEntry<I> register(String id, Supplier<I> supplier) {
        return new ItemLikeEntry<>(this.registry.register(id, supplier));
    }

    @Override
    public ItemLikeHolderEntry<Block> registerHolder(String id, Supplier<Block> supplier) {
        return new ItemLikeHolderEntry<>(this.registry.registerHolder(id, supplier));
    }

    @Override
    public Collection<RegistryEntry<Block>> getEntries() {
        return this.registry.getEntries();
    }

    @Override
    public void init() {
        this.registry.init();
    }
}
