package com.teamresourceful.resourcefullib.common.registry;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Supplier;

@ApiStatus.Experimental
public class ResourceSuppliedRegistry<T> extends SuppliedRegistry<ResourceLocation, T> {

    public ResourceSuppliedRegistry(String modId) {
        super(modId);
    }

    public static <T> ResourceSuppliedRegistry<T> create(String modId) {
        return new ResourceSuppliedRegistry<>(modId);
    }

    public <O extends T> ResourceDef<O> register(String id, Supplier<? extends O> supplier) {
        if (registered) throw new RuntimeException("Cannot register after registration has occurred");
        ResourceLocation res = new ResourceLocation(modId, id);
        needsRegistration.put(res, supplier);
        ResourceDef<? extends T> obj = new ResourceDef<>(this, res);
        registration.add(obj);
        //noinspection unchecked
        return (ResourceDef<O>) obj;
    }
}
