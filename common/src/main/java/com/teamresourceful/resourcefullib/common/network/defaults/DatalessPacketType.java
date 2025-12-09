package com.teamresourceful.resourcefullib.common.network.defaults;

import com.teamresourceful.resourcefullib.common.network.Packet;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

public abstract class DatalessPacketType<T extends Packet<T>> extends AbstractPacketType<T> {

    protected final Supplier<T> factory;

    public DatalessPacketType(Identifier id, Supplier<T> factory) {
        super(id);
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
