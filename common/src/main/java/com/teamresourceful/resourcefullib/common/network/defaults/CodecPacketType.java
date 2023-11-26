package com.teamresourceful.resourcefullib.common.network.defaults;

import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
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
}
