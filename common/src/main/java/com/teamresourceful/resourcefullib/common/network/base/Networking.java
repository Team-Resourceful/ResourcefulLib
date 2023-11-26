package com.teamresourceful.resourcefullib.common.network.base;

import com.teamresourceful.resourcefullib.common.network.Packet;
import net.minecraft.server.level.ServerPlayer;

public interface Networking {


    <T extends Packet<T>> void register(ClientboundPacketType<T> type);

    <T extends Packet<T>> void register(ServerboundPacketType<T> type);

    <T extends Packet<T>> void sendToServer(T message);

    <T extends Packet<T>> void sendToPlayer(T message, ServerPlayer player);

    boolean canSendToPlayer(ServerPlayer player);
}
