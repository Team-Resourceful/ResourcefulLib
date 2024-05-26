package com.teamresourceful.resourcefullib.common.registry;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.Holder;
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
    private final List<ItemLikeEntry<T>> entries = new ArrayList<>();

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
    public HolderRegistryEntry<T> registerHolder(String id, Supplier<T> supplier) {
        HolderEntry<T> entry = new HolderEntry<>(parent.registerHolder(id, supplier));
        this.entries.add(entry);
        return entry;
    }

    @Override
    public Collection<RegistryEntry<T>> getEntries() {
        return ImmutableList.copyOf(this.entries);
    }

    public Collection<ItemLikeEntry<T>> getItemLikeEntries() {
        return ImmutableList.copyOf(this.entries);
    }

    @Override
    public void init() {
        this.parent.init();
    }

    public interface ItemLikeEntry<T extends ItemLike> extends RegistryEntry<T>, ItemLike {}

    public record Entry<T extends ItemLike>(RegistryEntry<T> entry) implements ItemLikeEntry<T> {

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

    public record HolderEntry<T extends ItemLike>(HolderRegistryEntry<T> entry) implements HolderRegistryEntry<T>, ItemLikeEntry<T> {

        @Override
        public Holder<T> holder() {
            return entry.holder();
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