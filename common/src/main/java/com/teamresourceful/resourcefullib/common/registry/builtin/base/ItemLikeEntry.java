package com.teamresourceful.resourcefullib.common.registry.builtin.base;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

public record ItemLikeEntry<T extends ItemLike>(RegistryEntry<T> entry) implements RegistryEntry<T>, ItemLike {

    @Override
    public T get() {
        return entry.get();
    }

    @Override
    public Identifier getId() {
        return entry.getId();
    }

    @Override
    @NotNull
    public Item asItem() {
        return get().asItem();
    }
}