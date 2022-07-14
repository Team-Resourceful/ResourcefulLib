package com.teamresourceful.resourcefullib.common.registry;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public class ResourceDef<T> extends Definition<ResourceLocation, T> {

    public ResourceDef(ResourceSuppliedRegistry<T> supplier, ResourceLocation id) {
        super(supplier, id);
    }
}
