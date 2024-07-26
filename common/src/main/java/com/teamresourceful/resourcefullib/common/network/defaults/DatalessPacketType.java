package com.teamresourceful.resourcefullib.common.network.defaults;

import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public interface DatalessPacketType<T extends Packet<T>> extends PacketType<T> {

    T create();

    @Override
    default void encode(T message, FriendlyByteBuf buffer) {

    }

    @Override
    default T decode(FriendlyByteBuf buffer) {
        return create();
    }

    abstract class Client<T extends Packet<T>> extends AbstractPacketType.Client<T> implements DatalessPacketType<T> {

        private final Supplier<T> factory;

        public Client(Class<T> clazz, ResourceLocation id, Supplier<T> factory) {
            super(clazz, id);
            this.factory = factory;
        }

        @Override
        public T create() {
            return factory.get();
        }
    }

    abstract class Server<T extends Packet<T>> extends AbstractPacketType.Server<T> implements DatalessPacketType<T> {

        private final Supplier<T> factory;

        public Server(Class<T> clazz, ResourceLocation id, Supplier<T> factory) {
            super(clazz, id);
            this.factory = factory;
        }

        @Override
        public T create() {
            return factory.get();
        }
    }
}
