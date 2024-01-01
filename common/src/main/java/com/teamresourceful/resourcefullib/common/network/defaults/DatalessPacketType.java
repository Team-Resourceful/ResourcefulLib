package com.teamresourceful.resourcefullib.common.network.defaults;

import com.teamresourceful.resourcefullib.common.network.Packet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public abstract class DatalessPacketType<T extends Packet<T>> extends AbstractPacketType<T> {

    protected final Supplier<T> factory;

    public DatalessPacketType(Class<T> clazz, ResourceLocation id, Supplier<T> factory) {
        super(clazz, id);
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
