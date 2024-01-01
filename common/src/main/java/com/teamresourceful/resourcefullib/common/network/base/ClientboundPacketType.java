package com.teamresourceful.resourcefullib.common.network.base;

import com.teamresourceful.resourcefullib.common.network.Packet;

public interface ClientboundPacketType<T extends Packet<T>> extends PacketType<T> {

    Runnable handle(T message);
}
