package com.teamresourceful.resourcefullib.common.registry;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class ItemLikeResourcefulRegistry<T extends ItemLike> implements ResourcefulRegistry<T>  {

    private final ResourcefulRegistry<T> parent;
    private final List<Entry<T>> entries = new ArrayList<>();

    public ItemLikeResourcefulRegistry(Registry<T> registry, String id) {
        this.parent = ResourcefulRegistries.create(registry, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <I extends T> Entry<I> register(String id, Supplier<I> supplier) {
        Entry<I> entry = new Entry<>(parent.register(id, supplier));
        this.entries.add((Entry<T>) entry);
        return entry;
    }

    @Override
    public Collection<RegistryEntry<T>> getEntries() {
        return ImmutableList.copyOf(this.entries);
    }

    public Collection<Entry<T>> getItemLikeEntries() {
        return ImmutableList.copyOf(this.entries);
    }

    @Override
    public void init() {
        this.parent.init();
    }

    public record Entry<T extends ItemLike>(RegistryEntry<T> entry) implements RegistryEntry<T>, ItemLike {

        @Override
        public T get() {
            return entry.get();
        }

        @Override
        public ResourceLocation getId() {
            return entry.getId();
        }

        @Override
        public @NotNull Item asItem() {
            return entry.get().asItem();
        }
    }
}