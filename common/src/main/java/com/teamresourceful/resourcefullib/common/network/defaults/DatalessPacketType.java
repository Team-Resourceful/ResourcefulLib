package com.teamresourceful.resourcefullib.common.network.defaults;

import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface DatalessPacketType<T extends Packet<T>> extends PacketType<T> {

    T create();

    @Override
    default void encode(T message, FriendlyByteBuf buffer) {

    }

    @Override
    default T decode(FriendlyByteBuf buffer) {
        return create();
    }
}
