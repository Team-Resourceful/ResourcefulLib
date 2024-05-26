package com.teamresourceful.resourcefullib.client.registry;

import com.teamresourceful.resourcefullib.client.fluid.data.ClientFluidProperties;
import com.teamresourceful.resourcefullib.client.fluid.registry.ResourcefulClientFluidRegistry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import org.jetbrains.annotations.ApiStatus;

public final class ResourcefulClientRegistryType<D, T extends ResourcefulRegistry<D>> {

    @ApiStatus.Experimental
    public static final ResourcefulClientRegistryType<ClientFluidProperties, ResourcefulClientFluidRegistry> FLUID = new ResourcefulClientRegistryType<>(
            ResourcefulClientFluidRegistry.class
    );

    private final Class<T> type;

    private ResourcefulClientRegistryType(Class<T> type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ResourcefulClientRegistryType{type=" + type + "}";
    }
}
