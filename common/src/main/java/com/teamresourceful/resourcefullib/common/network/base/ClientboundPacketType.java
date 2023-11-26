package com.teamresourceful.resourcefullib.common.network.base;

import com.teamresourceful.resourcefullib.common.network.Packet;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface ClientboundPacketType<T extends Packet<T>> extends PacketType<T> {

    Runnable handle(T message);
}
