package com.teamresourceful.resourcefullib.common.network.fabric;

import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.Networking;
import com.teamresourceful.resourcefullib.common.network.base.ServerboundPacketType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class FabricNetworking implements Networking {

    private final FabricClientNetworking client;
    private final FabricServerNetworking server;

    public FabricNetworking(String modid, int protocolVersion, String channel) {
        this.client = new FabricClientNetworking(new ResourceLocation(modid, channel + "/v" + protocolVersion));
        this.server = new FabricServerNetworking(new ResourceLocation(modid, channel + "/v" + protocolVersion));
    }

    @Override
    public <T extends Packet<T>> void register(ClientboundPacketType<T> type) {
        client.register(type);
    }

    @Override
    public <T extends Packet<T>> void register(ServerboundPacketType<T> type) {
        server.register(type);
    }

    @Override
    public <T extends Packet<T>> void sendToServer(T message) {
        client.sendToServer(message);
    }

    @Override
    public <T extends Packet<T>> void sendToPlayer(T message, ServerPlayer player) {
        server.sendToPlayer(message, player);
    }

    @Override
    public boolean canSendToPlayer(ServerPlayer player) {
        return server.canSendToPlayer(player);
    }
}
