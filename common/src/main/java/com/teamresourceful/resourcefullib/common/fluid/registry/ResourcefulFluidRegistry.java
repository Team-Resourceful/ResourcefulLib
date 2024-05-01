package com.teamresourceful.resourcefullib.common.fluid.registry;

import com.teamresourceful.resourcefullib.common.fluid.data.FluidData;
import com.teamresourceful.resourcefullib.common.fluid.data.FluidProperties;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Supplier;

public interface ResourcefulFluidRegistry extends ResourcefulRegistry<FluidData> {

    /**
     * @hidden use {@link #register(String, FluidProperties.Builder)} instead
     */
    @ApiStatus.Internal
    @Deprecated
    @Override
    default <I extends FluidData> RegistryEntry<I> register(String id, Supplier<I> supplier) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Use register(String, FluidProperties.Builder) instead.");
    }

    RegistryEntry<FluidData> register(String id, FluidProperties.Builder builder);

    /**
     * @hidden For internal use only.
     */
    @ApiStatus.Internal
    record Entry(ResourceLocation id, FluidData data) implements RegistryEntry<FluidData> {

        @Override
        public FluidData get() {
            return this.data;
        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }
    }

}
