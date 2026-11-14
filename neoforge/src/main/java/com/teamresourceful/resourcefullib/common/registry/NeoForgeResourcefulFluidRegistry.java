
package com.teamresourceful.resourcefullib.common.registry;

import com.teamresourceful.resourcefullib.common.fluid.data.FluidData;
import com.teamresourceful.resourcefullib.common.fluid.data.FluidProperties;
import com.teamresourceful.resourcefullib.common.fluid.data.InternalFluidData;
import com.teamresourceful.resourcefullib.common.fluid.neoforge.ResourcefulFluidType;
import com.teamresourceful.resourcefullib.common.fluid.registry.ResourcefulFluidRegistry;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NeoForgeResourcefulFluidRegistry implements ResourcefulFluidRegistry {

    private static final Map<Identifier, FluidData> GLOBAL_REGISTRY = new ConcurrentHashMap<>();

    private final String namespace;

    private final NeoForgeResourcefulRegistry<FluidType> registry;

    private final RegistryEntries<FluidData> entries = new RegistryEntries<>();

    public NeoForgeResourcefulFluidRegistry(String namespace) {
        this.namespace = namespace;
        this.registry = new NeoForgeResourcefulRegistry<>(
                NeoForgeRegistries.FLUID_TYPES,
                namespace
        );
    }

    @Override
    public String namespace() {
        return namespace;
    }

    @Override
    public RegistryEntry<FluidData> register(String name, FluidProperties properties) {
        return register(Identifier.fromNamespaceAndPath(namespace, name), properties);
    }

    @Override
    public RegistryEntry<FluidData> register(Identifier id, FluidProperties properties) {
        if (GLOBAL_REGISTRY.containsKey(id)) {
            throw new IllegalArgumentException("Duplicate fluid registration: " + id);
        }

        var fluidType = registry.register(id, () -> new ResourcefulFluidType(id, properties));
        var data = new InternalFluidData(id, properties, fluidType);
        GLOBAL_REGISTRY.put(id, data);

        return entries.add(new Entry(id, data));
    }

    @Override
    public Collection<RegistryEntry<FluidData>> getEntries() {
        return entries.getEntries();
    }

    @Override
    public void init() {
        registry.init();
    }

    @ApiStatus.Internal
    public static Map<Identifier, FluidData> entries() {
        return GLOBAL_REGISTRY;
    }
}
