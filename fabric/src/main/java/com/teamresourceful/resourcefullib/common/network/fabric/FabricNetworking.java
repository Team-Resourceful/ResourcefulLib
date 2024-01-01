package com.teamresourceful.resourcefullib.common.network.fabric;

import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class FabricNetworking implements Networking {

    private final Networking client;
    private final Networking server;

    public FabricNetworking(String modid, int protocolVersion, String channel) {
        if (FabricLoader.getInstance().getEnvironmentType().equals(EnvType.CLIENT)) {
            this.client = FabricClientNetworking.of(new ResourceLocation(modid, channel + "/v" + protocolVersion));
        } else {
            this.client = DummyNetworking.INSTANCE;
        }
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
    public boolean canSendToPlayer(ServerPlayer player, PacketType<?> type) {
        return server.canSendToPlayer(player, type);
    }
}
