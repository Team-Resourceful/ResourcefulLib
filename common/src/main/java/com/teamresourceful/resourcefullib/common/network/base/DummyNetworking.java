package com.teamresourceful.resourcefullib.common.network.base;

import com.teamresourceful.resourcefullib.common.network.Packet;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.ApiStatus;

/**
 * This class is a dummy implementation of {@link Networking} that is used when we need to
 * return a {@link Networking} instance but don't have one for the current environment.
 */
@ApiStatus.Internal
public final class DummyNetworking implements Networking {

    public static final DummyNetworking INSTANCE = new DummyNetworking();

    private DummyNetworking() {}

    @Override
    public <T extends Packet<T>> void register(ClientboundPacketType<T> type) {

    }

    @Override
    public <T extends Packet<T>> void register(ServerboundPacketType<T> type) {

    }

    @Override
    public <T extends Packet<T>> void sendToServer(T message) {

    }

    @Override
    public <T extends Packet<T>> void sendToPlayer(T message, ServerPlayer player) {

    }

    @Override
    public boolean canSendToPlayer(ServerPlayer player, PacketType<?> type) {
        return false;
    }
}
