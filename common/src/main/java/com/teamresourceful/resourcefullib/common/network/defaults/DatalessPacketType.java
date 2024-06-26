package com.teamresourceful.resourcefullib.common.network.defaults;

import com.teamresourceful.resourcefullib.common.network.Packet;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Supplier;

public abstract class DatalessPacketType<T extends Packet<T>> extends AbstractPacketType<T> {

    protected final Supplier<T> factory;

    public DatalessPacketType(ResourceLocation id, Supplier<T> factory) {
        super(id);
        this.factory = factory;
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "22.0")
    public DatalessPacketType(Class<T> clazz, ResourceLocation id, Supplier<T> factory) {
        super(clazz, id);
        this.factory = factory;
    }

    @Override
    public void encode(T message, RegistryFriendlyByteBuf buffer) {

    }

    @Override
    public T decode(RegistryFriendlyByteBuf buffer) {
        return factory.get();
    }
}
