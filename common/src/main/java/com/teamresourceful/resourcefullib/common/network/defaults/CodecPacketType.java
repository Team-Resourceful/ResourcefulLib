package com.teamresourceful.resourcefullib.common.network.defaults;

import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public interface CodecPacketType<T extends Packet<T>> extends PacketType<T> {

    ByteCodec<T> codec();

    @Override
    default void encode(T message, FriendlyByteBuf buffer) {
        codec().encode(message, buffer);
    }

    @Override
    default T decode(FriendlyByteBuf buffer) {
        return codec().decode(buffer);
    }

    abstract class Client<T extends Packet<T>> extends AbstractPacketType<T> implements ClientboundPacketType<T>, CodecPacketType<T> {

        private final ByteCodec<T> codec;

        public Client(Class<T> clazz, ResourceLocation id, ByteCodec<T> codec) {
            super(clazz, id);
            this.codec = codec;
        }

        @Override
        public ByteCodec<T> codec() {
            return codec;
        }
    }

    abstract class Server<T extends Packet<T>> extends AbstractPacketType<T> implements ClientboundPacketType<T>, CodecPacketType<T> {

        private final ByteCodec<T> codec;

        public Server(Class<T> clazz, ResourceLocation id, ByteCodec<T> codec) {
            super(clazz, id);
            this.codec = codec;
        }

        @Override
        public ByteCodec<T> codec() {
            return codec;
        }
    }
}
