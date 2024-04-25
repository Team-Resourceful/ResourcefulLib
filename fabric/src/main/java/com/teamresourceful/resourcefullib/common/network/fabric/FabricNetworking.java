package com.teamresourceful.resourcefullib.common.network.fabric;

import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.Networking;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.base.ServerboundPacketType;
import com.teamresourceful.resourcefullib.common.network.internal.NetworkPacketPayload;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class FabricNetworking implements Networking {

    private static final boolean IS_CLIENT = FabricLoader.getInstance().getEnvironmentType().equals(EnvType.CLIENT);

    private final ResourceLocation channel;

    public FabricNetworking(ResourceLocation channel, int protocolVersion) {
        this.channel = channel.withSuffix("/v" + protocolVersion);
    }

    @Override
    public <T extends Packet<T>> void register(ClientboundPacketType<T> type) {
        final var payloadType = type.type(this.channel);
        PayloadTypeRegistry.playS2C().register(payloadType, type.codec(payloadType));
        if (!IS_CLIENT) return;
        FabricClientNetworkHandler.register(payloadType, type);
    }

    @Override
    public <T extends Packet<T>> void register(ServerboundPacketType<T> type) {
        final var payloadType = type.type(this.channel);
        PayloadTypeRegistry.playC2S().register(payloadType, type.codec(payloadType));
        ServerPlayNetworking.registerGlobalReceiver(
                payloadType,
                (payload, context) -> type.handle(payload.packet()).accept(context.player())
        );
    }

    @Override
    public <T extends Packet<T>> void sendToServer(T message) {
        if (!IS_CLIENT) return;
        FabricClientNetworkHandler.send(this.channel, message);
    }

    @Override
    public <T extends Packet<T>> void sendToPlayer(T message, ServerPlayer player) {
        ServerPlayNetworking.send(player, new NetworkPacketPayload<>(message, this.channel));
    }

    @Override
    public boolean canSendToPlayer(ServerPlayer player, PacketType<?> type) {
        return ServerPlayNetworking.canSend(player, type.type(this.channel));
    }

}
