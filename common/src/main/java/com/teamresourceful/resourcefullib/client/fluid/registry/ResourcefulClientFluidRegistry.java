package com.teamresourceful.resourcefullib.client.fluid.registry;

import com.teamresourceful.resourcefullib.client.fluid.data.ClientFluidProperties;
import com.teamresourceful.resourcefullib.common.registry.HolderRegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntries;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class ResourcefulClientFluidRegistry implements ResourcefulRegistry<ClientFluidProperties> {

    private static final Map<ResourceLocation, ClientFluidProperties> REGISTRY = new ConcurrentHashMap<>();

    private final String modid;
    private final RegistryEntries<ClientFluidProperties> entries = new RegistryEntries<>();

    public ResourcefulClientFluidRegistry(String modid) {
        this.modid = modid;
    }

    public RegistryEntry<ClientFluidProperties> register(String id, ClientFluidProperties.Builder builder) {
        return register(id, builder::build);
    }

    @Override
    public <I extends ClientFluidProperties> RegistryEntry<I> register(String id, Supplier<I> supplier) {
        REGISTRY.put(new ResourceLocation(this.modid, id), supplier.get());
        return entries.add(new Entry<>(new ResourceLocation(this.modid, id), supplier.get()));
    }

    /**
     * @hidden use {@link #register(String, ClientFluidProperties.Builder)} instead
     */
    @ApiStatus.Internal
    @Deprecated
    @Override
    public HolderRegistryEntry<ClientFluidProperties> registerHolder(String id, Supplier<ClientFluidProperties> supplier) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Use register(String, ClientFluidProperties.Builder) instead.");
    }

    @Override
    public Collection<RegistryEntry<ClientFluidProperties>> getEntries() {
        return entries.getEntries();
    }

    @Override
    public void init() {

    }

    @ApiStatus.Internal
    public static ClientFluidProperties get(ResourceLocation id) {
        return REGISTRY.get(id);
    }

    private record Entry<T extends ClientFluidProperties>(ResourceLocation id, T data) implements RegistryEntry<T> {

        @Override
        public T get() {
            return this.data;
        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }
    }
}