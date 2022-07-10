package com.teamresourceful.resourcefullib.common.registry;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Supplier;

@ApiStatus.Experimental
public class ResourceSuppliedRegistry<T> extends SuppliedRegistry<ResourceLocation, T> {

    public ResourceSuppliedRegistry(String modId) {
        super(modId);
    }

    public <O extends T> Object<ResourceLocation, T> register(String id, Supplier<? extends O> supplier) {
        return super.register(new ResourceLocation(modId, id), supplier);
    }

}
