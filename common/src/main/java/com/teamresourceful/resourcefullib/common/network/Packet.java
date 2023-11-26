package com.teamresourceful.resourcefullib.common.network;

import com.teamresourceful.resourcefullib.common.network.base.PacketType;

public interface Packet<T extends Packet<T>> {
    PacketType<T> type();
}
