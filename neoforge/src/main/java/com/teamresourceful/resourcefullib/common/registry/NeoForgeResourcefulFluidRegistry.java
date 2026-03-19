package com.teamresourceful.resourcefullib.common.registry;

import com.teamresourceful.resourcefullib.common.fluid.data.FluidData;
import com.teamresourceful.resourcefullib.common.fluid.data.FluidProperties;
import com.teamresourceful.resourcefullib.common.fluid.data.InternalFluidData;
import com.teamresourceful.resourcefullib.common.fluid.neoforge.ResourcefulFluidType;
import com.teamresourceful.resourcefullib.common.fluid.registry.ResourcefulFluidRegistry;
import net.minecraft.resources.Identifier;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NeoForgeResourcefulFluidRegistry implements ResourcefulFluidRegistry {

    private static final Map<Identifier, FluidData> GLOBAL_REGISTRY = new ConcurrentHashMap<>();

    private final String id;
    private final DeferredRegister<FluidType> registry;
    private final RegistryEntries<FluidData> entries = new RegistryEntries<>();

    public NeoForgeResourcefulFluidRegistry(String id) {
        this.id = id;
        this.registry = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, id);
    }

    @Override
    public String namespace() {
        return this.id;
    }

    @Override
    public RegistryEntry<FluidData> register(String name, FluidProperties properties) {
        var rid = Identifier.fromNamespaceAndPath(this.id, name);
        var data = new InternalFluidData(
                rid,
                properties,
                this.registry.register(name, (id) -> new ResourcefulFluidType(id, properties))
        );
        GLOBAL_REGISTRY.put(rid, data);
        return this.entries.add(new Entry(rid, data));
    }

    @Override
    public Collection<RegistryEntry<FluidData>> getEntries() {
        return this.entries.getEntries();
    }

    @Override
    public void init() {
        this.registry.register(ModLoadingContext.get().getActiveContainer().getEventBus());
    }

    @ApiStatus.Internal
    public static Map<Identifier, FluidData> entries() {
        return GLOBAL_REGISTRY;
    }
}
