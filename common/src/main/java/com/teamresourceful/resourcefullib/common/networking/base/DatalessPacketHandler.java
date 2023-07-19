package com.teamresourceful.resourcefullib.common.networking.base;

import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;

public abstract class DatalessPacketHandler<T extends Packet<T>> implements PacketHandler<T> {

    private final Supplier<T> factory;

    public DatalessPacketHandler(Supplier<T> factory) {
        this.factory = factory;
    }

    @Override
    public void encode(T message, FriendlyByteBuf buffer) {

    }

    @Override
    public T decode(FriendlyByteBuf buffer) {
        return factory.get();
    }
}
