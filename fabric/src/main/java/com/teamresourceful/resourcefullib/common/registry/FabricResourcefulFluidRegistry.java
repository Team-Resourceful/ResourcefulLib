package com.teamresourceful.resourcefullib.common.registry;

import com.teamresourceful.resourcefullib.client.fluid.fabric.ResourcefulFluidRenderHandler;
import com.teamresourceful.resourcefullib.common.fluid.data.FluidData;
import com.teamresourceful.resourcefullib.common.fluid.data.FluidProperties;
import com.teamresourceful.resourcefullib.common.fluid.data.InternalFluidData;
import com.teamresourceful.resourcefullib.common.fluid.registry.ResourcefulFluidRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;

import java.util.Collection;

public class FabricResourcefulFluidRegistry implements ResourcefulFluidRegistry {

    private final String id;
    private final RegistryEntries<FluidData> entries = new RegistryEntries<>();

    public FabricResourcefulFluidRegistry(String id) {
        this.id = id;
    }

    @Override
    public String namespace() {
        return this.id;
    }

    @Override
    public RegistryEntry<FluidData> register(String name, FluidProperties properties) {
        Identifier id = Identifier.fromNamespaceAndPath(this.id, name);
        return this.entries.add(new Entry(id, new InternalFluidData(id, properties)));
    }

    @Override
    public Collection<RegistryEntry<FluidData>> getEntries() {
        return this.entries.getEntries();
    }

    @Override
    public void init() {
        if (FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT) return;
        ClientLifecycleEvents.CLIENT_STARTED.register(_ -> this.stream().forEach(
                (entry) -> ResourcefulFluidRenderHandler.register(entry.getId(), entry.get())
        ));
    }
}
